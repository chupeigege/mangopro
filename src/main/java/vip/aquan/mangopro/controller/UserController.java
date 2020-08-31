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
     * 新增
     */
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public ResultObject insert(){
        Users user = new Users();
        user.setUserId("1002");
        user.setName("eric");
        user.setAge(18);
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
