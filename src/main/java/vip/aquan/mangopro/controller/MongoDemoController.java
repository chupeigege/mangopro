package vip.aquan.mangopro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.aquan.mangopro.pojo.*;
import vip.aquan.mangopro.service.MongoDemoService;
import vip.aquan.mangopro.test.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 需要创建索引db.collection.createIndex( {location : "2dsphere"} )
 */
@RequestMapping("/mongo")
@RestController
public class MongoDemoController {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoDemoService mongoDemoService;

    /**
     * 商户通过关键字、分类(多选）、范围--查询供应商列表(只有范围数据，没有返回距离)
     */
    @ResponseBody
    @RequestMapping(value = "/querySup", method = RequestMethod.GET)
    public ResultObject querySup(QueryVo queryVo){
        Query query = getQuery(queryVo);
        List<Supplier> supplierList = mongoTemplate.find(query, Supplier.class);
        return new ResultObject(200,supplierList);
    }

    /**
     * geo
     * 通过关键字、分类(多选）、范围--查询定位信息的供应商列表，并返回距离
     */
    @ResponseBody
    @RequestMapping(value = "/querySup2", method = RequestMethod.GET)
    public ResultObject querySup2(QueryVo queryVo){
        Map map = mongoDemoService.querySup2(queryVo);

        return new ResultObject(200,map);
    }


    /**
     * 构建查询条件
     * @param queryVo
     * @return
     */
    private Query getQuery(QueryVo queryVo) {
        Query query = new Query();

        //筛选xx米范围内数据
        if (queryVo.getLongitude() != null && queryVo.getLatitude()!=null) {
            //创建坐标点
            GeoJsonPoint point = new GeoJsonPoint(queryVo.getLongitude(), queryVo.getLatitude());

            Criteria criteria = Criteria.where("location").nearSphere(point);
            if(queryVo.getMaxDistance()!=null){
                criteria.maxDistance(queryVo.getMaxDistance());
            }
            query.addCriteria(criteria);
        }

        //按类别筛选（可多选）
        if (queryVo.getCategoryIds() != null && queryVo.getCategoryIds().length>0) {
            Criteria criteria = Criteria.where("categoryId").in(Arrays.asList(queryVo.getCategoryIds()));
            query.addCriteria(criteria);
        }

        //按名字模糊查询
        if(queryVo.getName()!=null && !"".equals(queryVo.getName())){
            Pattern pattern = Pattern.compile("^.*"+queryVo.getName()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = Criteria.where("name").regex(pattern);
            query.addCriteria(criteria);
        }

        return query.skip((queryVo.getPageNo() - 1) * queryVo.getPageSize()).limit(queryVo.getPageSize());
    }

    /**
     * 新增
     */
    @ResponseBody
    @RequestMapping(value = "/insert")
    public String insert(){
        //商户名、经纬度、地址。。。
        //供应商名、经纬度、地址、经营品类、营业时间  12：00 至 22：00。。。
        MongoSup mongoSup = new MongoSup();
        mongoSup.setName("轿场尾海滩");
        mongoSup.setCategoryId("2");


        //坐标：经纬度
        Location location = new Location();
        location.setType("Point");
        location.setCoordinates(new double[]{114.513041,22.591384});
        mongoSup.setLocation(location);
        mongoTemplate.insert(mongoSup);
        return "200";
    }

}
