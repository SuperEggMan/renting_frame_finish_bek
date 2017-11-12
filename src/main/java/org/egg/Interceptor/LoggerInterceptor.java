package org.egg.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dataochen
 * @Description 日志拦截
 * @date: 2017/11/7 16:29
 */
@Deprecated
public class LoggerInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptor.class);
    /**
     * [大小写不敏感]日志拦截字段名黑名单 不打印
     */
    private final String[] blackArray={"password"};
    /**
     *##[大小写不敏感]日志拦截卡号级别字段 前6后四
     */
    private final String[] cardNoArray={"cardNo","cardNum"};
    /**
     *[大小写不敏感]日志拦截手机号级别字段 前三后4
     */
    private final String[] phoneNumArray={"phonenum","phoneno","tel"};

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
