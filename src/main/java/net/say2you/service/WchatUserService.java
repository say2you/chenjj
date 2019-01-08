package net.say2you.service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.say2you.dao.WchatUserDao;
import net.say2you.model.WchatUser;
@Service
public class WchatUserService {
	@Autowired
	private WchatUserDao uDao;
	/**
	 * 通用openid获取数据库里用户的信息
	 * @param param
	 * @return
	 */
	public WchatUser getUserByOpenId(String openId) {
		WchatUser user=null;
		try {
			user=uDao.getUserByOpenId(openId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return user;
		
	}
	/**
	 * 向数据库添加一个新用户的信息
	 * @param user
	 * @return
	 */
	public int addUser(WchatUser user) {
		int uid=0;
		try {
			uid=uDao.addUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}		 
		return uid;
	}

}
