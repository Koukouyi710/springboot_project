package com.neuedu.schedule;

import com.neuedu.service.IOrderService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CloseOrderSchedule {

    @Value("${order.close.timeout}")
    private int orderTimeOut;

    @Autowired
    IOrderService orderService;

    //
    //@Scheduled(cron = "0 0 */1 * * *")
    @Scheduled(cron = "* */5 * * * *")
    public void closeOrder(){

        //step1:
        Date closeOrderTime = DateUtils.addHours(new Date(),-orderTimeOut);

        orderService.closeOrder(com.neuedu.utils.DateUtils.dateToString(closeOrderTime));

        System.out.println("====cron===="+System.currentTimeMillis());
    }
}
