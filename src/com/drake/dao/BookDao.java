package com.drake.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.drake.model.Book;
import com.drake.util.DBUtil;


public class BookDao {
	public void add(Book book) {
		Connection conn = DBUtil.getConnection();
		
		try {
			String sql = "INSERT INTO book" + 
					"(isbn, title, author, price, isBorrowed)" + 
					"VALUES(?, ?, ?, ?, ?)";
			
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, book.getIsbn());
			pst.setString(2, book.getTitle());
			pst.setString(3, book.getAuthor());
			pst.setDouble(4, book.getPrice());
			pst.setBoolean(5, book.getIsBorrowed());
			
			pst.execute();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}	
	}

	public void remove(int isbn) {
		Connection conn = DBUtil.getConnection();
		
		String sql = "DELETE FROM book " +
				"WHERE isbn=?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, isbn);
			
			pst.execute();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	// 根据isbn查询书本
	public Book get(int isbn) {
		Book book = new Book();
		
		Connection conn = DBUtil.getConnection();
		
		String sql = "SELECT * FROM book WHERE isbn=?";
		PreparedStatement pst;
		
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, isbn);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				book.setIsbn(rs.getInt("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPrice(rs.getDouble("price"));
				book.setIsBorrowed(rs.getBoolean("isBorrowed"));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return book;
	}
	
	
	// 获取用户已借书籍
	public List<Book> getBorrowed(int userId) {
		List<Book> bookBorrowed = new LinkedList<Book>();
		Connection conn = DBUtil.getConnection();
		ResultSet rs = null;
		
		try {
			// 先找出用户借过的书的isbn
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM book WHERE isbn in (SELECT book_isbn FROM user_book_borrowed WHERE user_id=" + userId + ")";
			rs = st.executeQuery(sql);
			
			while (rs.next()) {
				Book book = new Book();
				
				book.setIsbn(rs.getInt("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPrice(rs.getFloat("price"));
//				book.setIsBorrowed(rs.getBoolean("isBorrowed"));
				
				bookBorrowed.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookBorrowed;
	}

	// 获取所有书籍
	public List<Book> query() {
		Connection conn = DBUtil.getConnection();
		List<Book> bookList = new LinkedList<Book>();
		
		try {
			Statement st = conn.createStatement();
			String query = "SELECT * FROM book";
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				Book book = new Book();
				
				book.setIsbn(rs.getInt("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPrice(rs.getFloat("price"));
				book.setIsBorrowed(rs.getBoolean("isBorrowed"));
				
				bookList.add(book);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return bookList;
	}
	
	// 查询可借书籍
	public List<Book> getBorrowAvailable() {
		Connection conn = DBUtil.getConnection();
		List<Book> bookList = new LinkedList<Book>();
		
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM book WHERE isBorrowed=false";
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				Book book = new Book();
				
				book.setIsbn(rs.getInt("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPrice(rs.getFloat("price"));
				book.setIsBorrowed(rs.getBoolean("isBorrowed"));
				
				bookList.add(book);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return bookList;
	}

	// 根据书名模糊查询书籍
	public List<Book> query(String title) {
		Connection conn = DBUtil.getConnection();
		List<Book> bookList = new ArrayList<Book>();
		
		String sql = "SELECT * FROM book " + 
				"WHERE title like ?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, "%" + title + "%");
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Book book = new Book();
				book.setIsbn(rs.getInt("isbn"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPrice(rs.getDouble("price"));
				book.setIsBorrowed(rs.getBoolean("isBorrowed"));
				
				bookList.add(book);
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return bookList;
	}
	
	public void update(Book book) {
		Connection conn = DBUtil.getConnection();
		
		String sql = "UPDATE book" + 
				" SET title=?, author=?, price=?, isBorrowed=?" + 
				" WHERE isbn=?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			
			pst.setString(1, book.getTitle());
			pst.setString(2, book.getAuthor());
			pst.setDouble(3,  book.getPrice());
			pst.setBoolean(4, book.getIsBorrowed());
			pst.setInt(5, book.getIsbn());
			
			pst.execute();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	// 还书
	public boolean returnBook(int userId, int isbn) {
		Connection conn = DBUtil.getConnection();
		String sql = "DELETE FROM user_book_borrowed WHERE user_id=? and book_isbn=?";
		PreparedStatement pst = null;

		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, isbn);

			pst.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// 更新isBorrowed
		Book book = get(isbn);
		book.setIsBorrowed(false);
		update(book);
		
		return true;
	}
	
	// 借书
	public boolean borrowBook(int userId, int isbn) {
		Connection conn = DBUtil.getConnection();
		String sql = "INSERT INTO user_book_borrowed(user_id, book_isbn) values(?, ?)";
		PreparedStatement pst = null;

		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, isbn);

			pst.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// 更新isBorrowed
		Book book = get(isbn);
		book.setIsBorrowed(true);
		update(book);
		
		return true;
	}

	
}
