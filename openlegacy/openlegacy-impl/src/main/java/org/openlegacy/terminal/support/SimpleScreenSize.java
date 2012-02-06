package org.openlegacy.terminal.support;

import org.openlegacy.terminal.ScreenSize;

/**
 * A simple definition for screen size model with rows & columns properties
 * 
 */
public class SimpleScreenSize implements ScreenSize {

	private static final long serialVersionUID = 1L;

	private int rows = ScreenSize.DEFAULT_ROWS;
	private int columns = ScreenSize.DEFAULT_COLUMN;

	public SimpleScreenSize() {}

	public SimpleScreenSize(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

}
