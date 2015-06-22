package com.drake.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.drake.model.User;
import com.drake.service.UserService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
	public LoginView() {
		initUI();
		
        setTitle("GDUT 图书管理系统 - 登录界面");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}

	private void initUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.ipady = 20;
		c.insets = new Insets(0, 10, 10, 0);
		
		// 用户名label
		JLabel usernameLabel = new JLabel("用户名: ");
		c.gridx = 0;
		c.gridy =  0;
		add(usernameLabel, c);
		
		// 用户名field
		JTextField usernameField = new JTextField(20);
		setUsernameField(usernameField);
		c.gridx = 1;
		c.gridy = 0;
		add(usernameField, c);
		
		// 密码label
		JLabel passwordLabel = new JLabel("密码: ");
		c.gridx = 0;
		c.gridy =  1;
		add(passwordLabel, c);
		
		// 密码field
		JPasswordField passwordField = new JPasswordField(20);
		setPasswordField(passwordField);
		c.gridx = 1;
		c.gridy = 1;
		add(passwordField, c);
		
		// 登录按钮
		JButton loginBtn = new JButton("登录");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = getUsernameField().getText();
				String password= getPasswordField().getText();
				
				JPanel panel = (JPanel) getContentPane();
				UserService userService = new UserService();
				Map<String, Object> result = userService.login(username, password);
				
				if (!(boolean) result.get("success")) {
					ViewUtil.showErrorMsg(panel, result.get("msg").toString());
					
				} else {
					ViewUtil.showSuccessMsg(panel, result.get("msg").toString());
					
					User user = (User)result.get("data");
					
					if (user.getRole().equals("admin")) {
						new MainView(user);
						dispose();
					} else {
						new UserView(user);
						dispose();
					}
				}
			}
		});
		add(loginBtn, c);
		
		// 注册按钮
		JButton signupBtn = new JButton("注册");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(signupBtn, c);
		
	}
	
	public static void main(String[] args) {
		new LoginView();
	}
	
	
	// getters and setters
	public JTextField getUsernameField() {
		return usernameField;
	}
	public void setUsernameField(JTextField usernameField) {
		this.usernameField = usernameField;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
	public void setPasswordField(JPasswordField passwordField) {
		this.passwordField = passwordField;
	}

	private JPasswordField passwordField;
	private JTextField usernameField;
}
