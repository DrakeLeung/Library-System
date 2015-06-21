package com.drake.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.drake.service.BookService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class BookView extends JFrame {
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField priceField;
	
	public BookView() {
		initUI();
	}

	private void initUI() {
		createBookForm();
		
		setVisible(true);
        setTitle("GDUT 图书管理系统");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createBookForm() {
		setLayout(new GridLayout(5, 2, 5, 5));
		
		add(createField("isbn: "));
		add(createField("书名: "));
		add(createField("作者: "));
		add(createField("价格: "));
		
		add(createApplyBtn("应用"));
	}
	
	// 创建保存按钮
	private JButton createApplyBtn(String btnName) {
		JButton btn = new JButton(btnName);

		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BookService bookService = new BookService();
				
				Map<String, Object> result = bookService.add(getIsbnField(),
						getTitleField(), getAuthorField(), getPriceField());

				JPanel panel = (JPanel) getContentPane();
				if (!(boolean) result.get("success")) {
					ViewUtil.showErrorMsg(panel, (String) result.get("msg"));
				} else {
					ViewUtil.showSuccessMsg(panel, (String) result.get("msg"));
					dispose();
				}
			}
		});
		
		return btn;
	}

	// 创建表单
	private JPanel createField(String labelName) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(labelName);
		JTextField field = new JTextField(20);
		
		panel.add(label);
		panel.add(field);
		
		switch (labelName) {
			case "isbn: ": 
				this.isbnField = field;
			case "书名: ": 
				this.titleField = field;
			case "作者: ":
				this.authorField = field;
			case "价格: ":
				this.priceField = field;
		}
		
		return panel;
	}
	
	
	
	public String getIsbnField() {
		return this.isbnField.getText();
	}
	public void setIsbnField(String isbn) {
		this.isbnField.setText(isbn);
	}
	
	public String getTitleField() {
		return this.titleField.getText();
	}
	public void setTitleField(String title) {
		this.titleField.setText(title);
	}
	
	public String getAuthorField() {
		return this.authorField.getText();
	}
	public void setAuthorField(String author) {
		this.authorField.setText(author);
	}
	
	public String getPriceField() {
		return this.priceField.getText();
	}
	public void setPriceField(String price) {
		this.priceField.setText(price);
	}
	
}

