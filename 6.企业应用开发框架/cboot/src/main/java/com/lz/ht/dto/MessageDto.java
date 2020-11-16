package com.lz.ht.dto;

import com.lz.ht.model.Message;




public class MessageDto extends Message{

	private Integer msgType = 0; // 0 我发送给别人 1 别人发送给我的
	
	private String fromUserPic;
	
	private String fromUserName;
	
	private String fromUserDeptName;
	
	private String toUserPic;
	
	private String toUserName;
	 
	private String toUserDeptName;
	
	private Long subListNumber;

	private Long currMaxCorder;
	
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserDeptName() {
		return fromUserDeptName;
	}

	public void setFromUserDeptName(String fromUserDeptName) {
		this.fromUserDeptName = fromUserDeptName;
	}

	public String getToUserDeptName() {
		return toUserDeptName;
	}

	public void setToUserDeptName(String toUserDeptName) {
		this.toUserDeptName = toUserDeptName;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getFromUserPic() {
		return fromUserPic;
	}

	public void setFromUserPic(String fromUserPic) {
		this.fromUserPic = fromUserPic;
	}

	public String getToUserPic() {
		return toUserPic;
	}

	public void setToUserPic(String toUserPic) {
		this.toUserPic = toUserPic;
	}

	public Long getSubListNumber() {
		return subListNumber;
	}

	public void setSubListNumber(Long subListNumber) {
		this.subListNumber = subListNumber;
	}

	public Long getCurrMaxCorder() {
		return currMaxCorder;
	}

	public void setCurrMaxCorder(Long currMaxCorder) {
		this.currMaxCorder = currMaxCorder;
	}
	 
	
	
	
}