package com.drake.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drake.dao.BookDao;
import com.drake.model.Book;

public class BookService {
	// 添加书本
	public Map<String, Object> add(String isbn, String title, String author, String price) {
		Map<String, Object> result = new HashMap<String, Object>();
		Book book = new Book();
		
		result.put("success", false);
		
		// 检测isbn
		if (!isbn.matches("[0-9]{1,11}")) {
			result.put("msg", "isbn必须为11位数字");
			return result;
		} else {
			book.setIsbn(Integer.parseInt(isbn));
		}
		
		// 检测书名
		if (!title.matches("[\\S]{1,20}")) {
			result.put("msg", "书名长度必须为1-20个字符");
			return result;
		} else {
			book.setTitle(title);
		}
		
		// 检测作者
		if (!author.matches("[\\S]{1,20}")) {
			result.put("msg", "作者长度必须为1-20个字符");
			return result;
		} else {
			book.setAuthor(author);
		}
		
		// 检测价格
		if (!price.matches("[0-9]+")) {
			result.put("msg", "价格必须为数字");
			return result;
		} else {
			book.setPrice(Double.parseDouble(price));
		}
		
		BookDao bookDao = new BookDao();
		bookDao.add(book);
		result.put("success", true);
		result.put("msg", "添加成功");
		return result;
	}
	
	// 删除书本
	public Map<String, Object> remove(Object isbn) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 检测isbn
		if (!isbn.toString().matches("[0-9]{1,11}")) {
			result.put("success", false);
			result.put("msg", "isbn必须为11位数字");
			return result;
			
		} else {
			BookDao bookDao = new BookDao();
			bookDao.remove(Integer.parseInt(isbn.toString()));
			
			result.put("success", true);
			result.put("msg", "删除成功");
			return result;
		}
	}
	
	// 通过'isbn'查找书本（只有一本）
	public Book get(String isbn) {
		BookDao bookDao = new BookDao();
		
		return bookDao.get(Integer.parseInt(isbn));
	}
	
	// 通过'书名'模糊查找书本
	public Map<String, Object> query(String title) {
		Map<String, Object> result = new HashMap<String, Object>();
		BookDao bookDao = new BookDao();
		
//		判断书名是否为空
		if (title.matches("^$")) {
			result.put("success", false);
			result.put("msg", "书名不能为空");
			return result;
			
		} else {
			List<Book> bookList = (List<Book>)bookDao.query(title);
			
			if (bookList.size() == 0) {
				result.put("success", false);
				result.put("msg", "查询结果为空");
				return result;
				
			} else {
				result.put("success", true);
				result.put("data", bookList);
				result.put("msg", "查找成功");
				return result;
			}
		}
	}
	
	// 查询所有书籍
	public List<Book> query() {
		BookDao bookDao = new BookDao();
		
		return bookDao.query();
	}
	
	// 查询可借的书籍
	public List<Book> getBorrowed() {
		BookDao bookDao = new BookDao();
		
		return bookDao.getBorrowed();
	}
	
	// 更新书籍
	public Map<String, Object> update(Object isbn, Object title, Object author, Object price) {
		Map<String, Object> result = new HashMap<String, Object>();
		Book book = new Book();
		
		result.put("success", false);
		
		// 检测isbn
		if (!isbn.toString().matches("[0-9]{1,11}")) {
			result.put("msg", "isbn必须为11位数字");
			return result;
		} else {
			book.setIsbn(Integer.parseInt(isbn.toString()));
		}
		
		// 检测书名
		if (title.toString().matches("[\\S]{1,20}")) {
			book.setTitle(title.toString());
		} else {
			result.put("msg", "书名长度必须为1-20个字符");
			return result;
		}
		
		// 检测作者
		if (!author.toString().matches("[\\S]{1,20}")) {
			result.put("msg", "作者长度必须为1-20个字符");
			return result;
		} else {
			book.setAuthor(author.toString());
		}
		
		// 检测价格
		if (!price.toString().matches("^(\\d*\\.)?\\d+$")) {
			result.put("msg", "价格必须为数字");
			return result;
		} else {
			book.setPrice(Double.parseDouble(price.toString()));
		}
		
		BookDao bookDao = new BookDao();
		bookDao.update(book);
		result.put("success", true);
		result.put("msg", "更新成功");
		
		return result;
	}
	
	// 借／还书
	public Map<String, Object> borrow(int isbn, boolean isBorrow) {
		Map<String, Object> result = new HashMap<String, Object>();
		BookDao bookDao = new BookDao();
		
		bookDao.borrow(isbn, isBorrow);
		result.put("success", true);
		result.put("msg", isBorrow ? "已成功借阅" : "已归还成功");
		
		return result;
	}
}
