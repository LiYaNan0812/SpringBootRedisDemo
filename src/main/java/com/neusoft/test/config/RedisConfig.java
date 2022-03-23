package com.neusoft.test.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import redis.clients.jedis.ConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auth lyn
 * @Date 2022/3/23 8:30
 * version 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
/*
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.connect-timeout}")
    private int connectionTimeout;

    @Bean
    public JedisConnectionFactory myconnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(host);
        connectionFactory.setPort(port);
        connectionFactory.setTimeout(connectionTimeout);
        connectionFactory.getPoolConfig().setMinIdle(minIdle);
        connectionFactory.getPoolConfig().setMaxIdle(maxIdle);
        connectionFactory.getPoolConfig().setMaxWaitMillis(maxWait);
        connectionFactory.getPoolConfig().setMaxTotal(maxActive);
        return  connectionFactory;
    }

    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory myconnectionFactory){
        StringRedisTemplate template = new StringRedisTemplate(myconnectionFactory);
        this.setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    private void setSerializer(StringRedisTemplate template){
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);
        template.setValueSerializer(serializer);
    }
    */
//===========================================下面是新版创建cacheManager的方式=======================================//
    /*
        使用spring boot根据配置文件自动创建的bean redisConnectionFactory,创建
        CacheManager，供spring缓存使用
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheManager cacheManager = new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTTL(300),//默认配置
                this.initialCacheConfiguration()//自定义指定缓存的配置
                );
        return cacheManager;
    }

    private Map<String, RedisCacheConfiguration> initialCacheConfiguration(){
        Map<String,RedisCacheConfiguration> cacheConfigMap = new HashMap<>();
        cacheConfigMap.put("indexCodeCache",this.getRedisCacheConfigurationWithTTL(60));
        cacheConfigMap.put("areaCodeCache",this.getRedisCacheConfigurationWithTTL(24*60*60));
        return cacheConfigMap;
    }

    /*
        传递过期时间，创建redis缓存配置类。主要配置序列化器和过期时间
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTTL(Integer seconds){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }


}
