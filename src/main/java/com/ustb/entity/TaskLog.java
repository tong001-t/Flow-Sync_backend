package com.ustb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("task_log")
public class TaskLog {
    private Long id;
    private Long taskId;
    private Integer progressPercent;
    private String content;
    private Long operatorId;
    private Date createTime;
}
