package net.say2you.dao;
import java.util.Map;
import net.say2you.model.WchatUser;

public interface WchatUserDao {
	public int addUser(WchatUser user);
	public WchatUser getUserByOpenId(String openId);

}
