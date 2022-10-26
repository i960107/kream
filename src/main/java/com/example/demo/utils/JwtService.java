package com.example.demo.utils;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.entity.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createAccessToken(Long userIdx, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    public String createJwtRefreshToken() {
        return UUID.randomUUID().toString();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(HttpServletRequest request) {
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public Map<String, Object> getUserInfo(String accessToken) throws BaseException {

        if (isTokenEmpty(accessToken)) throw new BaseException(EMPTY_TOKEN);

        if (isTokenExpired(accessToken)) throw new BaseException(EXPIRED_JWT);

        Jws<Claims> claims = getClaimsFromJwtToken(accessToken);
        // 3. userIdx 추출, role추출
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userIdx", claims.getBody().get("userIdx", Long.class));
        userInfo.put("role", claims.getBody().get("role", String.class));
        return userInfo;
    }

    public boolean isTokenExpired(String accessToken) throws BaseException {
        Jws<Claims> claims = getClaimsFromJwtToken(accessToken);
        return (claims.getBody().getExpiration().before(new Date()));
    }

    public boolean isTokenEmpty(String token) {
        return token == null || token.length() == 0;
    }

//    public GetAccessTokenRes refreshAccessToken(String accessToken, String refreshToken) throws BaseException {
//        if (isTokenEmpty(accessToken) || isTokenEmpty(refreshToken)) throw new BaseException(EMPTY_TOKEN);
//        Jws<Claims> claims = getClaimsFromJwtToken(accessToken);
//        Long userIdx = claims.getBody().get("userIdx", Long.class);
//        String role = claims.getBody().get("role", String.class);
//
//        userRepository.findByIdxAndRefreshTokenAndStatus(userIdx, refreshToken, "ACTIVATED")
//                .orElseThrow(() -> new BaseException(INVALID_REFRESH_TOKEN));
//
//        return new GetAccessTokenRes(createAccessToken(userIdx, role));
//
//    }


    public Jws<Claims> getClaimsFromJwtToken(String accessToken) throws BaseException {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception e) {
            throw new BaseException(INVALID_JWT);
        }
        return claims;
    }


}
