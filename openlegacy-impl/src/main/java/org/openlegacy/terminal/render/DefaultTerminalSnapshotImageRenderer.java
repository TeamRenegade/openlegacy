package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRow;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SnapshotUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class DefaultTerminalSnapshotImageRenderer implements TerminalSnapshotImageRenderer {

	// default values for bean properties
	private Color imageBackgroundColor = Color.BLACK;
	private Color imageBoldFieldColor = Color.WHITE;
	private Color imageDefaultTextColor = Color.GREEN;
	private Color imageSorroundingTextColor = Color.WHITE;

	private int leftColumnsOffset = 2;
	private int topPixelsOffset = 2;
	private int imageWidth = 825;
	private int imageHeight = 400;
	private int fontSize = 15;
	private String fontFamily = "Courier New";
	private boolean drawLineNumbers = true;
	private int widthProportion = 10;
	private int heightProportion = 15;
	private int fontType = Font.PLAIN;

	public void render(TerminalSnapshot terminalSnapshot, OutputStream output) {

		BufferedImage buffer = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		Font font = new Font(fontFamily, fontType, fontSize);
		Graphics graphics = buffer.createGraphics();
		graphics.setFont(font);
		setDefaultColor(graphics);

		markBackgroundAndInputFields(terminalSnapshot, graphics);

		drawText(terminalSnapshot, graphics);

		try {
			ImageIO.write(buffer, "jpg", output);
		} catch (IOException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	private void drawText(TerminalSnapshot terminalSnapshot, Graphics graphics) {
		int columns = terminalSnapshot.getSize().getColumns();
		List<TerminalRow> rows = terminalSnapshot.getRows();
		String screenText = terminalSnapshot.getText();
		for (TerminalRow terminalRow : rows) {
			int rowNumber = terminalRow.getRowNumber();
			int startY = toHeight(rowNumber);

			if (drawLineNumbers) {
				// draw row number
				graphics.setColor(imageSorroundingTextColor);
				graphics.drawString(String.valueOf(String.format("%2d", terminalRow.getRowNumber())), 0, startY);
			}

			int rowStart = (rowNumber - 1) * columns; // row is 1 based, drawing is 0 base
			String text = screenText.substring(rowStart, rowStart + columns);
			for (int i = 0; i < text.length(); i++) {
				// text is 0 based, columns are 1 based
				TerminalField currentField = terminalSnapshot.getField(SimpleTerminalPosition.newInstance(rowNumber, i + 1));
				if (currentField != null && currentField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
					graphics.setColor(imageBackgroundColor);
				} else {
					if (currentField != null) {
						graphics.setColor(SnapshotUtils.convertColor(currentField.getColor()));
						if (currentField.isBold() && currentField.getColor() == org.openlegacy.terminal.Color.GREEN) {
							graphics.setColor(imageBoldFieldColor);
						}
					} else {
						setDefaultColor(graphics);
					}
				}
				// 2 - place holder for row numbers
				graphics.drawString(String.valueOf(text.charAt(i)), toWidth(i + leftColumnsOffset), startY);
			}
		}
	}

	private void markBackgroundAndInputFields(TerminalSnapshot terminalSnapshot, Graphics graphics) {
		int endX;
		List<TerminalField> fields = terminalSnapshot.getFields();
		setDefaultColor(graphics);
		for (TerminalField terminalField : fields) {
			TerminalPosition position = terminalField.getPosition();
			// -1 - pixels is 0 based , column is 1 based
			int startX = toWidth(position.getColumn() - 1 + leftColumnsOffset);
			int startY = toHeight(position.getRow());
			endX = toWidth(terminalField.getEndPosition().getColumn() + leftColumnsOffset);
			if (terminalField.isEditable()) {
				graphics.drawLine(startX, startY, endX, startY);
			}
			int rowHeight = toHeight(1);
			if (terminalField.getBackColor() != org.openlegacy.terminal.Color.BLACK) {
				graphics.setColor(SnapshotUtils.convertColor(terminalField.getBackColor()));
				//
				graphics.fillRect(startX, toHeight(position.getRow() - 1) + topPixelsOffset, toWidth(terminalField.getLength()),
						rowHeight);
			}
		}

		List<TerminalPosition> fieldSeperators = terminalSnapshot.getFieldSeperators();
		graphics.setColor(imageDefaultTextColor);
		for (TerminalPosition terminalPosition : fieldSeperators) {
			graphics.drawString("^", toWidth(terminalPosition.getColumn() - 1 + leftColumnsOffset),
					toHeight(terminalPosition.getRow()));
		}
	}

	private void setDefaultColor(Graphics graphics) {
		graphics.setColor(imageDefaultTextColor);
	}

	private int toHeight(int row) {
		return row * heightProportion;
	}

	private int toWidth(int column) {
		return column * widthProportion;
	}

	public String getFileFormat() {
		return "jpg";
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public void setDrawLineNumbers(boolean drawLineNumbers) {
		this.drawLineNumbers = drawLineNumbers;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setHeightProportion(int heightProportion) {
		this.heightProportion = heightProportion;
	}

	public void setWidthProportion(int widthProportion) {
		this.widthProportion = widthProportion;
	}

	public void setLeftColumnsOffset(int leftColumnsOffset) {
		this.leftColumnsOffset = leftColumnsOffset;
	}

	public void setTopPixelsOffset(int topPixelsOffset) {
		this.topPixelsOffset = topPixelsOffset;
	}

	public void setImageBackgroundColor(Color imageBackgroundColor) {
		this.imageBackgroundColor = imageBackgroundColor;
	}

	public void setImageBoldFieldColor(Color imageBoldFieldColor) {
		this.imageBoldFieldColor = imageBoldFieldColor;
	}

	public void setImageDefaultTextColor(Color imageDefaultTextColor) {
		this.imageDefaultTextColor = imageDefaultTextColor;
	}

	public void setImageSorroundingTextColor(Color imageSorroundingTextColor) {
		this.imageSorroundingTextColor = imageSorroundingTextColor;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}
}