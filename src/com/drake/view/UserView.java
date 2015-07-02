package com.drake.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;

import com.drake.model.Book;
import com.drake.model.BookTableModel;
import com.drake.model.User;
import com.drake.service.BookService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class UserView extends JFrame {
	public UserView(User user) {
		setUser(user);
		initUI();
	}
	public UserView() {
		initUI();
	}

	private void initUI() {
		createBookTable();
		createToolbar();
		createTitle();
		createStatusBar();
		
        
        setTitle("GDUT 图书管理系统");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}

	// 上方工具栏
	private void createTitle() {
		JToolBar bookToolbar = new JToolBar();
		
		// 所有可借图书按钮
		JButton allBookBtn = new JButton("可借图书");
		
		allBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookService bookService = new BookService();
				BookTableModel bookTableModel = new BookTableModel();
				
				bookTableModel.setData(bookService.getBorrowAvailable());
				
				getBookTable().setModel(bookTableModel);
			}
		});
		
		// 已借图书按钮
		JButton borrowedBookBtn = new JButton("已借图书");

		borrowedBookBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookService bookService = new BookService();
				BookTableModel bookTableModel = new BookTableModel();
				
				int userId = getUser().getId();
				bookTableModel.setData(bookService.getBorrowed(userId));
				
				getBookTable().setModel(bookTableModel);
			}
		});
		
		
		bookToolbar.add(borrowedBookBtn);
		bookToolbar.add(allBookBtn);
		add(bookToolbar, BorderLayout.NORTH);
	}
	
	// 设置下方状态栏
	private void createStatusBar() {
		User user = getUser();
		String username = "";
		
		if (user != null) {
			username = user.getUsername();
		}
		
        JLabel statusbar = new JLabel("你好, " + username + " . 欢迎使用GDUT图书管理系统．");
        
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        add(statusbar, BorderLayout.SOUTH);
	}

	// 右侧工具栏
	private void createToolbar() {
		JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
		
		JButton borrowBtn = createBtn("/resources/images/import.png");
		JButton returnBtn = createBtn("/resources/images/export.png");
		
		// '借书'按钮
		borrowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = (JPanel)getContentPane();

				// 获取选中行的书籍的isbn
				TableModel bookTableModel = getBookTable().getModel();
				int selectedRow = getBookTable().getSelectedRow();
				int isbn = 0;

				if (selectedRow == -1) {
					ViewUtil.showErrorMsg(panel, "请选择一本书");
					return;
				} else {
					isbn = (int) bookTableModel.getValueAt(selectedRow, 0);
				}

				// 调用接口
				BookService bookService = new BookService();
				Map<String, Object> result = bookService.borrowBook(getUser()
						.getId(), isbn);
				
				// 查询结果
				if ((boolean) result.get("success")) {
					// 为了更新MainView里面的book table　= =!
					List<Book> bookList = bookService.getBorrowAvailable(); // 获取所有书籍
					((BookTableModel) bookTableModel).setData(bookList);
					getBookTable().setModel(bookTableModel);

					ViewUtil.showSuccessMsg(panel, result.get("msg").toString());
				}
			}
		});
		
		// '还书'按钮
		returnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = (JPanel)getContentPane();
				TableModel bookTableModel = getBookTable().getModel();
				int selectedRow = getBookTable().getSelectedRow();
				int isbn = 0;
				
				// 检查是否选中table里面的某一行
				if (selectedRow == -1) {
					ViewUtil.showErrorMsg(panel, "请选择一本书");
					return;
				} else {
					isbn = (int) bookTableModel.getValueAt(selectedRow, 0);
				}
				
				// 获取用户id
				int userId = 0;
				User user = getUser();
				if (user != null) userId = user.getId();

				// 调用接口
				BookService bookService = new BookService();
				Map<String, Object> result = bookService.returnBook(userId, isbn);

				// 返回结果
				if ((boolean) result.get("success")) {
					// 为了更新book table　= =!
					List<Book> bookList = bookService.getBorrowed(userId); 
					((BookTableModel) bookTableModel).setData(bookList);
					getBookTable().setModel(bookTableModel);

				}
				ViewUtil.showSuccessMsg(panel, result.get("msg").toString());
			}
		});
		
		toolbar.add(borrowBtn);
		toolbar.add(returnBtn);
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(10, 5, 5, 5));
		
		add(toolbar, BorderLayout.EAST);
	}

	private JButton createBtn(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		JButton btn = new JButton(icon);
		
		btn.setBorderPainted(false);
		
		return btn;
	}
	
	// 已借图书table
	private void createBookTable() {
		BookService bookService = new BookService();
		BookTableModel bookTableModel = new BookTableModel();

		// 获取已借的书
		int userId = getUser().getId();
		bookTableModel.setData(bookService.getBorrowed(userId));	
		
		JTable bookTable = new JTable(bookTableModel);
		bookTable.setFillsViewportHeight(true);
		setBookTable(bookTable);
		
	    JScrollPane scrollPane = new JScrollPane(bookTable);
	    add(scrollPane, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		new UserView();
	}
	
	public JTable getBookTable() {
		return bookTable;
	}
	public void setBookTable(JTable bookTable) {
		this.bookTable = bookTable;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	private JTable bookTable;
	private User user;
}
