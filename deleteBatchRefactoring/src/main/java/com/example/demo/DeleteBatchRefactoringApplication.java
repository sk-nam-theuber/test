package com.example.demo;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.dao.TestDao;
import com.example.demo.dto.Ems;
import com.example.demo.service.DeleteService;
import com.example.demo.service.ProfileValidateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DeleteBatchRefactoringApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext ac = SpringApplication.run(DeleteBatchRefactoringApplication.class, args);
		ProfileValidateService profileValidateService = ac.getBean(ProfileValidateService.class);
		DeleteService deleteService = ac.getBean(DeleteService.class);

		profileValidateService.validateProfile();
		deleteService.targetMethod(-1);
	}

}
