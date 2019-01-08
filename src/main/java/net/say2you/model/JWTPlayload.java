package net.say2you.model;

public class JWTPlayload {
	private String iss;//token的颁发者
	private String sub;// token的主题
	private String aud;//接收jwt的一方
	private long exp;//jwt的过期时间，这个过期时间必须要大于签发时间
	private String nbf;//定义在什么时间之前，该jwt都是不可用的.
	private String iat;//jwt token的创建时间
	private String jti;//jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。相当于token的id
	private String serverCompany;//公共信息之本公司名称
	private String clientCompany;//私有信息之使用者公司
	private String clientAction;//私有信息之客户端操作
	private String userRole;//用户的角色
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}

	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public String getNbf() {
		return nbf;
	}
	public void setNbf(String nbf) {
		this.nbf = nbf;
	}
	public String getIat() {
		return iat;
	}
	public void setIat(String iat) {
		this.iat = iat;
	}
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	public String getServerCompany() {
		return serverCompany;
	}
	public void setServerCompany(String serverCompany) {
		this.serverCompany = serverCompany;
	}
	public String getClientCompany() {
		return clientCompany;
	}
	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}
	public String getClientAction() {
		return clientAction;
	}
	public void setClientAction(String clientAction) {
		this.clientAction = clientAction;
	}

}
