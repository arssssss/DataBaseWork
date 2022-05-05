package zyc.work.databasework.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zyc.work.databasework.enums.token.TokenType;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenUtil {
    public String secret="fs8d77x6v8b8m7sx4s199bdcss";

    public String issuer="zyc";

    public String CreateToken(long timeMillis, String userId, TokenType type){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        long nowMillis=System.currentTimeMillis(),
                expireMillis=nowMillis+timeMillis;

        String token = JWT.create()
                .withIssuer(issuer)    // 发布者
                .withIssuedAt(new Date(nowMillis))   // 生成签名的时间
                .withExpiresAt(new Date(expireMillis))   // 生成签名的有效期,小时
                .withClaim(type.name(),userId) // 插入数据
                .sign(algorithm);
        return token;
    }

    public boolean IsEffective(String token){
        try {
            if(StringUtils.hasText(token)){
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer(issuer)
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

    public String getId(String token, TokenType type){
        try {
            if(StringUtils.hasText(token)){
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer(issuer)
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                return jwt.getClaim(type.name()).asString();
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }


}
