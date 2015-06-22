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
import javax.swing.border.EmptyBorder;
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

	private void initUI() {
		createBookTable();
		createToolbar();
		createTitle();

        JLabel statusbar = new JLabel("你好, " + 
        		getUser().getUsername() + " . 欢迎使用GDUT图书管理系统．");
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        add(statusbar, BorderLayout.SOUTH);
        
        setTitle("GDUT 图书管理系统");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}

	// 标题
	private void createTitle() {
		JLabel title = new JLabel("所有图书: ");
		title.setBorder(new EmptyBorder(5, 0, 5, 0));
		title.setHorizontalAlignment(JLabel.CENTER);
		
		add(title, BorderLayout.NORTH);
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
				TableModel bookTableModel = getBookTable().getModel();
				int selectedRow = getBookTable().getSelectedRow();
				int isbn = (int) bookTableModel.getValueAt(selectedRow, 0);
				boolean isBorrowed = (boolean) bookTableModel.getValueAt(selectedRow, 4);
				
				if (isBorrowed) {
					ViewUtil.showErrorMsg(panel, "此书已被借阅");
				} else {
					boolean isBorrow = true; // true表示借书
					
					BookService bookService = new BookService();
					Map<String, Object> result = bookService.borrow(isbn, isBorrow);
					
					
					if ((boolean) result.get("success")) {
						// 为了更新MainView里面的book table　= =!
						List<Book> bookList =  bookService.query(); // 获取所有书籍
						((BookTableModel) bookTableModel).setData(bookList);
						getBookTable().setModel(bookTableModel);
						
						ViewUtil.showSuccessMsg(panel, result.get("msg").toString());
					}
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
				int isbn = (int) bookTableModel.getValueAt(selectedRow, 0);
				boolean isBorrowed = (boolean) bookTableModel.getValueAt(selectedRow, 4);
				
				if (!isBorrowed) {
					ViewUtil.showErrorMsg(panel, "你还没有借阅该书，不能归还");
				} else {
					boolean isBorrow = false; // true表示还书
					
					BookService bookService = new BookService();
					Map<String, Object> result = bookService.borrow(isbn, isBorrow);
					
					if ((boolean) result.get("success")) {
						// 为了更新MainView里面的book table　= =!
						List<Book> bookList =  bookService.query(); // 获取所有书籍
						((BookTableModel) bookTableModel).setData(bookList);
						getBookTable().setModel(bookTableModel);
						
						ViewUtil.showSuccessMsg(panel, result.get("msg").toString());
					}
				}
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
	
	// 所有藏书table
	private void createBookTable() {
		BookService bookService = new BookService();
		BookTableModel bookTableModel = new BookTableModel();
		
		bookTableModel.setData(bookService.query());
		
		JTable bookTable = new JTable(bookTableModel);
		bookTable.setFillsViewportHeight(true);
		setBookTable(bookTable);
		
	    JScrollPane scrollPane = new JScrollPane(bookTable);
	    add(scrollPane, BorderLayout.CENTER);
	}
	
//	public static void main(String[] args) {
//		new UserView();
//	}
//	
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
