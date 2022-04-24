package com.hlju.onlineshop.cart.interceptor;

import com.hlju.common.constant.AuthConstant;
import com.hlju.common.constant.CartConstant;
import com.hlju.common.vo.UserResponseVO;
import com.hlju.onlineshop.cart.dto.UserInfoDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

/**
 * 判断用户登录状态，进行封装，传递给目标请求
 *
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/23 20:58
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoDTO> threadLocal = new ThreadLocal<>();

    /**
     * 目标方法执行前
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        HttpSession session = request.getSession();
        UserResponseVO loginUser = (UserResponseVO) session.getAttribute(AuthConstant.LOGIN_USER);
        if (Objects.nonNull(loginUser)) {
            // 用户已登录
            userInfoDTO.setUserId(loginUser.getId());
        }

        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (Objects.equals(name, CartConstant.TEMP_USER_COOKIE_NAME)) {
                    userInfoDTO.setUserKey(cookie.getValue());
                    userInfoDTO.setIsTempUser(false);
                }
            }
        }

        // 当前用户未登录，也没有user-key，给一个临时用户的user-key
        if (StringUtils.isEmpty(userInfoDTO.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoDTO.setUserKey(uuid);
        }

        // 目标方法执行之前
        threadLocal.set(userInfoDTO);

        return true;
    }

    /**
     * 业务执行之后，让浏览器保存临时用户信息
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoDTO userInfoDTO = threadLocal.get();
        if (userInfoDTO.getIsTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoDTO.getUserKey());
            cookie.setDomain("onlineshop.hlju.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
