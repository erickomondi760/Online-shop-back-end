package com.ecommerce.project.security;

import com.ecommerce.project.security.userdetails.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.app.tokenExpirationTime}")
    private int tokenExpirationTime;

    @Value("${spring.app.secretKey}")
    private String secretkey;

    @Value("${spring.app.jwtCookie}")
    private String jwtCookie;

    private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


//    public String getJwtFromHeader(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//
//        if(bearerToken != null && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public String generateCookieFromHeader(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }

        return null;
    }

    public ResponseCookie getResponseCookieFromUser(UserDetailsImpl userDetails){
        return ResponseCookie.from(jwtCookie,generateJwtFromUsername(userDetails.getUsername()))
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();
    }

    public ResponseCookie getJwtCleanCookie(){
        return ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
    }
    public String generateJwtFromUsername(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + tokenExpirationTime))
                .signWith(key())
                .compact();
    }

    public String generateUserNameFromJwt(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretkey));
    }

    public boolean validateJwt(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            return  true;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }
}
