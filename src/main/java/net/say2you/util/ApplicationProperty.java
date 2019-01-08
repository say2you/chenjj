package net.say2you.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;


public class ApplicationProperty{
	private static final Logger logger = Logger.getLogger(ApplicationProperty.class);
    private static Properties props;
    static{
        loadProps();
    } 
    synchronized static private void loadProps(){
        logger.info("开始加载application.properties文件内容.......");
        props = new Properties();
        InputStream in = null; 
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("application.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("application.properties.properties文件流关闭出现异常");
            }
        }
        logger.info("加载properties文件内容完成...........");
        logger.info("properties文件内容：" + props);
    }
 
    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }
 
    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }       
        return props.getProperty(key, defaultValue);
    }

}
