package net.say2you.model;

import java.util.Date;

public class WchatServer {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerMemory() {
		return serverMemory;
	}
	public void setServerMemory(String serverMemory) {
		this.serverMemory = serverMemory;
	}
	public String getServerCpu() {
		return serverCpu;
	}
	public void setServerCpu(String serverCpu) {
		this.serverCpu = serverCpu;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminTel() {
		return adminTel;
	}
	public void setAdminTel(String adminTel) {
		this.adminTel = adminTel;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerSlb() {
		return serverSlb;
	}
	public void setServerSlb(String serverSlb) {
		this.serverSlb = serverSlb;
	}
	public String getServerDesc() {
		return serverDesc;
	}
	public void setServerDesc(String serverDesc) {
		this.serverDesc = serverDesc;
	}
	private String serverIp;
	private String serverMemory;
	private String serverCpu;
	private String adminName;
	private String adminTel;
	private String adminEmail;
	private String serverName;
	private String serverSlb;
	private String serverDesc;
	private String externalIp;
	private String serverDisk;
	private Date useStartDate;
	public Date getUseStartDate() {
		return useStartDate;
	}
	public void setUseStartDate(Date useStartDate) {
		this.useStartDate = useStartDate;
	}
	public Date getUseEndDate() {
		return useEndDate;
	}
	public void setUseEndDate(Date useEndDate) {
		this.useEndDate = useEndDate;
	}
	private Date useEndDate;
	public String getExternalIp() {
		return externalIp;
	}
	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}
	public String getServerDisk() {
		return serverDisk;
	}
	public void setServerDisk(String serverDisk) {
		this.serverDisk = serverDisk;
	}

}
