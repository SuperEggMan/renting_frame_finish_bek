package org.egg.service;

import org.apache.commons.lang3.StringUtils;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.mapper.BuysMapper;
import org.egg.model.Buys;
import org.egg.response.CommonSingleResult;
import org.egg.template.ServiceTemplate;
import org.egg.template.TemplateCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dataochen
 * @Description
 * @ Date: 2017/11/6 21:39
 */
@Service
public class BuysService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuysService.class);
    @Autowired
    ServiceTemplate serviceTemplate;
    @Autowired
    private BuysMapper buysMapper;
    /**
    *
    * @param buysNo
    * @return org.egg.response.CommonSingleResult<org.egg.model.Buys>
    * @Date:16:12 2017/11/7
    */
    public CommonSingleResult<Buys> queryBuyByNo(String buysNo) {
        CommonSingleResult<Buys> buysCommonSingleResult = new CommonSingleResult<>();
        serviceTemplate.process(buysCommonSingleResult, new TemplateCallBack() {
            @Override
            public void doCheck() {
                if (StringUtils.isBlank(buysNo)) {
                    throw new CommonException(CommonErrorEnum.PARAM_NULL,"buysNo",buysNo);
                }
            }

            @Override
            public void doAction() {
                Buys buys = buysMapper.queryEntityByNo(buysNo);
                buysCommonSingleResult.setData(buys);
            }
        });
        return buysCommonSingleResult;
    }
}
