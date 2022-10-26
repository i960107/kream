package com.example.demo.config;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, BaseException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

            if (auth != null) {
                //token 있는지 check getuserinfo에서 처리
                String token = jwtService.getJwt(request);
                Role role = auth.role();

                if (role.equals(Role.USER)) {
                    //role 일치하는지 check
                    Map<String, Object> userInfo = jwtService.getUserInfo(token);
                    if (!(userInfo.get("role").equals("USER")))
                        throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);

                    //idx 일치하는지  check
                    String tokenIdx =  userInfo.get("userIdx").toString();
                    Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    if (pathVariables.get("userIdx") != null) {
                        String pathVariableIdx = pathVariables.get("userIdx").toString();
                        if (!(pathVariableIdx.equals(tokenIdx)))
                            throw new BaseException(BaseResponseStatus.TOKEN_USER_NOT_MATH);
                    }
                    request.setAttribute("userIdx", tokenIdx);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView modelAndView) throws Exception {
    }

}
