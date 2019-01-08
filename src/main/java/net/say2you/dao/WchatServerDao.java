package net.say2you.dao;
import java.util.List;
import java.util.Map;
import net.say2you.model.WchatServer;

public interface WchatServerDao {
/**
 * 根据指定条件查询服务器列表
 * @param param
 * @return
 */
public List<WchatServer> getServerList(Map<String,String> param);
public WchatServer getServerById(int id);
}
