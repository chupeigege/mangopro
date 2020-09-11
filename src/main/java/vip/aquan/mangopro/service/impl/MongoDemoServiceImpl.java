package vip.aquan.mangopro.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.CustomMetric;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vip.aquan.mangopro.pojo.QueryVo;
import vip.aquan.mangopro.pojo.Supplier;
import vip.aquan.mangopro.service.MongoDemoService;

import javax.xml.ws.ServiceMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class MongoDemoServiceImpl implements MongoDemoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 自定义距离单位：米
     */
    private final CustomMetric customMetric = new CustomMetric(6378000.137D, "米");

    @Override
    public Map querySup2(QueryVo queryVo) {
        Map map = new HashMap<>(4);
        Query query = new Query();

        ///按类别筛选（可多选）
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

        long pageStart = (queryVo.getPageNo() - 1) * queryVo.getPageSize();

        if (queryVo.getMaxDistance() == null) {
            //默认范围 50公里
            queryVo.setMaxDistance(50000D);
        }
        NearQuery nearQuery = NearQuery.near(queryVo.getLongitude(), queryVo.getLatitude(), customMetric).maxDistance(queryVo.getMaxDistance()).query(query);

        //分页查询
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.geoNear(nearQuery, "distance"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "distance")),
                Aggregation.skip(pageStart),
                Aggregation.limit(queryVo.getPageSize())
        );
        //总记录数
        Aggregation aggTotal = Aggregation.newAggregation(
                Aggregation.geoNear(nearQuery, "distance"),
                Aggregation.count().as("count")
        );
        

//        Aggregation distanceAgg = Aggregation.newAggregation(
//                Aggregation.geoNear(NearQuery.near(114.116812, 22.544186, customMetric).query(query).maxDistance(50000), "distance"),
//                Aggregation.skip(pageStart),
//                Aggregation.limit(1)
//
//        );
//
//        Aggregation countAgg = Aggregation.newAggregation(
//                Aggregation.geoNear(NearQuery.near(114.116812, 22.544186, customMetric).query(query).maxDistance(50000), "distance"),
//                Aggregation.count().as("count")
//
//        );

        List<Supplier> supplierList = mongoTemplate.aggregate(agg, "supplier", Supplier.class).getMappedResults();
        Map<String, Integer> totalMap = mongoTemplate.aggregate(aggTotal, "supplier", Map.class).getUniqueMappedResult();
        Integer totalCount = totalMap == null ? 0 : totalMap.get("count");
//        int totalCount = mongoTemplate.aggregate(aggTotal, "supplierPosition", SupplierPositionResp.class).getMappedResults().size();

        map.put("list", supplierList);
        map.put("total", totalCount);

        return map;
    }
}
