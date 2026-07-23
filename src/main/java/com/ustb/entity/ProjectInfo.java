package com.ustb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_info")
public class ProjectInfo {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private Long ownerId;
    private Date startDate;
    private Date endDate;
    private Date createTime;
}