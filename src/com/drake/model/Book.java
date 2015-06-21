package com.drake.model;

public class Book {
	private int isbn;
	private String title;
	private String author;
	private double price;
	private boolean isBorrowed;
	
	public Book() {		
	}
	
	// 构造函数
	public Book(int isbn, String title, String author, double price) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.price = price;
		this.isBorrowed = false;
	}
	
	
	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setIsBorrowed(boolean isBorrowed) {
		this.isBorrowed = isBorrowed;
	}
	public boolean getIsBorrowed() {
		return this.isBorrowed;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", author=" + author
				+ ", price=" + price + ", isBorrowed=" + isBorrowed + "]";
	}
	
}
