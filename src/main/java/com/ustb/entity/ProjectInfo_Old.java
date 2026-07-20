package com.ustb.entity;

import java.util.Date;

/*
实体类，输出传递的载体
通常情况，一个实体类对应数据库中的一个表，实体类中的属性与数据库字段一一对应
实体类三要素
1.公共的类
2.私有成员字段
3.无参构造函数
4.提供get/set访问器
 */
public class ProjectInfo_Old {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private Long ownerId;
    private Date startDate;
    private Date endDate;
    private Date createTime;
    /*
    无参构造函数（Alt+insert==》Constructor）
     */
    public ProjectInfo_Old() {
    }

//get/set访问器（Alt+insert==》getter and setter）
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}