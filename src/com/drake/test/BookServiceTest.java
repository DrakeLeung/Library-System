package com.drake.test;

import com.drake.model.Book;
import com.drake.service.BookService;

public class BookServiceTest {
	
	public static void main(String[] args) {
//		add();
		//remove();
		get();
		
	}
	
	public static void get() {
		BookService bookService = new BookService();
		
//		Book book = bookService.get(456);
//		System.out.println(book.toString());
	}
	
	public static void remove() {
		int isbn = 456;
		BookService bookService = new BookService();
//		bookService.remove(isbn);
	}
	
	public static void add() {
		Book book = new Book(456, "SICP", "Bryant", 22.22);
		BookService bookService = new BookService();
//		bookService.add(book);
	}
}
