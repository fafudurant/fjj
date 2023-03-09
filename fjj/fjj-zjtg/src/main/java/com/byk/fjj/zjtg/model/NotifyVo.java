package com.byk.fjj.zjtg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * UserBind
 * 封装FAFU基金资金托管平台向FAFU基金发起的具体请求地址和参数集合
 * </p>
 *
 * @author byk
 */
@Data
public class NotifyVo implements Serializable {
	
	private static final long serialVersionUID = 1L;


	private String notifyUrl;

	private Map<String, Object> paramMap;

	public NotifyVo() {}

	public NotifyVo(String notifyUrl, Map<String, Object> paramMap) {
		this.notifyUrl = notifyUrl;
		this.paramMap = paramMap;
	}
}

