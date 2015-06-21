package com.drake.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import com.drake.model.BookTableModel;
import com.drake.service.BookService;
import com.drake.util.ViewUtil;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	private JTable bookListTable;
	
	public MainView() {
		initUI();
	}

	// 初始化界面
	private void initUI() {
		createMenuBar();
		
		add(createToolBar(), BorderLayout.EAST);
		add(createBookListTable(), BorderLayout.CENTER);
		
		JLabel title = new JLabel("所有藏书");
		title.setBorder(new EmptyBorder(5, 0, 5, 0));
		title.setHorizontalAlignment(JLabel.CENTER);
		add(title, BorderLayout.NORTH);
		
		
        setTitle("GDUT 图书管理系统");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
	}


	// 右侧工具栏
	public JToolBar createToolBar() {
		JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
		
		JButton rmBtn = createBtn("/resources/images/remove.png");
		JButton editBtn = createBtn("/resources/images/edit.png");
		JButton addBtn = createBtn("/resources/images/add.png");
		JButton findBtn = createBtn("/resources/images/find.png");
		
		// 更新书本
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableModel bookTableModel = bookListTable.getModel();
				int selectedRowIndex = bookListTable.getSelectedRow();

				// 获取选中行数据
				Object selectedIsbn = bookTableModel.getValueAt(selectedRowIndex, 0);
				Object selectedTitle = bookTableModel.getValueAt(selectedRowIndex, 1);
				Object selectedAuthor = bookTableModel.getValueAt(selectedRowIndex, 2);
				Object selectedPrice = bookTableModel.getValueAt(selectedRowIndex, 3);
				
				
				BookService bookService = new BookService();
				Map<String, Object> result = bookService
						.update(selectedIsbn, selectedTitle, selectedAuthor, selectedPrice);

				JPanel panel = (JPanel) getContentPane();
				if (!(boolean) result.get("success")) {
					ViewUtil.showErrorMsg(panel, (String) result.get("msg"));
				} else {
					ViewUtil.showSuccessMsg(panel, (String) result.get("msg"));
				}
			}
		});
		
		// 删除书本
		rmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 // 获取选中书本的isbn
				Object selectedIsbn = bookListTable.getModel()
					.getValueAt(bookListTable.getSelectedRow(), 0);
				
				BookService bookService = new BookService();
				Map<String, Object> result = bookService.remove(selectedIsbn);
				
				JPanel panel = (JPanel)getContentPane();
				if (!(boolean)result.get("success")) {
					ViewUtil.showErrorMsg(panel, (String)result.get("msg"));
				} else {
					ViewUtil.showSuccessMsg(panel, (String)result.get("msg"));
				}
			}
		});
		
		// 查找书本
		findBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new FindBookView();
			}
		});
		
		toolbar.add(rmBtn);
		toolbar.add(editBtn);
		toolbar.add(addBtn);
		toolbar.add(findBtn);
		toolbar.setFloatable(false);
		
//		toolbar.setMargin(new Insets(10, 5, 5, 5));
		return toolbar;
	}
	
	private JButton createBtn(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		JButton btn = new JButton(icon);
		
		btn.setBorderPainted(false);
		
		return btn;
	}

	// 所有藏书table
	private JScrollPane createBookListTable() {
		BookService bookService = new BookService();
	    BookTableModel tableModel = new BookTableModel();
	    
	    tableModel.setData(bookService.query());
	    
	    JTable bookTable = new JTable(tableModel);
	    this.bookListTable = bookTable;
	    bookTable.setFillsViewportHeight(true);
	    
	    JScrollPane scrollPane = new JScrollPane(bookTable);
	    
	    return scrollPane;
	}
	
	// 菜单栏
	public void createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		menubar.add(createFileMenu());
		menubar.add(createManagementMenu());
		menubar.add(Box.createHorizontalGlue());
		menubar.add(refreshMenu());
		menubar.add(createHelpMenu());
		
		setJMenuBar(menubar);
	}
	
	// 图书管理菜单
	public JMenu createManagementMenu() {
		JMenu management = new JMenu("图书管理");
		
		JMenuItem addItem= new JMenuItem("添加图书");
		JMenuItem removeItem = new JMenuItem("删除图书");
		JMenuItem findItem = new JMenuItem("查找图书");
		JMenuItem updateItem = new JMenuItem("修改图书");
		
		// 点击添加图书
		addItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new BookView();
			}
		});
		
		management.add(addItem);
		management.add(removeItem);
		management.add(findItem);
		management.add(updateItem);
		
		return management;
	}
	
	// 借/还书菜单
	public JMenu createFileMenu() {
		JMenu borrowAndReturn = new JMenu("借/还书");
		JMenuItem borrowBook = new JMenuItem("借书");
		JMenuItem returnBook = new JMenuItem("还书");
		
		borrowAndReturn.add(borrowBook);
		borrowAndReturn.add(returnBook);
		
		return borrowAndReturn;
	}
	
	// 刷新菜单
	public JMenu refreshMenu() {
		JMenu refresh = new JMenu("刷新");
		JMenuItem refreshNow = new JMenuItem("立即刷新");
		
		// 重新刷新所藏书本table
		refreshNow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookService bookService = new BookService();
			    BookTableModel tableModel = new BookTableModel();
			    
			    tableModel.setData(bookService.query());
				bookListTable.setModel(tableModel);
			}
		});
		
		refresh.add(refreshNow);
		return refresh;
	}
	
	// 帮助菜单
	public JMenu createHelpMenu() {
		JMenu help = new JMenu("帮助");
		JMenuItem exitItem = new JMenuItem("退出");
		
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);			
			}
		});
		
		help.add(exitItem);
		
		return help;
	}
}
