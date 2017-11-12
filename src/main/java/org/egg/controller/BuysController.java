package org.egg.controller;

import org.egg.model.Buys;
import org.egg.response.CommonSingleResult;
import org.egg.service.BuysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dataochen
 * @Description
 * @ Date: 2017/11/6 21:37
 */
@Controller
@RequestMapping("/buys")
public class BuysController {

    @Autowired
    private BuysService buysService;


    @RequestMapping("/queryEntityByNo")
    @ResponseBody
    public CommonSingleResult<Buys>  queryEntityByNo(@RequestParam("buysNo") String buysNo) {
        // FIXME: 2017/11/7 buys为空 解决
        CommonSingleResult<Buys> buysCommonSingleResult = buysService.queryBuyByNo(buysNo);
        return buysCommonSingleResult;
    }
}
