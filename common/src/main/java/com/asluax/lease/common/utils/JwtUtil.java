package com.asluax.lease.common.utils;

import com.asluax.lease.common.exception.MyException;
import com.asluax.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final long TOKEN_EXPIRATION = 60 * 60 * 1000L;
    private static final SecretKey TOKEN_SIGN_KEY = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {
        return  Jwts.builder().
                setSubject("USER_INFO").
                setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION)).
                claim("userId", userId).
                claim("username", username).
                signWith(TOKEN_SIGN_KEY).
                compressWith(CompressionCodecs.GZIP).
                compact();
    }

    public static Claims parseToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().
                    setSigningKey(TOKEN_SIGN_KEY).
                    build().parseClaimsJws(token);
            return claimsJws.getBody();

        } catch (ExpiredJwtException e) {
            throw new MyException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new MyException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}