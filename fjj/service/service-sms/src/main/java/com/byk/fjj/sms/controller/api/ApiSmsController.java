package com.byk.fjj.sms.controller.api;

import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.common.result.Result;
import com.byk.common.util.RegexValidateUtils;
import com.byk.fjj.sms.client.CoreUserInfoClient;
import com.byk.fjj.sms.service.SmsService;
import com.byk.common.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author byk
 */
@Api(tags = "短信管理")
@Slf4j
//@CrossOrigin //跨域
@RestController
@RequestMapping("/api/sms")
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    //发送手机验证码
    @ApiOperation("发送手机验证码")
    @PostMapping("/send/{mobile}")
    public Result send(
            @ApiParam(value = "手机号", required = true)
            @PathVariable("mobile") String mobile){
        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);
        //判断手机号是否已经注册
        boolean result = coreUserInfoClient.checkMobile(mobile);
        log.info("result = " + result);
        Assert.isTrue(result == false,ResponseEnum.MOBILE_EXIST_ERROR);
        //从redis获取验证码，如果获取获取到，返回ok
        //key 手机号 value 验证码
        String code = redisTemplate.opsForValue().get(mobile);
        if(!StringUtils.isEmpty(code)){
            return Result.ok().message("生成验证码成功");
        }
        //如果从redis获取不到
        //生成验证码通过整合短信服务进行发送
        code = RandomUtil.getSixBitRandom();
        ////调用service方法，通过整合短信服务进行发送
        //boolean isSend = smsService.send(mobile,code);
        //System.out.println(isSend);
        ////生成验证码放到redis里面，设置有效时间
        //if(isSend){
        //    redisTemplate.opsForValue().set("fjj:sms:code:" + mobile,code,5, TimeUnit.MINUTES);
        //    return Result.ok().message("发送短信成功");
        //}else {
        //    return Result.error().message("发送短信失败");
        //}
        redisTemplate.opsForValue().set("fjj:sms:code:" + mobile,code,5, TimeUnit.MINUTES);
        return Result.ok().message("发送短信成功");
    }
}
