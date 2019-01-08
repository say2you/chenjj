package net.say2you.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.say2you.dao.WchatServerDao;
import net.say2you.model.WchatServer;
@Service
public class WchatServerService {
	@Autowired
	private WchatServerDao serverDao;
	public List<WchatServer> getServerList(Map<String,String> param){
		List<WchatServer> list=serverDao.getServerList(param);
		return list;
	}
	public WchatServer getServerById(int id) {
		WchatServer server=	serverDao.getServerById(id);
		return server;
	}

}
