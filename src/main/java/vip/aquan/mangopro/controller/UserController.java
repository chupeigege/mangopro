package vip.aquan.mangopro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.aquan.mangopro.pojo.ResultObject;
import vip.aquan.mangopro.pojo.Users;

import java.util.List;

/**
 * @author: wcp
 * @date: 2020/9/1 01:38
 * @Description:
 */
@RequestMapping("/mongo")
@RestController
public class UserController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }
    /**
     * mongo操作：
     *
     * db.places.insert(
     *    {
     *       location : { type: "Point", coordinates: [114.116812,22.544186] },
     *       name: "老街",
     *       category : "LJ"
     *    }
     * )
     *
     * db.places.insert(
     *    {
     *       location : { type: "Point", coordinates: [ 116.397128, 39.916527 ] },
     *       name: "BEIJING1",
     *       category : "BJ1"
     *    }
     * )
     *
     * db.getCollection('places').find({})
     *
     * db.places.createIndex( {location : "2dsphere"} )
     *
     * db.places.findOne()
     *
     * db.places.find({
     *   location:{
     *     $near: {
     *        $geometry: {
     *           type: "Point" ,
     *           coordinates: [114.116812,22.544186 ]
     *        },
     *        $maxDistance: 2000
     *     }
     *   }
     * })
     */

    /**
     * 新增
     */
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ResultObject insert(){
        Users user = new Users();
        user.setUserId("1002");
        user.setName("eric");
        user.setAge(18);
//        user.setLoc(new double[]{112.1214,114.1595}); 要改成point类型
        mongoTemplate.insert(user);
        return new ResultObject(200);
    }

    /**
     * 更新
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultObject update(){
        Query query = Query.query(Criteria.where("userId").is("1002"));
        Update update = new Update();
        update.set("name","name1");
        update.set("age",18);
        mongoTemplate.updateFirst(query,update,"user");
        return new ResultObject(200);
    }

    /**
     * 查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultObject query(){
        Query query = Query.query(Criteria.where("userId").is("1002"));
        List<Users> list = mongoTemplate.find(query,Users.class);
        return new ResultObject(200,list);
    }

    /**删除
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultObject delete(){
        Query query = Query.query(Criteria.where("userId").is("1002"));
        mongoTemplate.remove(query,"user");
        return new ResultObject(200);
    }



}
