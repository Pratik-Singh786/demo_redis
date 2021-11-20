package com.exampleredis.demoredis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    //Connection factory->Lettuce  connection
    //redis template
    // if you wont give any parameter inside the method
    //it will run on localhost,6379

    // it means  a bean(object) of lettuce connection factory and redis template has been created so whenever
    //it is required we can autowire it.
    @Bean
    LettuceConnectionFactory getFactory()
    {
        //its for connecting to the localhost server
       LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost",6377));
        return connectionFactory;

       //now for connecting to the cloud
        /*RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration
                ("redis-14746.c89.us-east-1-3.ec2.cloud.redislabs.com", 14746);

        redisStandaloneConfiguration.setPassword("yIUtYRwio5xl21LOoVmZk3lLckjlJsfs");*/

       // LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);


    }

    //In my sql there is a converter which converts Integer ->int, String-> varchar, short->TINYINT
    //but in redis we need to convert these things ,so we need serializers
    @Bean
    RedisTemplate<String,Object> getTemplate() //not putting the second parameter as String because list and hashes
    {
        //RedisTemplate<String,Object>  redisTemplate=new RedisTemplate<>();
       // redisTemplate.afterPropertiesSet();
        //instead of these five lines above line is enough
       RedisTemplate<String,Object>  redisTemplate=new RedisTemplate<>();
       RedisSerializer<String> stringRedisSerializer=new StringRedisSerializer();//serializing a string where redis goes and vice-versa
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        JdkSerializationRedisSerializer jdkSerializationRedisSerializer=new JdkSerializationRedisSerializer();//converts java obj into redis and vice versa
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setConnectionFactory(getFactory());


        return redisTemplate;



    }


}
