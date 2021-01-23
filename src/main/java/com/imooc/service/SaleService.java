package com.imooc.service;

/**
 * Created by 廖师兄
 * 2017-08-06 23:18
 */
public interface SaleService {

    String querySaleProductInfo(String productId);
    void orderProductMockConcurrent(String productId);

}