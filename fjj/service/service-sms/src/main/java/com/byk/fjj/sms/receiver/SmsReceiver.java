package com.byk.fjj.sms.receiver;

import com.byk.fjj.base.dto.SmsDTO;
import com.byk.fjj.rabbitutil.constant.MQConst;
import com.byk.fjj.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author byk
 */
@Slf4j
@Component
public class SmsReceiver {

    @Resource
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_SMS_ITEM, durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_TOPIC_SMS),
            key = {MQConst.ROUTING_SMS_ITEM}
    ))
    public void send(SmsDTO smsDTO) throws IOException {
        log.info("SmsReceiver 消息监听");
        smsService.send(smsDTO.getMobile(), smsDTO.getMessage());
    }
}
