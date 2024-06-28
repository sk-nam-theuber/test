package com.example.demo.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Ems;

@Mapper
public interface TestDao {

	public List<Ems> findTop50();
	
	public List<Ems> findUberAutoCount(Ems ems);
	
	public List<Ems> findUberBetaCount(Ems ems);
	
	public void deleteUberAutoDataByDate(Ems ems);
	
	public void deleteUberBetaDataByDate(Ems ems);
}
