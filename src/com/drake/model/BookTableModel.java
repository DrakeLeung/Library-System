package com.drake.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class BookTableModel extends AbstractTableModel {

	private List<Book> data = new ArrayList<Book>();

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		// 根据实际情况返回列数
		return 5;
	}

	@Override
	public String getColumnName(int column) {
		// 根据实际情况返回列名
		switch (column) {
			case 0:
				return "isbn";
			case 1:
				return "title";
			case 2:
				return "author";
			case 3:
				return "price";
			default:
				return "isBorrowed";
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Book book = data.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return book.getIsbn();
			case 1:
				return book.getTitle();
			case 2:
				return book.getAuthor();
			case 3:
				return book.getPrice();
			default:
				return book.getIsBorrowed();
		}
	}

	// 设置数据
	public void setData(List<Book> data) {
		if (data == null)
			throw new IllegalArgumentException("参数data不能为null。");

		this.data = data;

		fireTableDataChanged();
	}
	
//	public boolean isCellEditable(int row, int col) {
//		return true;
//	}

	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex) {
		Book book = data.get(rowIndex);
		
		switch (colIndex) {
			case 0:
				if (value.toString().matches("[0-9]{1,11}")) 
					book.setIsbn(Integer.parseInt((String) value));
				else 
					System.err.println("错误: isbn必须为11位数字");
				break;
				
			case 1:
				book.setTitle((String)value);
				break;
				
			case 2:
				book.setAuthor((String)value);
				break;
				
			case 3:
				if (value.toString().matches("^(\\d*\\.)?\\d+$"))
					book.setPrice(Double.parseDouble((String) value));
				else 
					System.err.println("错误: 价格必须为合法数字");
				break;
				
			case 4:
				book.setIsBorrowed((boolean)value);
				break;
		}
		
		fireTableCellUpdated(rowIndex, colIndex);
	}

}
