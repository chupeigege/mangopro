package vip.aquan.mangopro.service;

import vip.aquan.mangopro.pojo.QueryVo;

import java.util.Map;

public interface MongoDemoService {
    /**
     * 通过关键字、分类(多选）、范围--查询定位信息的供应商列表，并返回距离
     * @param queryVo
     * @return
     */
    Map querySup2(QueryVo queryVo);
}
