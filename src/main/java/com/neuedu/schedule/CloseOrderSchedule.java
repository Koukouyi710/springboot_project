package com.neuedu.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseOrderSchedule {

    //
    @Scheduled(cron = "* * */1 * * *")
    public void closeOrder(){
        System.out.println("====cron===="+System.currentTimeMillis());
    }
}
