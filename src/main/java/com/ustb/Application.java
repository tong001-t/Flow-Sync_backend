package com.ustb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//启动类，SprignBoot应用程序从启动类开始启动并运行
//加载需要初始化的内容
@SpringBootApplication
//配置Mapper接口的扫描位置
@MapperScan("com.ustb.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
