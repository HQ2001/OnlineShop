package com.hlju.onlineshop.order.interceptor;

import com.hlju.common.constant.AuthConstant;
import com.hlju.common.vo.UserResponseVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/25 21:30
 */
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserResponseVO> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserResponseVO login = (UserResponseVO) session.getAttribute(AuthConstant.LOGIN_USER);
        if (Objects.isNull(login)) {
            // 用户未登录
            request.getSession().setAttribute("errorMessage", "请先登录");
            response.sendRedirect("http://auth.onlineshop.hlju.com/login.html");
            return false;
        }

        // 用户已登陆，保存用户的登录信息
        loginUser.set(login);
        return true;
    }

}
