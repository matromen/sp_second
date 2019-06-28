package com.bgkim.domain;

import lombok.Getter;

@Getter

public class ListPageConfig {
	private int startPage;
	private int endPage;
	private boolean prev, next;
	private int pageNumber;
	
//	private Param pbean;
//	private int total;
	
	
	public ListPageConfig(Param pbean, int total) {
//		this.pbean = pbean;
//		this.total = total;
		
		int aboutEndPage;
		
		aboutEndPage = (int)(Math.ceil(pbean.getPageNum()/10.0))*10; //대략적인 러프한 마지막 page. ex) 1~10 ->10, 11~20 -> 20 ....
		this.startPage = aboutEndPage - 9;
		
		int realEndPage = (int)Math.ceil((total * 1.0)/pbean.getPageAmount());
		
		if(aboutEndPage > realEndPage) {  // realEndPage 11page... aboutEndPage 20page
			aboutEndPage = realEndPage;
		}
		
		
		
		this.prev = this.startPage > 10;
		this.next = realEndPage > aboutEndPage;
		
		this.endPage = aboutEndPage;
		
		
		pageNumber = total - ((pbean.getPageNum()-1)*pbean.getPageAmount());  //page 순번
	}
	
	
}
