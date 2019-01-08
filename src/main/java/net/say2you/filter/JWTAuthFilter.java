package net.say2you.filter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import net.say2you.util.ApplicationProperty;
import net.say2you.util.JWTUtil;
import net.say2you.util.JsonTools;
import net.say2you.util.MessageValue;
import net.say2you.util.PropertyUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;


@Component
public class JWTAuthFilter  implements Filter {	
	private  FilterConfig filterConfig;	
	private String[] urlPass = {"/auth/","/user/registerUser"};
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest httpRequest = (HttpServletRequest)request; 
		 HttpServletResponse httpResponse = (HttpServletResponse) response; 
		 HttpSession session = ((HttpServletRequest) request).getSession(true);
		 PrintWriter pr=null;
			// 获取客户请求的页面
		String userUrl = httpRequest.getRequestURL().toString();
		
		try{
			//取出所有的初始化参数，如果过滤器关闭，则不执行任何关闭动作
			 String initToggle = filterConfig.getInitParameter("toggle");
			 if(initToggle.equals("off")){
				 chain.doFilter(request, response);  
                 return; 
			 }
				// 这些url不签权
				if (isUrlPass(userUrl, urlPass)) {
					chain.doFilter(httpRequest, httpResponse);
					return;
				}
			
				//检查session
			if(session.getAttribute("sessionid")!=null ){
				String au=(String)session.getAttribute("sessionid");
				if(au.equals(session.getId())){
					//从header里取得客户端的操作请求，此动作需要客户端在header中进行设置
					  String sessionAction = httpRequest.getHeader("Action");
					  //客户端请求销毁session
					  if(sessionAction!=null && sessionAction.equals(PropertyUtil.getProperty("audience.sessionAction"))){
						  session.removeAttribute("sessionid");
						  session.invalidate();
						  return;
					  }
					 chain.doFilter(request, response);  
	                 return; 
				}
				 
        	}
			//校验 tgt
			 String auth =  httpRequest.getParameter("tgt");
		        if ((auth != null) && (auth.length() > 7)){  
		            String headStr = auth.substring(0, 6).toLowerCase();		            
		            if (headStr.compareTo("bearer") == 0){ 	                  
		                auth = auth.substring(6, auth.length());
		                Claims claim=JWTUtil.parseJWT(auth, ApplicationProperty.getProperty("audience.base64Secret"));
		                if (claim!= null){ 
//		                	System.out.println("从auth设置session并访问接口:"+session.getId());
		                	session.setAttribute("sessionid", session.getId());
		                	//设置默认session时长30分钟
		                	session.setMaxInactiveInterval(Integer.parseInt(ApplicationProperty.getProperty("audience.sessionSecond")));
		                    chain.doFilter(request, response);  
		                    return;  
		                }  
		            }  
		        } 	        			
				httpResponse.setCharacterEncoding("UTF-8");    
			    httpResponse.setContentType("application/json; charset=utf-8");  
		        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);   
		        Map<String,Object> map=new HashMap<String,Object>(); 
		        map.put("status",false);
		        map.put("messagecode",MessageValue.TGT_UNDEFINED_CODE);
			    map.put("message",  PropertyUtil.getProperty(MessageValue.TGT_UNDEFINED_CODE));
		        String str=JsonTools.Object2Json(map);	
		        pr=httpResponse.getWriter();
		        pr.write(str);
		        pr.close();		        
		          
		}catch(SignatureException|MalformedJwtException e){
			Map<String,Object> map=new HashMap<String,Object>(); 
	        map.put("status",false);
	        map.put("messagecode",MessageValue.TGT_ERROR_CODE);
		    map.put("message",  PropertyUtil.getProperty(MessageValue.TGT_ERROR_CODE));
	        String str=JsonTools.Object2Json(map);	
	        pr=httpResponse.getWriter();
	        pr.write(str);
	        pr.close();		
		}catch(ExpiredJwtException e){
			//token超时
			Map<String,Object> map=new HashMap<String,Object>(); 
	        map.put("status",false);
	        map.put("messagecode",MessageValue.TGT_TIMEOUT_CODE);
		    map.put("message",  PropertyUtil.getProperty(MessageValue.TGT_TIMEOUT_CODE));
	        String str=JsonTools.Object2Json(map);	
	        pr=httpResponse.getWriter();
	        pr.write(str);
	        pr.close();		
		}finally{
			if(pr!=null){
				pr.close();
			}		
		}
        return;  
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {		
		this.filterConfig=filterConfig;
	}
	
	/**
	 * 检查url是否在指定的数组中
	 * @param reqUrl
	 * @param urlGroup
	 * @return
	 */

	private boolean isUrlPass(String reqUrl, String[] urlGroup) {
		boolean isPass = false;
		for (String userUrl : urlGroup) {
			if (reqUrl.indexOf(userUrl) >= 0) {
				isPass = true;
				break;
			}
		}
		return isPass;
	}

}
