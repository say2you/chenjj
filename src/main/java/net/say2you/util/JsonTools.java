package net.say2you.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.MapType;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class JsonTools {
	/**
	 * Map对象格式化为json
	 * @param mp
	 * @return
	 */
	public static String Object2Json(Map<String, Object> mp) {
		ObjectMapper objectMapper = new ObjectMapper();
		String str = null;
		Map<String, Object> obj = mp;
		if (mp != null && mp.size() > 0) {
			try {
				str = objectMapper.writeValueAsString(obj);
				return str;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonMessage("false", MessageValue.DATA_NOTFOUND,
				PropertyUtil.getProperty(MessageValue.DATA_NOTFOUND));

	}
   /**
    * 状态和消息格式化为json
    * @param status  消息的状态 true or false
    * @param messagecode  消息码 参考message.properties
    * @param message      消息内容
    * @return  json
    */
	public static String jsonMessage(String status, String messagecode,
			String message) {
		String jsonStr = "{\"status\":" + status + ",\"messagecode\":\""
				+ messagecode + "\",\"message\":\"" + message + "\"}";
		return jsonStr;
	}
	/**
	 * 将一个json格式的字符串格式化为Map<String,Object> 类型的数据
	 * @param jsonStr   json格式的字符串
	 * @return  Map<String,Object> 类型的数据
	 */
   public static Map<String,Object> json2Map(String jsonStr){
	   ObjectMapper mapper = new ObjectMapper();
	   MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);	
	   Map<String, Object> data=null;
	try {
		data = mapper.readValue(jsonStr, type);
	} catch (IOException e) {		
		e.printStackTrace();
	}
	   return data;
   }
   /**
    * 将一个json格式的字符串格式化为Map<String,String> 类型的数据
    * @param jsonStr json格式的字符串
    * @return Map<String,String> 类型的数据
    */
   public static Map<String,String> paramJson2Map(String jsonStr){
	   ObjectMapper mapper = new ObjectMapper();
	   MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class,String.class);	
	   Map<String, String> data=null;
	try {
		data = mapper.readValue(jsonStr, type);
	} catch (IOException e) {		
		e.printStackTrace();
	}
	   return data;
   }
   /**
    * json数组转list<Map<String,Object>>对象
    * @param jsonArrayStr
    * @return
    */
   public static List<Map<String,Object>> jsonArrayStr2List(String jsonArrayStr){
		JsonArray jsonArray = (JsonArray)new JsonParser().parse(jsonArrayStr);
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	for(int i = 0 ; i < jsonArray.size();i++){
    	    list.add(JsonTools.json2Map(jsonArray.get(i).toString()));
    	}
	   return list;
   }
   public static void main(String[] args){
	   Map<String,Object> obj=JsonTools.json2Map("{\"status\":\"true\",\"message\":\"helloworld\",\"name\":\"chenjj\"}");
	   System.out.printf("status is:%s and name is : %s",obj.get("status").toString(),obj.get("name").toString());
   }
}
