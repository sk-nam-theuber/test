package com.example.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProfileValidateService {
	
	private Environment env;
	
	public void validateProfile() {
		
		boolean flag = false;
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		
		for (String profile : profiles) {
			if(!profile.equals("dev") && !profile.equals("prod")) {
                throw new IllegalArgumentException("Invalid profile: " + profile);
			}
		}
	}
	
}
