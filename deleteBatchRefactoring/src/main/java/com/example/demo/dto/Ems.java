package com.example.demo.dto;

import lombok.Data;

@Data
public class Ems {

	private String commId;
	private String leafId;
	private String channelId;
	private String userId;
	private String name;
	private String phone;
	private String email;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String description;
	private String type;
	private int count;

	@Override
	public String toString() {
		return "Ems [commId=" + commId + ", leafId=" + leafId + ", channelId=" + channelId + ", userId=" + userId
				+ ", name=" + name + ", phone=" + phone + ", email=" + email + ", field1=" + field1 + ", field2="
				+ field2 + ", field3=" + field3 + ", field4=" + field4 + ", field5=" + field5 + ", field6=" + field6
				+ ", field7=" + field7 + ", field8=" + field8 + ", description=" + description + "]";
	}

}
