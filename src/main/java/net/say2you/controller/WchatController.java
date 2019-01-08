package net.say2you.controller;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import net.say2you.service.WchatService;
import net.say2you.util.JsonTools;

@RestController
public class WchatController {
	@Autowired
	private WchatService wchatService;
	@RequestMapping(value = "/wchat/toQuery",produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
	@ResponseBody
	public String getWchatCode(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String wchatCode=request.getParameter("wchatCode");		
		if(wchatCode==null || wchatCode.equals("") || wchatCode.equals("null")) {
			resultMap.put("status", false);
			resultMap.put("message", "参数缺失或不正确");
			return JsonTools.Object2Json(resultMap);
		}		
		resultMap=wchatService.getUserOpenid(wchatCode);		
		return JsonTools.Object2Json(resultMap);
		
	}

}
