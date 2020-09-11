package vip.aquan.mangopro.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 供应商
 */
@Document(collection = "supplier")
public class Supplier {
    /**
     * 分请求VO,响应Vo, 插入的vo，跟返回的vo分开
     */
    //商户号

    //供应商或商户名
    private String name;
    //请求vo
    private Integer categoryId;
    //根据categoryId查，还是Sting直接返回？
    private String categoryName;
    //距离 单位：米
    private String distance;
    //地址
    private String address;
    //营业时间  12：00 至 22：00
    private String businessTime;
    //目的地坐标
    private Location location;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
