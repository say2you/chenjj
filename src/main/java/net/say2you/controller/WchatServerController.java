package net.say2you.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.say2you.model.WchatServer;
import net.say2you.service.WchatServerService;
import net.say2you.util.JsonTools;
import net.say2you.util.MessageValue;

@RestController
public class WchatServerController {
	@Autowired
	private WchatServerService serverService;
	@RequestMapping(value = "/server/serverlist",produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
	@ResponseBody
	public String getServerList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,String> param=new HashMap<String,String>();
		Map<String,Object> result=new HashMap<String,Object>();
		String serverIp=request.getParameter("serverIp");
		String serverMemory=request.getParameter("serverMemory");
		String serverCpu=request.getParameter("serverCpu");
		String adminName=request.getParameter("adminName");
		String adminTel=request.getParameter("adminTel");
		String adminEmail=request.getParameter("adminEmail");
		String serverName=request.getParameter("serverName");
		String serverDesc=request.getParameter("serverDesc");
		String serverSlb=request.getParameter("serverSlb");
		String externalIp=request.getParameter("externalIp");
		String serverDisk=request.getParameter("serverDisk");
		if(serverIp!=null  && !serverIp.equals("") && !serverIp.equals("null")) {
			param.put("serverIp", serverIp);
		}
		if(serverMemory!=null && !serverMemory.equals("") && !serverMemory.equals("null")) {
			param.put("serverMemory", serverMemory);
		}
		if(serverCpu!=null && !serverCpu.equals("") && !serverCpu.equals("null")) {
			param.put("serverCpu", serverCpu);
		}
		if(adminName!=null && !adminName.equals("") && !adminName.equals("null")) {
			param.put("adminName", adminName);
		}
		if(adminTel!=null && !adminTel.equals("") && !adminTel.equals("null")) {
			param.put("adminTel", adminTel);
		}
		if(adminEmail !=null  && !adminEmail.equals("") && !adminEmail.equals("null")) {
			param.put("adminEmail", adminEmail);
		}
		if(serverName!=null  && !serverName.equals("") && !serverName.equals("null")) {
			param.put("serverName", serverName);
		}
		if(serverDesc!=null && !serverDesc.equals("") && !serverDesc.equals("null")) {
			param.put("serverDesc", serverDesc);
		}
		if(serverSlb!=null && !serverSlb.equals("null") && !serverSlb.equals("null")) {
			param.put("serverSlb", serverSlb);
		}
		if(externalIp!=null && !externalIp.equals("") && !externalIp.equals("null")) {
			param.put("externalIp", externalIp);
		}
		if(serverDisk!=null && !serverDisk.equals("") && !serverDisk.equals("null")) {
			param.put("serverDisk", serverDisk);
		}
		try {
			List<WchatServer> list=serverService.getServerList(param);
			result.put("status", true);
			result.put("result", list);
		}catch(Exception e) {
			result.put("status", false);
			result.put("message", "查询出错:"+e.getMessage());
		}
		return JsonTools.Object2Json(result);	
		
	}
	@RequestMapping(value = "/server/serverdesc",produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
	@ResponseBody
	public String getServerById(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> result=new HashMap<String,Object>();
			String serverId=request.getParameter("serverId");
			if(serverId==null || serverId.equals("") || serverId.equals("null")) {
				result.put("status", false);
				result.put("message", MessageValue.PARAM_NOTFOUND_OR_ERROR_CODE);
				return JsonTools.Object2Json(result);
			}
			try {
				WchatServer serverInfo=serverService.getServerById(Integer.parseInt(serverId));
				if(serverInfo==null) {
					result.put("status", false);
					result.put("message", "未查到相关数据");
					return JsonTools.Object2Json(result);
				}
				result.put("status", true);
				result.put("serverInfo", serverInfo);
				return JsonTools.Object2Json(result);
			}catch(Exception e) {
				result.put("status", false);
				result.put("message","查询异常:"+e.getMessage());
				return JsonTools.Object2Json(result);
			}	
		
	}

}
