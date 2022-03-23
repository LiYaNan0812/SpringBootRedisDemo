package com.neusoft.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auth lyn
 * @Date 2022/3/22 14:25
 * version 1.0
 */

public class JedisTest {
    Jedis jedis ;

    @Before
    public void before(){
        jedis = new Jedis("http://192.168.9.23:6379");
    }

    @Test
    public void testBasicString(){
        jedis.set("baidu","https://www.baidu.com");
        System.out.println(jedis.get("baidu"));
        jedis.append("baidu","/index.html");
        System.out.println(jedis.get("baidu"));
        jedis.set("baidu","http://www.baidu.com");
        System.out.println(jedis.get("baidu"));
        jedis.del("baidu");
        System.out.println(jedis.get("baidu"));
        jedis.mset("onekey","1","twokey","2");
        System.out.println(jedis.mget("onekey","twokey"));
        System.out.println(jedis.keys("*"));
    }

    @Test
    public void testMap() throws InterruptedException {
        Map<String,String> user = new HashMap<>();
        user.put("name","liyanan");
        user.put("lover","wujingjing");
        jedis.hset("user2",user);
        System.out.println(jedis.hgetAll("user2"));
        jedis.hdel("user2","lover");
        System.out.println(jedis.hmget("user2","name","lover"));
        System.out.println(jedis.hkeys("user"));
        System.out.println(jedis.hvals("user"));
        jedis.expire("user2",2);
        Thread.sleep(1000);
        System.out.println(jedis.pttl("user2"));
        Thread.sleep(1000);
        System.out.println(jedis.exists("user2"));
    }

    @Test
    public void testList(){
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework",0,-1));
        jedis.lpush("java framework","spring","struts","hibernate");
        System.out.println(jedis.lrange("java framework",0,jedis.llen("java framework")));
        jedis.rpop("java framework");
        System.out.println(jedis.lindex("java framework",0));
        System.out.println(jedis.lindex("java framework",1));
        System.out.println(jedis.lindex("java framework",2));
    }

    @Test
    public void testSet(){
        jedis.sadd("loverset","wujingjing","liqiying","baixinyan");
        jedis.sadd("loverset","xiangjiahuan");
        jedis.srem("loverset","xiangjiahua");
        System.out.println(jedis.smembers("loverset"));
        System.out.println(jedis.sismember("loverset","xiangjiahuan"));
        System.out.println("让系统决定今晚和谁共度良宵"+jedis.srandmember("loverset",2));
        System.out.println("后宫有几人"+jedis.scard("loverset"));
    }

}
