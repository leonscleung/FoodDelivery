package com.imooc.exception;

import com.imooc.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException{
    private Integer code;

    public ProductException(ResultEnum resultEnum) {
        //把Message传到父类的构造方法
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ProductException(Integer code, String message){
        super(message);
        this.code = code;
    }
}
