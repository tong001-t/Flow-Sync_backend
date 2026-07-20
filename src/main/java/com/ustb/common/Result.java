package com.ustb.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回相应结果
 */
@Data
@AllArgsConstructor
public class Result {
    //状态码(100--成功，101--失败）
    private int code;
    //系统消息
    private String message;
    //响应数据
    private Object data;

    //构造函数
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, Object data) {
        this.code = code;
        this.data = data;
    }

}
