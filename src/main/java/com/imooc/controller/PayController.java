package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.ProductException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;

import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) throws UnsupportedEncodingException {
        // 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            throw new ProductException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 发起支付
        PayResponse payResponse = payService.create(orderDTO);
        map.put("payResponse", payResponse);
        try {
            String decode = URLDecoder.decode(returnUrl, "UTF-8");
            map.put("returnUrl", decode);
        } catch (UnsupportedEncodingException e){
            log.error("[支付订单] 解析返回地址错误, returnUrl={}", returnUrl);
            e.printStackTrace();
        }
        return new ModelAndView("pay/create", map);
    }

    //异步通知
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回微信处理结果
        return new ModelAndView("pay/success");

    }
}
