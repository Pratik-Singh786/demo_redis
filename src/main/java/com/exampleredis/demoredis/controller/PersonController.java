package com.exampleredis.demoredis.controller;

import com.exampleredis.demoredis.model.User;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
public class PersonController
{
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    //it will pick from config file;
//----------------------------------------X--------------------------------------------------
    @GetMapping("/getValue")
    public String getValue(@RequestParam("String key")String key)
    {
        return (String)redisTemplate.opsForValue().get(key);
    }

    @PostMapping("/setValue")
    public void setValue(@RequestParam("key")String key,
                         @RequestParam("value")String value,
                         @RequestParam(value="expiry",required = false)Integer expiry)
    {
        if(expiry==null) {
            redisTemplate.opsForValue().set(key, value);
        }
        else
        {
            redisTemplate.opsForValue().set(key,value, Duration.ofSeconds(expiry));
        }


    }
    //--------------------------------------------x-----------------------------------------------------
    // 1. retrieve
    //2.lpush
    //3.rpush
    //4lpop
    //5.rpop
    @GetMapping("/getListElements")
    public List<User> getUserByList(@RequestParam("key")String key,
                                    @RequestParam(value="start",required=false,defaultValue="0")long start,
                                    @RequestParam(value="end",required=false,defaultValue="-1")long end)
    {
        return redisTemplate.opsForList().
                range(key, start, end).
                stream().
                map(x -> (User) x).
                collect(Collectors.toList());

    }
    @PostMapping("/lPush")
    public Long lPush(@RequestParam("key")String key,
                      @RequestBody User user)
    {
        return redisTemplate.opsForList().leftPush(key,user);
    }
    @PostMapping("/rpush")
    public Long rpush(@RequestParam("key")String key,
                      @RequestBody User user)
    {
        return redisTemplate.opsForList().rightPush(key,user);
    }

    @GetMapping("/lpop")
    public User lpop(@RequestParam("key")String key)
                     //@RequestParam("count")int count)
    {
       return (User) redisTemplate.opsForList().leftPop(key);

    }
    @GetMapping("/rpop")
    public User rpop(@RequestParam("key")String key)

    {
        return (User) redisTemplate.opsForList().rightPop(key);

    }

//--------------------------------X------------------------------------------
    @GetMapping("/hgetall")
    public Map<Object,Object> hgetall(@RequestParam("key")String key) //Map<key,value>
    {
        Map<Object,Object> mapToReturn=new HashMap<>();

        List<Object> fields= Arrays.asList("id","name","country","age");
        List<Object> values=redisTemplate.opsForHash().multiGet(key,fields);
        for(int i=0;i<fields.size();i++)
        {
            mapToReturn.put(fields.get(i),values.get(i));
        }
      return mapToReturn;
    }

    @PostMapping("/hmset")
    public  void hmset(@RequestParam("key")String key,@RequestBody User user)
    {
        Map<String,Object> fieldMap=new HashMap<>();
        fieldMap.put("id",user.getId());
        fieldMap.put("name",user.getName());
        fieldMap.put("country",user.getCountry());
        fieldMap.put("age",user.getAge());
        redisTemplate.opsForHash().putAll(key,fieldMap);
    }







}
