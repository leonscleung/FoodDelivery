package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {
    private static final long serialVersionUID = 3068837394742385883L;
    //错误码
    private Integer code;
    private String msg;
    private T data;
}
