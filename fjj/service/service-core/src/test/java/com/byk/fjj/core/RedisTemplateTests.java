package com.byk.fjj.core;

import com.byk.fjj.core.mapper.DictMapper;
import com.byk.fjj.core.pojo.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author byk
 * @date 2022/11/20 17:03
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DictMapper dictMapper;

    //存值
    @Test
    public void saveDict(){
        Dict dict = dictMapper.selectById(1);
        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.MINUTES);
    }

    //取值
    @Test
    public void getDict(){
        Dict dict = (Dict)redisTemplate.opsForValue().get("dict");
        System.out.println(dict);
    }
}
