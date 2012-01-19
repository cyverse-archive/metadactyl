package org.iplantc.workflow.template.notifications;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.workflow.marshaller.NotificationMarshaller;

public class Notification {

	long hid;
	
	String idc;
	String name;
	String sender;
	String type;
	List<String> receivers = new LinkedList<String>();
	
	
	public String getIdc() {
		return idc;
	}
	public void setIdc(String id) {
		this.idc = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	
	public void addreceiver(String receiver){
		receivers.add(receiver);
	}
	
	
	public void accept(NotificationMarshaller marshaller) throws Exception{
		marshaller.visit(this);
		marshaller.leave(this);
		
	}
	
		
	public long getHid() {
		return hid;
	}
	public void setHid(long hid) {
		this.hid = hid;
	}
	
	
	
	
}
