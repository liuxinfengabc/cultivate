package com.lz.ht.dto;

import com.lz.ht.model.Orgnews;

public class OrgnewsDto extends Orgnews{

	
	private String publishTimeStart;
	
	private String publishTimeEnd;

	public String getPublishTimeStart() {
		return publishTimeStart;
	}

	public void setPublishTimeStart(String publishTimeStart) {
		this.publishTimeStart = publishTimeStart;
	}

	public String getPublishTimeEnd() {
		return publishTimeEnd;
	}

	public void setPublishTimeEnd(String publishTimeEnd) {
		this.publishTimeEnd = publishTimeEnd;
	}
	
	
}