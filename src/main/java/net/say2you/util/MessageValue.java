package net.say2you.util;

public class MessageValue {
	public static final String DATA_NOTFOUND="1000";
	//参数错误或不存在
	public static final String PARAM_NOTFOUND_OR_ERROR_CODE="2000";
	//tgt有效时长超过最大值8小时
	public static final String TGT_EXPTIME_MAX_CODE="3000";
	//tgt超时
	public static final String TGT_EXTTIME_ERROR="3001";
	//tgt长度错误
	public static final String TGT_UNDEFINED_CODE="3002";
	//tgt 解析错误 
	public static final String TGT_ERROR_CODE="3003";
	//tgt超时错误 
	public static final String TGT_TIMEOUT_CODE="3004";
}
