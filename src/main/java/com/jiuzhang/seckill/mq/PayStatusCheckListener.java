
package com.jiuzhang.seckill.mq;


import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.OrderDao;
import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.po.Order;
import com.jiuzhang.seckill.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "pay_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RedisService redisService;

    @Override
    @Transactional
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Receive order payment status verification message:" + message);
        Order order = JSON.parseObject(message, Order.class);
        //1.Check Orders
        Order orderInfo = orderDao.queryOrder(order.getOrderNo());
        //2.Reads whether an order has been paid for or not
        if (orderInfo.getOrderStatus() != 2) {
            //3.Close the order without completing the payment
            log.info("Order closure with incomplete payment, order number：" + orderInfo.getOrderNo());
            orderInfo.setOrderStatus(99);
            orderDao.updateOrder(orderInfo);
            //4.Restoration of database stock
            seckillActivityDao.revertStock(order.getSeckillActivityId());
            // Restore redis stock
            redisService.revertStock("stock:" + order.getSeckillActivityId());
            //5.Removing users from the purchased list
            redisService.removeLimitMember(order.getSeckillActivityId(), order.getUserId());
        }
    }
}

