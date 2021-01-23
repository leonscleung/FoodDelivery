package com.imooc.service.impl;

import com.imooc.exception.ProductException;
import com.imooc.service.RedisLock;
import com.imooc.service.SaleService;
import com.imooc.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaleServiceImpl implements SaleService {

    private static final int TIMEOUT = 10 * 1000; //超时时间10秒

    @Autowired
    private RedisLock redisLock;

    static Map<String,Integer> products;
    static Map<String,Integer> stock;
    static Map<String,String> orders;
    static
    {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("1236", 100000);
        stock.put("1236", 100000);
    }

    private String queryMap(String productId)
    {
        return "春节活动，芒果冰特价，限量份"
                + products.get(productId)
                +" 还剩：" + stock.get(productId)+" 份"
                +" 该商品成功下单用户数目："
                +  orders.size() +" 人" ;
    }

    @Override
    public String querySaleProductInfo(String productId)
    {
        return this.queryMap(productId);
    }

    //synchronized无法做到细粒度控制,只适合单点情况
    @Override
    public void orderProductMockConcurrent(String productId)
    {

        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLock.lock(productId, String.valueOf(time))) {
            throw new ProductException(422, "没抢到，过一会再来吧~");
        }

        //1.查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if(stockNum == 0) {
            throw new ProductException(421,"活动结束");
        }else {
            //2.下单(模拟不同用户openid不同)
            orders.put(KeyUtil.genUniqueKey(),productId);
            //3.减库存
            stockNum = stockNum - 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId,stockNum);
        }
        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}
