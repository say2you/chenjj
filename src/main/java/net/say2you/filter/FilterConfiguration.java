package net.say2you.filter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import net.say2you.util.ApplicationProperty;
@Configuration
public class FilterConfiguration {
	@Autowired
    private JWTAuthFilter jwtFilter;
//	@Autowired
//	private CorsFilter ipFilter;
	//字符编码filter
	
	   @Bean
	    public FilterRegistrationBean<Filter> filterRegistrationEncoding() {
	        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();                      
	         
	        //注入过滤器
	        registration.setFilter(characterEncodingFilter());
	        //拦截规则
	        registration.addUrlPatterns("/*");
	        //过滤器名称
	        registration.setName("chacterEncoderFilter");
	        //是否自动注册 false 取消Filter的自动注册
	        registration.setEnabled(true);
	        //过滤器顺序	        
	        registration.setOrder(Integer.MAX_VALUE);
	       
	        return registration;
	    }
	  
	   //iplist filter
//	   @Bean
//	    public FilterRegistrationBean<CorsFilter> filterRegistrationIp() {
//	        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<CorsFilter>();
////	                                 System.out.println("sssssssss>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//	         
//	        //注入过滤器
//	        registration.setFilter(ipFilter);
//	        //拦截规则
//	        registration.addUrlPatterns("/*");
//	        //过滤器名称
//	        registration.setName("ipFilter");
//	        //是否自动注册 false 取消Filter的自动注册
//	        registration.setEnabled(true);
//	        //初始化参数
//	       Map<String,String> initParam=new HashMap<String,String>();
//	       initParam.put("toggle", InitProperty.getProperty("iptoggle"));
////	       System.out.println(InitProperty.getProperty("iptoggle")+"-------------------------------------");
//	        registration.setInitParameters(initParam);
//	        //过滤器顺序
//	        
////	        registration.setOrder(Integer.MAX_VALUE-2);
//	       
//	        return registration;
//	    }
	 
	   
	   @Bean
	    public FilterRegistrationBean<JWTAuthFilter> filterRegistration() {
	        FilterRegistrationBean<JWTAuthFilter> registration = new FilterRegistrationBean<JWTAuthFilter>();	         
	        //注入过滤器
	        registration.setFilter(jwtFilter);
	        //拦截规则
	        registration.addUrlPatterns("/*");
	        //过滤器名称
	        registration.setName("jwtFilter");
	        //是否自动注册 false 取消Filter的自动注册
	        registration.setEnabled(true);
	        //初始化参数
		       Map<String,String> initParam=new HashMap<String,String>();
		       initParam.put("toggle", ApplicationProperty.getProperty("jwttoggle"));		      
		       registration.setInitParameters(initParam);
	        //过滤器顺序	        
//	        registration.setOrder(Integer.MAX_VALUE-3);
	       
	        return registration;
	    }
 
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter c = new CharacterEncodingFilter();
        c.setEncoding("UTF-8");
        c.setForceRequestEncoding(true);
        c.setForceResponseEncoding(true);        
        return c;
    }

}
