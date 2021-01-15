package com.imooc.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(400, "成功"),
    PARAM_ERROR(403, "参数不正确"),
    PRODUCT_NOT_EXIST(404, "商品不存在"),
    PRODUCT_STOCK_ERROR(405, "库存不足"),
    ORDER_NOT_EXIST(406, "订单不存在"),
    ORDERDETAIL_NOT_EXIST(407, "订单详情不存在"),
    ORDER_STATUS_ERROR(408,"订单状态异常"),
    ORDER_UPDATE_FAIL(409, "订单更新失败"),
    ORDER_DETAIL_EMPTY(410, "订单为空"),
    ORDER_PAY_STATUS_ERROR(411, "订单支付状态异常"),
    CART_EMPTY(412, "购物车为空"),
    ORDER_OWNER_ERROR(413, "订单不属于当前用户"),
    WECHAT_MP_ERROR(414, "微信公众号错误"),
    WXPAY_NOTIFY_PRICE_VERIFY_ERROR(415, "异步支付金额校验失败"),
    ORDER_CANCELLED(416,"订单取消成功"),
    ORDER_FINISHED(417, "订单完结成功"),
    PRODUCT_STATUS_ERROR(418, "商品状态不正确")
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
