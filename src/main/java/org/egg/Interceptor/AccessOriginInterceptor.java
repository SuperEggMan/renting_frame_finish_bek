package org.egg.Interceptor;

import org.egg.utils.PropertiesUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dataochen
 * @Description 跨域
 * @date: 2017/11/7 18:34
 */
public class AccessOriginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", PropertiesUtil.getProperty("front.url"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        if ("POST".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST");
        } else {
            httpServletResponse.setHeader("Access-Control-Allow-Methods","GET");

        }
        httpServletResponse.setHeader("Access-Control-Max-Age","60");
        //ajax 自定义header头key,value
        httpServletResponse.setHeader("Access-Control-Allow-Headers","renting");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
