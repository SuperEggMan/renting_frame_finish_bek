package org.egg.controller;

import org.apache.ibatis.io.ResolverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chendatao on 2017/11/3.
 * @author chendatao
 */
@Controller
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Value("${test}")
    private String TEST;
    @RequestMapping("/")
    @ResponseBody
    String home() {
        System.out.println(TEST);
        return "Hello World!";
    }

}
