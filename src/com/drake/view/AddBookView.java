package com.drake.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.drake.model.Book;
import com.drake.model.BookTableModel;
import com.drake.service.BookService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class AddBookView extends JFrame {
	
	public AddBookView() {
		initUI();
	}
	public AddBookView(JTable bookTable) {
		setBookTable(bookTable);
		initUI();
	}

	private void initUI() {
		createBookForm();
		
        setTitle("GDUT 图书管理系统 - 添加书籍");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
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

		// 点击'apply'按钮
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookService bookService = new BookService();
				String isbn = getIsbnField().getText();
				String title = getTitleField().getText();
				String author = getAuthorField().getText();
				String price = getPriceField().getText();
				
				Map<String, Object> result = bookService.add(isbn, title, author, price);

				JPanel panel = (JPanel) getContentPane();
				if (!(boolean) result.get("success")) {
					ViewUtil.showErrorMsg(panel, (String) result.get("msg"));
					
				} else {
					// 为了更新MainView里面的book table　= =!
					List<Book> bookList =  bookService.query(); // 获取所有书籍
					BookTableModel bookTableModel = new BookTableModel();
					bookTableModel.setData(bookList);
					getBookTable().setModel(bookTableModel);
					
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
				setIsbnField(field);
			case "书名: ": 
				setTitleField(field);
			case "作者: ":
				setAuthorField(field);
			case "价格: ":
				setPriceField(field);
		}
		
		return panel;
	}
	
//	public static void main(String[] args) {
//		new AddBookView();
//	}
	
	// getters and setters
	public JTextField getIsbnField() {
		return isbnField;
	}
	public void setIsbnField(JTextField isbnField) {
		this.isbnField = isbnField;
	}
	public JTextField getTitleField() {
		return titleField;
	}
	public void setTitleField(JTextField titleField) {
		this.titleField = titleField;
	}
	public JTextField getAuthorField() {
		return authorField;
	}
	public void setAuthorField(JTextField authorField) {
		this.authorField = authorField;
	}
	public JTextField getPriceField() {
		return priceField;
	}
	public void setPriceField(JTextField priceField) {
		this.priceField = priceField;
	}
	public JTable getBookTable() {
		return bookTable;
	}
	public void setBookTable(JTable bookTable) {
		this.bookTable = bookTable;
	}
	
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField authorField;
	private JTextField priceField;
	
	private JTable bookTable; // 展示所有藏书的table

}

