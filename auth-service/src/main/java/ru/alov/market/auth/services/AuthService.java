package ru.alov.market.auth.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.JwtRequest;
import ru.alov.market.api.dto.JwtResponse;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.converters.UserConverter;
import ru.alov.market.auth.entities.User;
import ru.alov.market.auth.utils.JwtTokenUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserConverter userConverter;

    @Value("${utils.username.prefix}")
    private String usernamePrefix;

//    @Value("${jwt.refresh.lifetime}")
//    private Integer refreshTokenLifetime;

    public JwtResponse getJwtTokens(@NonNull JwtRequest jwtRequest) {
        User user = authenticateUser(jwtRequest.getUsername(), jwtRequest.getPassword());
        String accessToken = jwtTokenUtil.generateAccessToken(userConverter.entityToDto(user));
        String refreshToken = jwtTokenUtil.generateRefreshToken(userConverter.entityToDto(user));
        String key = usernamePrefix + user.getUsername();
        redisTemplate.opsForValue().set(key, refreshToken);
//        setCookie(response, refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

//    private void setCookie(HttpServletResponse response, String refreshToken) {
//        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", refreshToken)
//                                                      .sameSite("Lax")
//                                                      .httpOnly(false)
//                                                      .secure(false)
//                                                      .domain("localhost")
//                                                      .path("/")
//                                                      .maxAge(refreshTokenLifetime / 1000)
//                                                      .build();
////        Cookie cookie = new Cookie("refresh-token", refreshToken);
////        cookie.setHttpOnly(true);
////        cookie.setMaxAge(refreshTokenLifetime/1000);
//////        cookie.setDomain("localhost");
//////        cookie.setPath("/");
////        response.addCookie(responseCookie);
//        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
//    }

    public JwtResponse refreshJwtTokens(@NonNull String refreshToken) {
        String key = usernamePrefix + jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
        if (!redisTemplate.hasKey(key)) {
            throw new ResourceNotFoundException("Вы не найдены в системе. Пожалуйста, перезайдите");
        }
        String savedRefreshToken = (String) redisTemplate.opsForValue().get(key);
        if (savedRefreshToken != null && !savedRefreshToken.equals(refreshToken)) {
            throw new IllegalStateException("Неверный токен обновления. Пожалуйста перезайдите");
        }
        UserProfileDto userProfileDto = new UserProfileDto(jwtTokenUtil.getUsernameFromRefreshToken(refreshToken), jwtTokenUtil.getEmailFromRefreshToken(refreshToken), jwtTokenUtil.getRolesFromRefreshToken(refreshToken));
        String newAccessToken = jwtTokenUtil.generateAccessToken(userProfileDto);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userProfileDto);
        redisTemplate.opsForValue().set(key, newRefreshToken);
        return new JwtResponse(newAccessToken, newRefreshToken);
    }

//    public JwtResponse refreshJwtTokens(@NonNull String refreshToken, HttpServletResponse response) {
//        String key = usernamePrefix + jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
//        if (!redisTemplate.hasKey(key)) {
//            throw new ResourceNotFoundException("Вы не найдены в системе. Пожалуйста, перезайдите");
//        }
//        String savedRefreshToken = (String) redisTemplate.opsForValue().get(key);
//        System.out.println(savedRefreshToken);
//        if (savedRefreshToken != null && !savedRefreshToken.equals(refreshToken)) {
//            throw new IllegalStateException("Неверный токен обновления. Пожалуйста перезайдите");
//        }
//        UserProfileDto userProfileDto = new UserProfileDto(jwtTokenUtil.getUsernameFromRefreshToken(refreshToken), jwtTokenUtil.getEmailFromRefreshToken(refreshToken), jwtTokenUtil.getRolesFromRefreshToken(refreshToken));
//        String newAccessToken = jwtTokenUtil.generateAccessToken(userProfileDto);
//        String newRefreshToken = jwtTokenUtil.generateRefreshToken(userProfileDto);
//        redisTemplate.opsForValue().set(key, newRefreshToken);
////        setCookie(response, newRefreshToken);
//        System.out.println("new refresh token: " + newRefreshToken);
//        return new JwtResponse(newAccessToken, refreshToken);
//    }

    public User authenticateUser(@NonNull String username, @NonNull String password) {
        User user = userService.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден", username)));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Неверный пароль");
        return user;
    }

}
