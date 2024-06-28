package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.DeleteOldDataDao;
import com.example.demo.dao.TestDao;
import com.example.demo.dto.Ems;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteService {

	private final DeleteOldDataDao deleteDao;

	private final TestDao testDao;

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Value("${BACKUP_PATH}")
	private String backupPath;

	@Value("${FILE_NAME_A}")
	private String fileNameA;

	@Value("${FILE_NAME_B}")
	private String fileNameB;

	@Value("${FILE_NAME_C}")
	private String fileNameC;

	@Value("${FILE_NAME_LOG}")
	private String fileNameLog;

	@Value("${STATISTIC_PATH}")
	private String statisticPath;

	@Value("${STATISTIC_RESULT_FILE_NAME}")
	private String statisticResultFileName;

	@Value("${STATISTIC_RESULT_MMS_FILE_NAME}")
	private String statisticResultMmsFileName;

	@Value("${STATISTIC_RESULT_EMS_FILE_NAME}")
	private String statisticResultEmsFileName;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.username}")
	private String dataSourceUserName;

	@Value("${spring.datasource.password}")
	private String dataSourcePassword;

	public List<String> propertiesTest() {
		return Arrays.asList(backupPath, fileNameA, fileNameB, fileNameC, fileNameLog, statisticPath,
				statisticResultFileName, statisticResultMmsFileName, statisticResultEmsFileName, driverClassName,
				dataSourceUrl, dataSourceUserName, dataSourcePassword);
	}

	@Transactional
	public void targetMethod(int addDate) throws Exception {
		Ems ems = new Ems();
		ems.setField1(this.fullDate(addDate));

		List<Ems> countA = testDao.findUberAutoCount(ems);
		List<Ems> countB = testDao.findUberBetaCount(ems);
		
		String logContents = this.createDbDataLog(countA, countB, addDate) + this.createFileDataLog(addDate);
		String logFileName = fileNameLog + "_" + this.fullDate(addDate + 1) + ".dat";
				
		deleteFile(addDate);
		deleteDb(ems);
		sendDeleteHistoryMail(logContents);
		//createDeleteLogFile(backupPath, logFileName, logContents);
	}

	private String fullDate(int addDate) {

		LocalDateTime now = LocalDateTime.now().plusDays(addDate);
		String formatDate = formatter.format(now);
		log.info("----------------- deleteTargetDate : " + formatDate);
		return formatDate;
	}

	private void createDeleteLogFile(String directory, String fileName, String contents) {

		Path filePath = Paths.get(directory, fileName);
		try {
			Files.write(filePath, contents.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
					StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			new NoSuchFileException(fileName);
		}
	}

	private void sendDeleteHistoryMail(String logContents) throws Exception {

//		SendMailService mailService = new SendMailService();
//
//		try {
//			mailService.sendMail("[EMS | 파일 삭제]", logContents.replaceAll("\n", "<br/>"));
//		} catch (Exception e) {
//			mailService.sendMail("[EMS | 파일 삭제 에러]", e.getStackTrace().toString());
//		}

	}

	private void deleteDb(Ems ems) {
		testDao.deleteUberAutoDataByDate(ems);
		testDao.deleteUberBetaDataByDate(ems);
	}

	private void deleteFile(int addDate) throws Exception {
		// read file a,b, 통계파일 , 날짜별

		File statisticsFolder = new File(statisticPath + statisticsDirectory(addDate));

		if (statisticsFolder.exists()) {
			statisticsFolder.delete();
		}
	}

	private String statisticsDirectory(int addDate) {

		LocalDateTime now = LocalDateTime.now().plusDays(addDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String datePath = formatter.format(now);

		log.info("------------------ deleteTargetDirectoryName : " + datePath);

		return datePath;
	}

	private String loadFileAndCommIdCountA(String date) throws Exception {
		return this.loadFileAndCommIdCount(fileNameA, date);
	}

	private String loadFileAndCommIdCountB(String date) throws Exception {
		return this.loadFileAndCommIdCount(fileNameB, date);
	}

	private String loadFileAndCommIdCount(String fileName, String date) throws Exception {
		ParsingService ps = new ParsingService();
		List<Ems> list = ps.loadFile(backupPath + fileName + "." + date + ".dat", "\\^&");
		int size = list.size();
		if (size > 0) {
			Map<String, Integer> resultMap = new HashMap<String, Integer>();
			for (Ems item : list) {
				if (!resultMap.containsKey(item.getCommId())) {
					resultMap.put(item.getCommId(), 1);
				} else {
					int count = resultMap.get(item.getCommId());
					resultMap.put(item.getCommId(), count + 1);
				}
			}
			return resultMap.toString();
		}
		return "";
	}

	private String createDbDataLog(List<Ems> countA, List<Ems> countB, int addDate) {

		log.info("------------ countA Contents : " + countA.toString());
		log.info("------------ countB Contents : " + countB.toString());

		StringBuilder contents = new StringBuilder();
		contents.append("[");
		contents.append(this.fullDate(addDate));
		contents.append(" / A Type DB] - {");
		for (Ems item : countA) {
			contents.append(item.getCommId());
			contents.append(" : ");
			contents.append(item.getCount());
			contents.append(" | ");
		}
		contents.append("}\n");
		contents.append("[");
		contents.append(this.fullDate(addDate));
		contents.append(" / B Type DB] - {");
		for (Ems item : countB) {
			contents.append(item.getCommId());
			contents.append(" : ");
			contents.append(item.getCount());
			contents.append(" | ");
		}
		contents.append("}\n");

		log.info("------------ deleteDB Contents : " + contents.toString());
		return contents.toString();
	}

	private String createFileDataLog(int addDate) throws Exception {

		String contents = "";
		String fileAResult = " - " + this.loadFileAndCommIdCountA(this.fullDate(addDate));
		String fileBResult = " - " + this.loadFileAndCommIdCountB(this.fullDate(addDate));

		File aFile = new File(backupPath + fileNameA + "." + this.fullDate(addDate) + ".enc");
		File bFile = new File(backupPath + fileNameB + "." + this.fullDate(addDate) + ".enc");
		File cFile = new File(backupPath + fileNameC + "." + this.fullDate(addDate) + ".dat");

		contents += "[" + this.fullDate(addDate) + " / " + fileNameA + "]";
		contents += aFile.exists() == true ? " | DELETE :" + aFile.delete() : " | DON'T EXIST";
		contents += fileAResult;
		contents += "\n[" + this.fullDate(addDate) + " / " + fileNameB + "]";
		contents += bFile.exists() == true ? " | DELETE :" + bFile.delete() : " | DON'T EXIST";
		contents += fileBResult;
		contents += "\n[" + this.fullDate(addDate) + " / O_CMP_INFO]";
		contents += cFile.exists() == true ? " | DELETE :" + cFile.delete() : " | DON'T EXIST";

		File statisticsGMFile = new File(statisticPath + statisticsDirectory(addDate) + statisticResultFileName);
		File statisticsMMSFile = new File(statisticPath + statisticsDirectory(addDate) + statisticResultMmsFileName);
		File statisticsEMSFile = new File(statisticPath + statisticsDirectory(addDate) + statisticResultEmsFileName);
		File statisticsFolder = new File(statisticPath + statisticsDirectory(addDate));

		contents += "\n[" + this.fullDate(addDate - 1) + " / " + statisticResultFileName + "]";
		contents += statisticsGMFile.exists() == true ? " | DELETE :" + statisticsGMFile.delete() : " | DON'T EXIST";
		contents += "\n[" + this.fullDate(addDate - 1) + " / " + statisticResultMmsFileName + "]";
		contents += statisticsMMSFile.exists() == true ? " | DELETE :" + statisticsMMSFile.delete() : " | DON'T EXIST";
		contents += "\n[" + this.fullDate(addDate - 1) + " / " + statisticResultEmsFileName + "]";
		contents += statisticsEMSFile.exists() == true ? " | DELETE :" + statisticsEMSFile.delete() : " | DON'T EXIST";

		return contents;
	}

}
