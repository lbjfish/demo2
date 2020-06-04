package com.lee.content.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

import static java.util.Collections.singletonMap;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        // 配置连接工厂
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //  解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        //(必须配置这个，不配置这个反序列化会把po转成map，那就不好办了)
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer); // value的序列化类型
        // 设置hash key 和value序列化模式 (解决用hash的时候字节问题，
        // 因为RedisTemplate的时候会转成字节流，只能用StringRedisTemplate，现在加了这个就无须StringRedisTemplate了)
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                //这个配置是默认的配置，也就是说，对全部 cacheNames 生效
                .cacheDefaults(defaultCacheConfig())
                //下面两个配置是对特定的某个 cacheNames 生效（可以把下面的当做，是特殊对待的配置）
                .withInitialCacheConfigurations(singletonMap("predefined", defaultCacheConfig().disableCachingNullValues()))
                .withInitialCacheConfigurations(singletonMap("predefined2", defaultCacheConfig().disableCachingNullValues().entryTtl(Duration.ofSeconds(10))))
                .transactionAware()
                .build();

        return cacheManager;
    }

    private RedisCacheConfiguration defaultCacheConfig() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        //(必须配置这个，不配置这个反序列化会把po转成map，那就不好办了)
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //  TTL  Time To Live
        /*前缀
         * 过期时间
         * key序列化
         * value序列化
         * */
        RedisCacheConfiguration redisCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        //.prefixKeysWith("redis")
                        //失效时间
                        .entryTtl(Duration.ofMinutes(20))
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                        .disableCachingNullValues();

        return redisCacheConfiguration;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, params) -> {
            //本类名+方法名+参数名 为key
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName());
            sb.append("-");
            sb.append(method.getName());
            sb.append("-");
            for (Object param : params) {
                sb.append(param.toString());
            }
            return sb.toString();
        };
    }


//    @Bean
//    CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        //user信息缓存配置
//        RedisCacheConfiguration userCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)).disableCachingNullValues().prefixKeysWith("user");
//        //product信息缓存配置
//        RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)).disableCachingNullValues().prefixKeysWith("product");
//        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
//        redisCacheConfigurationMap.put("user", userCacheConfiguration);
//        redisCacheConfigurationMap.put("product", productCacheConfiguration);
//        //初始化一个RedisCacheWriter
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//
//
//        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(Object.class));
//        RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//
//
//        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
//        //设置默认超过期时间是30秒
//        defaultCacheConfig.entryTtl(Duration.ofMinutes(2));
//        //初始化RedisCacheManager
//        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
//        return cacheManager;
//    }
}