package vip.aquan.mangopro.pojo;

public class Location {

    //坐标类型：固定Point
    private String type;
    //坐标[经度，纬度]
    private double[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
