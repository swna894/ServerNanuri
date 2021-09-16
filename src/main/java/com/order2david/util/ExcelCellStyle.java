package com.order2david.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelCellStyle {

	/**
	 * Create a library of cell styles
	 */

	Short fontSize = 11;

	public CellStyle getHeadStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);

		return cellStyle;
	}

	public CellStyle getTitleStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setFont(font);
		return cellStyle;

	}

	public CellStyle getAmountStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setFont(font);
		return cellStyle;

	}

	public CellStyle getRightStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.DOTTED);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getTopMediumStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		return cellStyle;
	}

	public CellStyle getCurrencyDotStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("$#,##0.00"));
		cellStyle.setBorderBottom(BorderStyle.DOTTED);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getCurrencyMediumStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("$#,##0.00"));
		cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getCurrencyLabelStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("$#,##0.00"));
		// cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		//cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getCurrencyStyle(Workbook workbook) {

		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		DataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("$#,##0.00"));
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getCenterStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.DOTTED);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints(fontSize);
		// font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.DOTTED);
		cellStyle.setWrapText(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRightHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRightDateStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 13);
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftMiddleHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 18);
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftHeaderCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 28);
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRighButtomtHeaderCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 10);
		// font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRightHeaderCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 10);
		// font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRighToptHeaderCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 10);
		// font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getCenterHeaderCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short) 18);
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getRighttMiddleHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftCommentHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftCompanyStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("times"); // times
		font.setBold(true);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getBigHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 28);
		font.setFontName("Forte"); // times
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getAvalonHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 16);
		font.setFontName("Forte"); // times
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

	public CellStyle getLeftFooterStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setBold(false);

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		return cellStyle;
	}

//	HSSFFont dateFont = wb.createFont();
//	dateFont.setFontHeightInPoints((short) 11);
//	dateFont.setBoldweight((short) Font.BOLD);
//	style = wb.createCellStyle();
//	style.setAlignment(CellStyle.ALIGN_RIGHT);
//	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//	style.setFont(dateFont);
//	styles.put("date", style);

//	HSSFFont titleFont = wb.createFont();
//	titleFont.setFontHeightInPoints((short) 9);
//	titleFont.setBoldweight((short) Font.BOLD);
//	style = wb.createCellStyle();
//	style.setAlignment(CellStyle.ALIGN_CENTER);
//	style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//	style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
//	style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
//	style.setFont(titleFont);
//	styles.put("title", style);

}

//		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
//		DataFormat format = wb.createDataFormat();
//		
//		// Create a new font and alter it.
//		Font font = wb.createFont();
//		font.setFontHeightInPoints((short) 9);
//		font.setFontName("���� ���");
//		font.setColor(IndexedColors.BLACK.getIndex());
//
//		Font redFont = wb.createFont();
//		redFont.setFontHeightInPoints((short) 9);
//		redFont.setFontName("���� ���");
//		redFont.setColor(IndexedColors.RED.getIndex());
//
//		XSSFCellStyle  style = wb.createCellStyle();
//		style.setFont(font);
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 216, 216))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		styles.put("groupTitle", style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());	
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 216, 216))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		style.setWrapText(true);
//		styles.put("�÷�Ÿ��Ʋ", style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 216, 216))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(redFont);
//		style.setWrapText(true);
//		styles.put("���÷�Ÿ��Ʋ", style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setFont(font);
//		styles.put("�߾�����", style);
//		
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFont(font);
//		styles.put("�⺻", style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setDataFormat(format.getFormat("0.0"));
//		style.setFont(font);
//		styles.put("BANCHASUM", style);
//		
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(204, 238, 255)));  
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(HYUMU, style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(234, 200, 200))); // 255, 106, 102
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(JUHYU, style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(153, 255, 230))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(DAEHYU, style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(false);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(204, 255, 51))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(YEONCHA, style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(true);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(179, 198, 255))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(GYEONGJO, style);
//		
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(true);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 194, 153))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(GONGHYU, style);
//		
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setWrapText(true);
//		style.setBorderRight(CellStyle.BORDER_THIN);
//		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderLeft(CellStyle.BORDER_THIN);
//		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderTop(CellStyle.BORDER_THIN);
//		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//		style.setBorderBottom(CellStyle.BORDER_THIN);
//		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 204, 0))); 
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put(BANCHA, style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put("LIGHT_GREEN_NO", style);
//
//		style = wb.createCellStyle();
//		style.setAlignment(CellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 102)));  //IndexedColors.GOLD.getIndex()
//		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		style.setFont(font);
//		styles.put("GOLD_NO", style);
//
//		return styles;
//	}
