package com.drake.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.drake.model.Book;
import com.drake.model.BookTableModel;
import com.drake.service.BookService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class FindBookView extends JFrame {
	
	// Constructor
	public FindBookView() {
		initUI();
	}
	
	// 初始化界面
	public void initUI() {
		createSearchForm();
		createBookTable();
		
        setTitle("GDUT 图书管理系统 - 查找书籍");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
	}

	// 查询结果的书籍table
	private void createBookTable() {
		JTable bookTable = new JTable();
		bookTable.setFillsViewportHeight(true);
		setBookTable(bookTable);
		
	    JScrollPane scrollPane = new JScrollPane(bookTable);
	    add(scrollPane, BorderLayout.CENTER);
	}

	// 搜索表单
	private void createSearchForm() {
		JPanel searchForm = new JPanel();
		searchForm.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// 查询说明
		JLabel title = new JLabel("输入书本的名字: ");
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 0, 0); // padding
		searchForm.add(title, c);
		
		// 查询的text field
		JTextField textfield = new JTextField(10);
		setSearchField(textfield);
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;  // tall
		searchForm.add(textfield, c);
		
		// 查询按钮
		JButton searchBtn = new JButton("开始查询");
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10, 10, 0, 10); // padding
		
		// 点击'查询'按钮,
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = (JPanel)getContentPane();
				BookService bookService = new BookService();
				String searchText = getSearchField().getText();
				
				Map<String, Object> result = bookService.query(searchText);
				
				// 返回失败
				if (!(boolean)result.get("success")) {
					ViewUtil.showErrorMsg(panel, (String)result.get("msg"));
				
				// 返回成功
				} else {
					// 把查询结果添加到table里去
					@SuppressWarnings("unchecked")
					List<Book> bookList =  (List<Book>) result.get("data");
					BookTableModel bookTableModel = new BookTableModel();
					
					bookTableModel.setData(bookList);
					getBookTable().setModel(bookTableModel);
					
					ViewUtil.showSuccessMsg(panel, (String)result.get("msg"));
				}
			}
		});
		
		searchForm.add(searchBtn, c);
		add(searchForm, BorderLayout.NORTH);
	}

	// test
	public static void main(String[] args) {
		new FindBookView().setVisible(true);

	}

	// getters and setters
	public JTable getBookTable() {
		return bookTable;
	}

	public void setBookTable(JTable bookTable) {
		this.bookTable = bookTable;
	}

	public JTextField getSearchField() {
		return searchField;
	}

	public void setSearchField(JTextField searchField) {
		this.searchField = searchField;
	}

	
	private JTable bookTable; // 显示书籍
	private JTextField searchField; // 搜索field
}
