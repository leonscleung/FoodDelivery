package com.imooc.controller;
import com.imooc.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale")
@Slf4j
public class SaleController {
    @Autowired
    private SaleService saleService;

    //返回抢购人数/库存
    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId)throws Exception
    {
        return saleService.querySaleProductInfo(productId);
    }

    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId)throws Exception
    {
        log.info("@skill request, productId:" + productId);
        saleService.orderProductMockConcurrent(productId);
        return saleService.querySaleProductInfo(productId);
    }
}
