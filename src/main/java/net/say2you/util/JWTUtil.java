package net.say2you.util;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import net.say2you.model.JWTPlayload;

public class JWTUtil {
	/**
	 * 解析jWt token
	 * @param jsonWebToken 客户端传过来的jwt token
	 * @param base64Security  客户端与服务端共同持有的密钥
	 * @return
	 * @throws SignatureException  token解析异常
	 * @throws MalformedJwtException
	 * @throws ExpiredJwtException  时间失效异常
	 */
	public static Claims parseJWT(String jsonWebToken, String base64Security)
			throws SignatureException, MalformedJwtException,
			ExpiredJwtException {

		Claims claims = Jwts
				.parser().setAllowedClockSkewSeconds(120)
				.setSigningKey(
						DatatypeConverter.parseBase64Binary(base64Security)).setAllowedClockSkewSeconds(60)
				.parseClaimsJws(jsonWebToken).getBody();
		
		return claims;

	}
    /**
     * 创建一个JWTtoken
     * @param playLoad  JWT token的生成参数，具体请参考JWTPlayload bean
     * @return
     */
	public static String createJWT(JWTPlayload playLoad) {
		//签名密码
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		//取得当前系统时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		// 生成签名密钥，此秘钥应该是user与server共同持有，以后凭此验证token
		byte[] apiKeySecretBytes = DatatypeConverter
				.parseBase64Binary(ApplicationProperty
						.getProperty("audience.base64Secret"));
		Key signingKey = new SecretKeySpec(apiKeySecretBytes,
				signatureAlgorithm.getJcaName());

		// 添加构成JWT的参数
		JwtBuilder builder = Jwts
				.builder()
				.setHeaderParam("typ", "JWT")
				// .setIssuedAt("")//创建时间
				// .setSubject("")//主题，差不多是一些个人信息
				.claim("userRole",playLoad.getUserRole())
				.claim("clientCompany", playLoad.getClientCompany())
				.claim("clientAction", "getTGT")
				.setIssuer(playLoad.getIss())// 发送者签名
				.setAudience(playLoad.getAud()) // 接收者userName
				.signWith(signatureAlgorithm, signingKey); // 第三段密钥
		// 添加Token过期时间
		if (playLoad.getExp() >= 0) {
			// 创建过期时间
			long expMillis = nowMillis+ playLoad.getExp();
			// 获取现在时间
			Date exp = new Date(expMillis);
			// 系统之前时间的token都是不被承认的			
			builder.setExpiration(exp).setNotBefore(now);
		}
		// 生成JWT
		return builder.compact();
	}
}
