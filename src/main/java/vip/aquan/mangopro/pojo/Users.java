package vip.aquan.mangopro.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: wcp
 * @date: 2020/9/1 01:36
 * @Description:
 */
@Document(collection = "user")
public class Users {
    private String userId;
    private String name;
    private Integer age;
    private double[] loc;

    public void setLoc(double[] loc) {
        this.loc = loc;
    }

    public double[] getLoc() {
        return loc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
