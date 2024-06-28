package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.Ems;
import com.example.demo.util.StringUtils;
import com.humuson.tms.common.security.HumusonDecryptor;

public class ParsingService {

	private final String SEED_ENC_KEY = "amail0722!@";

	public List<Ems> loadFile(String filePath, String pattern) throws Exception{
		File file = new File(filePath);
		List<Ems> result = new ArrayList<Ems>();
		if(file.exists()){
			String temp = "";
			try(
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "euc-kr");
				BufferedReader br = new BufferedReader(isr)) {

				while((temp = br.readLine()) != null) {
					if("\\^&".equals(pattern)){
						Ems e =parseTargetFile(temp, pattern);
						if (e != null) result.add(e);
					} else if("\\|".equals(pattern)){
						Ems e =parseConfigFile(temp, pattern);
						if (e != null) result.add(e);
					}
				}
			} catch (Exception e){
				e.printStackTrace();
				throw new Exception("파싱오류 입니다.");
			}
	    }
		return result;
	}
	
	public Ems parseTargetFile(String line, String pattern) throws Exception {
		Ems ems = new Ems();
		String[] list = line.split(pattern);
		if (!"1000dtsfg08k".equals(list[0]) || !"1000dwt3dxr9".equals(list[0])) {
			ems.setCommId(list[0]);
			ems.setUserId(list[5]);
			ems.setLeafId(list[11]);
			ems.setChannelId(list[14]);
			ems.setName(StringUtils.isNullorEmpty(list[16]) ? "" : HumusonDecryptor.decrypt(list[16], SEED_ENC_KEY, false));
			ems.setPhone(StringUtils.isNullorEmpty(list[17]) ? "" : HumusonDecryptor.decrypt(list[17], SEED_ENC_KEY, false));
			ems.setEmail(StringUtils.isNullorEmpty(list[18]) ? "" : HumusonDecryptor.decrypt(list[18], SEED_ENC_KEY, false));
			ems.setField1(list[26]); //서비스센터명
			ems.setField2(StringUtils.isNullorEmpty(list[27]) ? "" : HumusonDecryptor.decrypt(list[27], SEED_ENC_KEY, false)); //정비소 대표명
			ems.setField3(StringUtils.isNullorEmpty(list[28]) ? "" : HumusonDecryptor.decrypt(list[28], SEED_ENC_KEY, false)); //정비소 연락처
			ems.setField4(list[10]); //A타입_휴면 안내인 경우 : 휴면예정일, B타입_활용동의이력안내인 경우 : 동의날짜
			ems.setField5(list[19]); //CRM PROMOTION 쿠폰
			return ems;
		}
		return null;
	}
	
	public Ems parseConfigFile(String line, String pattern){
		Ems ems = new Ems();
		String[] list = line.split(pattern);
		
		if (!"1000dtsfg08k".equals(list[0]) || !"1000dwt3dxr9".equals(list[0])) {
			ems.setCommId(list[0]);
			ems.setName(list[1]);
			ems.setDescription(list[2]);
			ems.setType(list[10]);
			return ems;
		}
		return null;
	}
	
	public void moveFile(String filePath, String moveFilePath){
		File file = new File(filePath);
		File moveFile = new File(moveFilePath);
		file.renameTo(moveFile);
	}


	
}
