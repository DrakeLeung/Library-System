package com.drake.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.drake.model.User;
import com.drake.util.DBUtil;

public class UserDao {
	public User find(String username, String password) {
		Connection conn = DBUtil.getConnection();
		User user = new User();
		String sql = "SELECT * FROM user WHERE username=? and password=?";
		PreparedStatement pst;
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, password);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
}
