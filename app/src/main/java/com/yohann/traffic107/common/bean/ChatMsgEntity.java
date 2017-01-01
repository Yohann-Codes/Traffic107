package com.yohann.traffic107.common.bean;

/**
 * 一个消息的JavaBean
 * 
 * @author way
 * 
 */
public class ChatMsgEntity {
	private String name;//消息来自
	private String message;//消息内容
	private boolean isComMeg = true;// 是否为收到的消息

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatMsgEntity() {
	}

	public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
		super();
		this.name = name;
		this.message = text;
		this.isComMeg = isComMsg;
	}

}
