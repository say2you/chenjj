package net.say2you.controller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import net.say2you.model.JWTPlayload;
import net.say2you.model.WchatUser;
import net.say2you.service.WchatUserService;
import net.say2you.util.ApplicationProperty;
import net.say2you.util.JWTUtil;
import net.say2you.util.JsonTools;
import net.say2you.util.MessageValue;
import net.say2you.util.PropertyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
public class JWTController {

	@Autowired
	private WchatUserService uService;
	
	/**
	 * 通过用户名或密码或手机号验证码等获取tgt码
	 * @param request  userName、password、exp、wCodew
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/auth/tickets",produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
	@ResponseBody
	public String getAccessToken(HttpServletRequest request,
			HttpServletResponse response){
		//返回结果的map
		 Map<String,Object> mp=new HashMap<String,Object>();
		 //参数map
		 
		String openId=request.getParameter("openId");		
		String exp=request.getParameter("exp");	
		long expTime=Long.parseLong(ApplicationProperty.getProperty("audience.expiresSecond"));		
		if(openId==null || openId.equals("") || openId.equals("null")) {
			 mp.put("status", false);
			 mp.put("messagecode",MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE);
		     mp.put("message",  PropertyUtil.getProperty(MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE));	
	        String str=JsonTools.Object2Json(mp);
	 		return str;		
		}
		
		//自定义的过期时间，如果没有定义将使用配置文件中的默认值
		if(exp!=null && !exp.equals("") && !exp.equals("null")) {
			try {
				long  etime=Long.parseLong(exp);
				if(etime>Long.parseLong(ApplicationProperty.getProperty("audience.maxExpiresSecond"))) {
					 mp.put("status", false);
					 mp.put("messagecode",MessageValue.TGT_EXPTIME_MAX_CODE);
				     mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_EXPTIME_MAX_CODE));
			        return JsonTools.Object2Json(mp);			 			
				}				
				expTime=etime;
			}catch(Exception e) {				
				 mp.put("status", false);
				 mp.put("messagecode",MessageValue.TGT_EXTTIME_ERROR);
			     mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_EXTTIME_ERROR));
		        String str=JsonTools.Object2Json(mp);
		 		return str;		
			}
			
		}
		WchatUser user=uService.getUserByOpenId(openId);
		if(user==null) {
			mp.put("status", false);
			mp.put("message", "未能找到用户信息");
			return JsonTools.Object2Json(mp);		 			
		}	
	     
		//设置playload 用来生成jwt
		JWTPlayload playLoad=new JWTPlayload();
		//token的颁发者
		playLoad.setIss(ApplicationProperty.getProperty("wchat.iss"));
		//接收token的一端
		playLoad.setAud("clientuser");
		//颁发token的公司
		playLoad.setServerCompany(ApplicationProperty.getProperty("wchat.company"));
		//该token的主题
		playLoad.setSub("login");
		//客户端的公司
		playLoad.setClientCompany("wcahat");
		//token的有效期，单位是毫秒
		playLoad.setExp(expTime);
		//设置用户的角色
		playLoad.setUserRole("clientuser");
		
         //创建tgt
		String accessToken=JWTUtil.createJWT(playLoad);   
		
		//向返回值中设置tgt与refresh token
         mp.put("status", true);
         mp.put("tgt", "bearer"+accessToken);//tgt
         mp.put("tgt_Expires", expTime);//tgt的有效期,单位是毫秒
         mp.put("Token_type", "bearer");//tgt 类型            
        return JsonTools.Object2Json(mp);
			
	}
	/**
	 * 检查tgt
	 * @param request  tgt 用户的tgt验证码
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/auth/checkTGT",produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
	@ResponseBody
	public String checkTgt(HttpServletRequest request,
			HttpServletResponse response) {
		String auth=request.getParameter("tgt");		
		Map<String,Object> mp=new HashMap<String,Object>();
		if(auth==null || auth.equals("") || auth.equals("null")) {
			mp.put("status", false);
			 mp.put("messagecode",MessageValue.TGT_UNDEFINED_CODE);
		     mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_UNDEFINED_CODE));	
			return JsonTools.Object2Json(mp);
		}
			 //httpRequest.getHeader("Authorization");	
		try {
			 if(auth.length()<7) {
				 mp.put("status", false);
				 mp.put("messagecode",MessageValue.TGT_UNDEFINED_CODE);
			     mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_UNDEFINED_CODE));	      
			     return JsonTools.Object2Json(mp);
			 }
		       
		            String headStr = auth.substring(0, 6).toLowerCase();		            
		            if (headStr.compareTo("bearer") == 0){ 	                  
		                auth = auth.substring(6, auth.length());
		                Claims claim=JWTUtil.parseJWT(auth, ApplicationProperty.getProperty("audience.base64Secret"));
		                if(claim==null) {
		                	mp.put("status", false);
		                	mp.put("messagecode",MessageValue.TGT_ERROR_CODE);
		          	        mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_ERROR_CODE));	      
		     				return JsonTools.Object2Json(mp);
		                }
		               
		                //取得当前时间  毫秒
		                long nowMillis = System.currentTimeMillis();
		                Date now=new Date(nowMillis);
		        		if(claim.getExpiration()!=null) {
		        		
		        			//创建tgt的时间
		        		    long exp=claim.getExpiration().getTime();
		        		    //剩余tgt的时间
		        		   long time=(exp-(now.getTime()))/1000;
		        		   mp.put("status", true);
		        		   mp.put("tgt_time_left", time+"秒");
		     			   mp.put("message", "tgt correct!");
		     			  return JsonTools.Object2Json(mp);
		        		}  	                
		            }  
		       
			
		}catch(SignatureException|MalformedJwtException e) {			
	        mp.put("status",false);
	        mp.put("messagecode",MessageValue.TGT_ERROR_CODE);
	        mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_ERROR_CODE));	       
	        return JsonTools.Object2Json(mp);
			
		}catch(ExpiredJwtException e) {
			 mp.put("status",false);
			 mp.put("messagecode",MessageValue.TGT_TIMEOUT_CODE);
		     mp.put("message", PropertyUtil.getProperty(MessageValue.TGT_TIMEOUT_CODE));
		     return JsonTools.Object2Json(mp);	     
		}	
		mp.put("status", false);
		mp.put("messagecode",MessageValue.TGT_ERROR_CODE);
	    mp.put("message",  PropertyUtil.getProperty(MessageValue.TGT_ERROR_CODE));	 
		return JsonTools.Object2Json(mp);
		
	}	
	
	@RequestMapping(value = "/auth/posthttp", produces = {"application/json;charset=UTF-8"},method = RequestMethod.GET)
	@ResponseBody
	public String postHttp(HttpServletRequest request,
			HttpServletResponse response){
		String token=request.getParameter("token");
		String url="http://localhost:8080/gisuni_authority/user/userlist";		
		Map<String, String> headers=new HashMap<String,String>();
		headers.put("Authorization", token);
		
		 String result = "";
	        BufferedReader in = null;
	      
	        try {
	            // 构造httprequest设置
	            RequestConfig config = RequestConfig.custom().setConnectTimeout(300)
	                    .setConnectionRequestTimeout(300).build();
	            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	            HttpGet htGet = new HttpGet(url);
	            // 添加http headers
	            if (headers != null && headers.size() > 0) {
	                for (String key : headers.keySet()) {
	                    htGet.addHeader(key, "bearer"+headers.get(key));
	                }
	            }
	            // 读取数据	           
	            HttpResponse r = client.execute(htGet);
	            in = new BufferedReader(new InputStreamReader(r.getEntity().getContent(), Charsets.UTF_8));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        } finally {
	            try {
	                if (in != null) {
	                    in = null;
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
		
	}

}
