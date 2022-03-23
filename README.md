springboot+redis实现缓存

1、引入redis依赖。
<dependency>
    <groupid>org.springframework.boot</groupid>
    <artifactid>spring-boot-starter-data-redis</aritifactid>
<dependency>
2、配置redis，通过在配置文件中配置以下信息，springboot将会自动创建redisConnectionFactory,并自动注入到IOC容器中。
spring:
    redis:
        host: locahost
        port: 6379
        password:
        connect-timeout: 3000  #连接超时时间（毫秒）
        jedis:
            pool:
                max-active: 8  #连接池最大连接数
                max-idle: 8  #连接池中最大空闲连接数
                min-idle: 0   #连接池中最小空闲连接数
                max-wait: -1 #连接池最大阻塞等待时间（适用负值代表没有限制）
3、创建redis配置类，生成CacheManager。
@Configuration
public class RedisConfig extends CachingConfigurerSuppoert{
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager cacheManager = new RedisCacheManager(
            RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
            this.redisCacheConfigurationWithTTL(300),
            this.configMap
        );
        return cacheManager;
    }

    //配置RedisCacheConfiguration
    private RedisCacheConfiguration redisCacheConfigurationWithTTL(Integer seconds){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.
            defaultCacheConfigure();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        valueSerializer.setObjectMapper(objectMapper);
        redisCacheConfiguration = redisCacheConfiguration.serializeValueWith(
            RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));
        return redisCacheConfiguration;
    }
    
    //配置自定义缓存的RedisCacheConfiguration
    private Map<String,RedisCacheConfiguration> configMap(){
        Map<String,RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("indexcodeCache",this.redisCacheCOnfigurationWithTTL(30*60);
        configMap.put("yearMap",this.redisCacheConfigurationWithTTL(24*60*60);
        return configMap;
    }
}
    
4、使用spring框架提供的注解进行缓存管理。
· 在配置类或者启动类上使用@EnableCaching开启缓存。
· 在需要缓存的方法上使用@Cacheable(value="indexcodeCache",key="#id")，
表示该方法的返回值可以被缓存，缓存的名称是indexcodeCache,键是参数id的值。
如果缓存中有数据，则直接从缓存中获取，否则，执行方法体，拿到返回值并放入
redis中一份。