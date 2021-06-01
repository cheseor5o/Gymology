package model;

/**
 * A POJO represents course
 * @since  2021/5/9 13:40
 */
public class Course {
    private String id;
    private String name;
    private String sort;
    private String time;
    private Boolean vip;
    private String picture;
    private String location;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sort='" + sort + '\'' +
                ", time='" + time + '\'' +
                ", vip=" + vip +
                ", picture='" + picture + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
