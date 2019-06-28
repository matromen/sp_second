package com.bgkim.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data

public class Param {
	private int pageNum;
	private int pageAmount;
	
	private String searchType;
	private String searchValue;
		
	public Param(int pageNum, int pageAmount, String searchType, String searchValue) {
		this.pageNum = pageNum;
		this.pageAmount = pageAmount;		
		this.searchType = searchType;
		this.searchValue = searchValue;
	}
	
	public String[] getSearchTypeArr() {
		return this.searchType == null ? new String[] {} : this.searchType.split(",");
	}
}
