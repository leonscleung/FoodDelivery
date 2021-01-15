package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> {
    //错误码
    private Integer code;
    private String msg;
    private T data;
}
