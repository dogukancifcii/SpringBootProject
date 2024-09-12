package com.dogukan.security.jwt;

import com.dogukan.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
//@Slf4j //bu anatasyon assagidaki isi yapiyor aslinda.Loglama isine yariyor.
public class JwtUtils {

    //gelen hatalri burada log olarak kayit ettik.
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecret;

    //TODO: Generate JWT *******************************

    //Burada kullanici sisteme login olmali ve authentication yapmali
    //login olan kullaniciyi security contexte atiyoduk.Assagida o yuzden Authentication yazdik.Authentication nesnesi sayesinde login olan kullanicinin bilgisini alabilicez.
    //
    public String generateJwtToken(Authentication authentication) {
        //Burada anlik olan giris yapmis olan kullaniciyi bana getPrincipal methodu ile getiriyor.UserDetailImpl ile benim user nesneme ceviriyorum.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //unique bir bilgi olan usernameyi jwt token icerisine koyduk.
        //sebebi kime ait oldugunu bulmak icin unique bilgi koyduk.
        return generateTokenFromUsername(userDetails.getUsername());
        //username kullanarak jwt token create ettik
    }

    private String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username) //tokene username ekleme(token kime ait)
                .setIssuedAt(new Date()) //olusturma tarihi
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))//token'ın ne zaman sona ereceğini belirler.
                .signWith(SignatureAlgorithm.HS512, jwtSecret)//token’ı bir imza algoritması kullanarak imzalar. Bu imza, token'ın bütünlüğünü doğrulamak için gereklidir.
                .compact(); //JWT'nin tüm bu bilgileri birleştirerek nihai token'ı oluşturur ve string olarak geri döndürür.
    }

    //TODO: Validate JWT *******************************
    //jwt token kontrolu yaptigimiz yer.
    public boolean validateJwtToken(String jwtToken) {

        try {
            //parser jwt tokeni belli parcalara boler
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Jwt token is expired : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Jwt token is unsupported : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Jwt token is invalid : {}", e.getMessage());
        } catch (SignatureException e) {
            LOGGER.error("Jwt signature is invalid : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Jwt token is empty : {}", e.getMessage());
        }
        return false;
    }

    //TODO: getUsernameFromJWT *************************
    //tokenden username getiren yardimci method.
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()//subject bodyde bulunur
                .getSubject();//subjecti yani user name getirmis olduk.
    }

}
