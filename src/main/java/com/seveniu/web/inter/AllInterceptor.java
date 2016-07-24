package com.seveniu.web.inter;

import com.seveniu.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Remark:
 * <p/>
 * Author: Tim (Xuan Ma)
 * Date: 12/11/13 10:39
 */
@Component
public class AllInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (SessionUtil.isAdmin(request.getSession())) {
            return true;
        }

        if ((request.getRequestURI().equals("/login")
                || request.getRequestURI().startsWith("/tags/dic")
                || request.getRequestURI().startsWith("/api")
        ))
            return true;

        if (!SessionUtil.isValid(request.getSession())) {
//            response.sendRedirect("/login");
//            return false;
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception e) throws Exception {
    }
}