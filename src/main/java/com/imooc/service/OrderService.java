package com.imooc.service;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDTO create(OrderDTO orderDTO);
    OrderDTO findOne(String orderId);
    //个人所有订单
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
    //所有人的订单
    Page<OrderDTO> findList(Pageable pageable);
    OrderDTO cancel(OrderDTO orderDTO);
    OrderDTO finish(OrderDTO orderDTO);
    OrderDTO paid(OrderDTO orderDTO);
}
