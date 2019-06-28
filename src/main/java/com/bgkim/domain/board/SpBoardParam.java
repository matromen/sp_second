package com.bgkim.domain.board;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import com.bgkim.domain.Param;

import lombok.Data;

@Data

public class SpBoardParam extends Param{

	private String searchValue0="";
	private List<String> transSearchValue0;
	
	
	public SpBoardParam() {
		super(1, 10, "999", "");
	}
	
	public String getParamUrl() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
		
		builder.queryParam("pageNum", this.getPageNum());
		builder.queryParam("pageAmount", this.getPageAmount());
		builder.queryParam("searchType", this.getSearchType());		
		builder.queryParam("searchValue", this.getSearchValue());
		
		builder.queryParam("searchValue0", this.searchValue0);
		
		return builder.toUriString();
	}	
}
