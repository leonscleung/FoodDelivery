package com.imooc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
//1. 配置Maven依赖 2. 配置Mapper启动类 3. 使用SQL注解（推荐)/xml接口 4. 配置xml文件位置
@MapperScan(basePackages = "com.imooc.dataobject.mapper")
public class Product2Application {

    public static void main(String[] args) {
        SpringApplication.run(Product2Application.class, args);
    }

}
