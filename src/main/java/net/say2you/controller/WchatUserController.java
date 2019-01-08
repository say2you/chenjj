package net.say2you.controller;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.say2you.model.WchatUser;
import net.say2you.service.WchatService;
import net.say2you.service.WchatUserService;
import net.say2you.util.JsonTools;
import net.say2you.util.MessageValue;
import net.say2you.util.PropertyUtil;

@RestController

public class WchatUserController {
	@Autowired
	private WchatService wchatService;
	@Autowired
	private WchatUserService userService;
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	String getUser(@RequestParam(value="userName")String userName) {
		return "hello:"+userName;
	}
	/**
	 * 用户登录时，在数据库里注册一个用户
	 * @param request  wchatCode
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/registerUser",produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
	@ResponseBody
	public String  registerUser(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String wchatCode=request.getParameter("wchatCode");
//		String encryptedData=request.getParameter("encryptedData");
//		String iv=request.getParameter("iv");
		String nickName=request.getParameter("nickName");
		if(wchatCode==null || wchatCode.equals("") || wchatCode.equals("null")) {
		     resultMap.put("status", false);
		     resultMap.put("message", PropertyUtil.getProperty(MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE));
		     return JsonTools.Object2Json(resultMap);
		}
//		if(encryptedData==null || encryptedData.equals("") || encryptedData.equals("null")) {
//			resultMap.put("status", false);
//		     resultMap.put("message", MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE);
//		     return JsonTools.Object2Json(resultMap);
//		}
//		if(iv==null || iv.equals("") || iv.equals("null")) {
//			resultMap.put("status", false);
//		     resultMap.put("message", MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE);
//		     return JsonTools.Object2Json(resultMap);
//		}
		if(nickName==null || nickName.equals("") || nickName.equals("null")) {
			resultMap.put("status", false);
		     resultMap.put("message",  PropertyUtil.getProperty(MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE));
		     return JsonTools.Object2Json(resultMap);
		}
		//根据用户的wchatCode取得用户的openid
		Map<String,Object> resMap=wchatService.getUserOpenid(wchatCode);
		if(resMap==null || !((boolean)resMap.get("status"))) {
			resultMap.put("status", false);
			resultMap.put("message", "获取用户openid失败");
			return JsonTools.Object2Json(resultMap);
		}
		
			//获取用户的openId与sessionKey
			String openId=(String)resMap.get("openid");
			String sessionKey=(String)resMap.get("session_key");
			//查询该用户是否已注册到数据库
			WchatUser user=userService.getUserByOpenId(openId);			
			if(user!=null) {
				resultMap.put("isRegister", true);
				resultMap.put("status", true);
				resultMap.put("openId", openId);
				resultMap.put("sessionKey", sessionKey);
				return JsonTools.Object2Json(resultMap);
			}
			//base64解码用户的加密文获得手机号
//			Map<String,String> param=new HashMap<String,String>();
//			param.put("sessionKey", sessionKey);
//			param.put("encryptedData", encryptedData);
//			param.put("iv", iv);
			//此处不再获得用户手机号
//			Map<String,Object> returnMap=wchatService.getUserDetail(param);
//			if(returnMap==null) {
//			   resultMap.put("status", false);
//			   resultMap.put("message", "未能解析用户信息");
//			   return JsonTools.Object2Json(resultMap);
//			}
//			String telphone=(String)returnMap.get("phoneNumber");
//			System.out.println(telphone+"============================================");
			WchatUser uinfo=new WchatUser();
			uinfo.setOpenId(openId);
//			uinfo.setTelPhone(telphone);
			uinfo.setNickName(nickName);
			int intNum=userService.addUser(uinfo);
			if(intNum>0) {
				resultMap.put("status", false);
				resultMap.put("message", "添加用户失败");
				
			}else {
				resultMap.put("status", true);
				resultMap.put("openId", openId);	
				resultMap.put("sessionKey",sessionKey);		
				resultMap.put("message", "添加用户成功");
			}
			
		    return JsonTools.Object2Json(resultMap);
		
	}

}
