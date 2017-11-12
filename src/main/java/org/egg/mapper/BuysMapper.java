package org.egg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.egg.model.Buys;

/**
 * 求购信息mapper
 * @author dataochen
 * @date 2017/11/7
 */
@Mapper
public interface BuysMapper {
    /**
     * insert
     * @return int
     * @param record
     * @Date:13:58 2017/11/7
     */
    int insert(Buys record);

    /**
     * insertSelective
     * @return int
     * @param record
     * @Date:13:58 2017/11/7
     */
    int insertSelective(Buys record);

    /**
     * 根据busyno查询唯一实体
     * @return int
     * @param buysNO
     * @Date:13:58 2017/11/7
     */
    Buys queryEntityByNo(String buysNO);
}