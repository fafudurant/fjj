package com.byk.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byk
 */
@Data
public class Result {

    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    /**
     * 构造函数私有化
     */
    private Result(){}

    /**
     * 返回成功结果
     * @return
     */
    public static Result ok(){
        Result result = new Result();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(ResponseEnum.SUCCESS.getMessage());
        return result;
    }

    /**
     * 返回失败结果
     * @return
     */
    public static Result error(){
        Result result = new Result();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(ResponseEnum.ERROR.getMessage());
        return result;
    }

    /**
     * 设置特点的结果
     * @param responseEnum
     * @return
     */
    public static Result setResult(ResponseEnum responseEnum){
        Result result = new Result();
        result.setCode(responseEnum.getCode());
        result.setMessage(responseEnum.getMessage());
        return result;
    }

    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    /**
     * 设置特定的响应消息
     * @param message
     * @return
     */
    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    /**
     * 设置特定的响应码
     * @param code
     * @return
     */
    public Result code(Integer code){
        this.setCode(code);
        return this;
    }
}
