package net.say2you.service;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.util.Base64.Decoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.say2you.util.HTTPClientUtils;
import net.say2you.util.JsonTools;

@Service
public class WchatService {
	@Value("${wchat.appid}")
	private String wchatappid;
	@Value("${wchat.secret}")
	private String wchatsecret;
	@Value("${wchat.openidurl}")
	private String openidUrl;
	/**
	 * 根据用户的code获取用户的openid
	 * @param wchatCode
	 * @return
	 */
	public Map<String,Object> getUserOpenid(String wchatCode){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String param="appid="+wchatappid+"&secret="+wchatsecret+"&js_code="+wchatCode+"&grant_type=authorization_code";
		try {
			String responseStr=HTTPClientUtils.sendGet(openidUrl, param);
			if(responseStr==null || responseStr.equals("") || responseStr.equals("null")) {
				resultMap.put("status", false);
				resultMap.put("message", "错误：未能请示到数据");
				return resultMap;
			}
			
				Map<String,Object> reMap=JsonTools.json2Map(responseStr);
				if(reMap.get("errorcode")!=null &&  ((String)reMap.get("errcode")).equals("40029")) {
					//获取openid出错
					resultMap.put("status", false);
					resultMap.put("message", "错误：未能获取openid");
					return resultMap;
				}
				
				resultMap.put("openid", reMap.get("openid"));
				resultMap.put("session_key", reMap.get("session_key"));
				resultMap.put("status", true);
			
			
		}catch(Exception e) {
			resultMap.put("status", false);
			resultMap.put("message", "错误:"+e.getMessage());
			return resultMap;
		}
		return resultMap;
		
	}

	public Map<String,Object> getUserDetail(Map<String,String> param){
		Decoder decoder=Base64.getDecoder();
		// 被加密的数据
        byte[] dataByte = decoder.decode(param.get("encryptedData"));
        // 加密秘钥
        byte[] keyByte = decoder.decode(param.get("sessionKey"));
        // 偏移量
        byte[] ivByte = decoder.decode(param.get("iv"));
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
         int base = 16;
         if (keyByte.length % base != 0) {
             int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
             byte[] temp = new byte[groups * base];
             Arrays.fill(temp, (byte) 0);
             System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
             keyByte = temp;
         }
         // 初始化
         Security.addProvider(new BouncyCastleProvider());
//         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
         Cipher cipher =Cipher.getInstance("AES/CBC/NOPadding");
         SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
         AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
         parameters.init(new IvParameterSpec(ivByte));
         cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
         byte[] resultByte = cipher.doFinal(dataByte);
         if (null != resultByte && resultByte.length > 0) {
             String result = new String(resultByte, "UTF-8");  
             System.out.println("------------------------------result:"+result);
             return JsonTools.json2Map(result);
         }
     } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
     } catch (NoSuchPaddingException e) {
         e.printStackTrace();
     } catch (InvalidParameterSpecException e) {
         e.printStackTrace();
     } catch (IllegalBlockSizeException e) {
         e.printStackTrace();
     } catch (BadPaddingException e) {
         e.printStackTrace();
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     } catch (InvalidKeyException e) {
         e.printStackTrace();
     } catch (InvalidAlgorithmParameterException e) {
         e.printStackTrace();
     }        
        return null;

	}

}
