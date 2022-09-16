package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.service.InfoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MACHENIKE on 2018-12-03.
 */
@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    AtomicInteger count = new AtomicInteger(0);

    @Autowired
    InfoUserService userService;

    @GetMapping("ping")
    @ResponseBody
    public void ping(){
        userService.ping();
        logger.info("客户端已ping");
    }

    @PostMapping("add")
    @ResponseBody
    public void addUser(UserDTO userDTO){
        userService.insertInfoUser(new InfoUser(String.valueOf(userDTO.getId()),userDTO.getName(),userDTO.getAddress()));
        logger.info("注册成功");
    }

    @PostMapping("insert")
    @ResponseBody
    public void insertDefaultUsers() throws InterruptedException {
        long start = System.currentTimeMillis();
        int thread_count = 100;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                synchronized (this){
                    InfoUser infoUser = new InfoUser(String.valueOf(count.addAndGet(1)),"Jeen","BeiJing");
                    List<InfoUser> users = userService.insertInfoUser(infoUser);
                    logger.info("返回用户信息记录:{}", JSON.toJSONString(users));
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));
    }

    @GetMapping("getById")
    @ResponseBody
    public InfoUser getById(String id){
        logger.info("根据ID查询用户信息:{}",id);
        return userService.getInfoUserById(id);
    }

    @GetMapping("getNameById")
    @ResponseBody
    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        return userService.getNameById(id);
    }

    @GetMapping("getAllUser")
    @ResponseBody
    public Map<String,InfoUser> getAllUser() throws InterruptedException {

        long start = System.currentTimeMillis();
        int thread_count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                Map<String, InfoUser> allUser = userService.getAllUser();
                logger.info("查询所有用户信息：{}",JSONObject.toJSONString(allUser));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));

        return null;
    }
}
