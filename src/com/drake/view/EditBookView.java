package com.drake.view;

import javax.swing.JTable;

public class EditBookView {
	
	// Constructor
	public EditBookView(JTable bookTable) {
		setBookTable(bookTable);
	}
	public static void main(String[] args) {

	}

	// getters and setters
	public JTable getBookTable() {
		return bookTable;
	}
	public void setBookTable(JTable bookTable) {
		this.bookTable = bookTable;
	}

	private JTable bookTable; // 展示书本的table
}
