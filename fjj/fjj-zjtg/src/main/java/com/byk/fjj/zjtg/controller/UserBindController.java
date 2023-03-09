package com.byk.fjj.zjtg.controller;

import com.byk.fjj.zjtg.service.UserBindService;
import com.byk.fjj.zjtg.util.SignUtil;
import com.byk.fjj.zjtg.model.NotifyVo;
import com.byk.fjj.zjtg.model.UserBind;
import com.byk.fjj.zjtg.task.ScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author byk
 *
 */
@Slf4j
@CrossOrigin
@Controller
@RequestMapping("/userBind")
public class UserBindController {

	@Resource
	private UserBindService userBindService;

	@Resource
	private ThreadPoolExecutor threadPoolExecutor;

	/**
	 * 绑定用户
	 * @param request
	 * @return
	 */
	@PostMapping("/BindAgreeUserV2")
	public String  bind(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> paramMap = SignUtil.switchMap(request.getParameterMap());
		SignUtil.isSignEquals(paramMap);

		boolean isBind = userBindService.isBind((String)paramMap.get("idCard"));
		if(isBind) {
			model.addAttribute("returnUrl", paramMap.get("returnUrl"));
			return "user/success";
		}

		model.addAttribute("paramMap", paramMap);
		return "user/bindUser";
	}

	@PostMapping("/save")
	public String save(Model model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> paramMap = SignUtil.switchMap(request.getParameterMap());

		UserBind userBind = userBindService.bind(paramMap);

		//异步通知
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultCode","0001");
		resultMap.put("resultMsg","成功");
		resultMap.put("bindCode",userBind.getBindCode());
		resultMap.put("agentUserId",userBind.getAgentUserId());
		resultMap.put("timestamp",new Date().getTime());
		resultMap.put("sign",SignUtil.getSign(resultMap));
		ScheduledTask.queue.offer(new NotifyVo(userBind.getNotifyUrl(), resultMap));

		//同步跳转
		//response.sendRedirect(userBind.getReturnUrl());
		model.addAttribute("returnUrl", paramMap.get("returnUrl"));
		return "user/success";
	}
}

