package org.egg.configurer;

import org.egg.Interceptor.AccessOriginInterceptor;
import org.egg.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author dataochen
 * @Description springboot 配置
 * @date: 2017/11/7 16:29
 */
@Configuration
public class MyWebAppConfigurer
        extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new AccessOriginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}