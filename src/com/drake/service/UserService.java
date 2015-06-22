package com.drake.service;

import java.util.HashMap;
import java.util.Map;

import com.drake.dao.UserDao;
import com.drake.model.User;

public class UserService {
	public Map<String, Object> login(String username, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = new User();
		
		// 检测用户名和密码
		if (username.matches("^$") || password.matches("^$")) {
			result.put("success", false);
			result.put("msg", "用户名和密码不能为空");
			return result;
		}
		
//		查询用户
		UserDao userDao = new UserDao();
		user= userDao.find(username, password);
		
		if (user.getUsername() == null) {
			result.put("success", false);
			result.put("msg", "用户名或密码错误");
			return result;
		} else {
			result.put("success", true);
			result.put("msg", "登录成功");
			result.put("data", user);
			return result;
		}
	}
	
}
