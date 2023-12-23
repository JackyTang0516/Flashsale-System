package com.jiuzhang.seckill.service;

import com.alibaba.fastjson.JSON;
import com.jiuzhang.seckill.db.dao.OrderDao;
import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.dao.SeckillCommodityDao;
import com.jiuzhang.seckill.db.po.Order;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.db.po.SeckillCommodity;
import com.jiuzhang.seckill.mq.RocketMQService;
import com.jiuzhang.seckill.util.RedisService;
import com.jiuzhang.seckill.util.SnowFlake;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SeckillActivityService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RocketMQService rocketMQService;

    @Autowired
    SeckillCommodityDao seckillCommodityDao;

    @Autowired
    OrderDao orderDao;

    /**
     * datacenterId; data center
     * machineId; machine identification
     * In distributed environments can be read from the machine configuration
     * Write as a deterministic in a stand-alone development environment
     */
    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * Create order.
     *
     * @param id Activity ID
     * @param userId User ID
     * @return Order detail
     * @throws Exception MQ exception
     */
    public Order createOrder(long id, long userId) throws Exception {

        // 1. query & get activity
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(id);

        // 2. create & set new order information
        Order order = new Order();

        // use snowflake algorithm to generate order ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());

        // 3. send "create order" message to Rocket MQ
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));

        // 4. send "validate pay status" message to Rocket MQ
        // Rocket MQ support 18 levels, messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        rocketMQService.sendDelayMessage("pay_check", JSON.toJSONString(order), 4);

        return order;
    }

    /**
     * Determine if an item is still in stock
     *
     * @param activityId product ID
     * @return
     */
    public boolean seckillStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }


    /**
     * Add information about the spike details to redis
     *
     * @param seckillActivityId
     */
    public void pushSeckillInfoToRedis(long seckillActivityId) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        redisService.setValue("seckillActivity:" + seckillActivityId, JSON.toJSONString(seckillActivity));

        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());
        redisService.setValue("seckillCommodity:" + seckillActivity.getCommodityId(), JSON.toJSONString(seckillCommodity));
    }

    /**
     * Order Payment Processing Completed
     *
     * @param orderNo
     */
    public void payOrderProcess(String orderNo) throws Exception {
        log.info("Completed Payment Order, Order Number：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        /*
         * 1. To determine whether the order exists
         * 2. determine whether the order status is unpaid
         */
        if (order == null) {
            log.error("Order number corresponding to the order does not exist：" + orderNo);
            return;
        } else if(order.getOrderStatus() != 1 ) {
            log.error("Invalid Order Status：" + orderNo);
            return;
        }
        /*
         * 2.Order Payment Completed
         */
        order.setPayTime(new Date());
        //Order Status
        // 0:No available stock, invalid order
        // 1:Created and waiting for payment
        // 2:Payment is complete.
        order.setOrderStatus(2);
        orderDao.updateOrder(order);
        /*
         * 3.Send order payment success message
         */
        rocketMQService.sendMessage("pay_done", JSON.toJSONString(order));
    }
}
