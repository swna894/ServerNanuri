package com.order2david.order.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.order2david.order.model.Order;
import com.order2david.order.model.OrderItem;
import com.order2david.shop.model.Shop;
import com.order2david.supplier.model.Supplier;
import com.order2david.util.ExcelCellStyle;


/*
 *   Ordering Roprot 
 *   -. addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn)))
 */
@Component
public class ExcelInvoicePrint {

	//@Autowired
	//EnvironmentRepository environmentRepository;

	List<String> columnHeader = Arrays.asList("NO", "CODE", "DESCRIPTION", "Q'ty", "PRICE", "AMOUNT", "COMMENT");
	
	@Autowired
	ExcelCellStyle excelCellStyle;

	public String printing(Order order) {
		String fileName = "/nanuri7788/tomcat/temp/" + order.getInvoice() + "_" + order.getShop().getCompany() + ".xlsx";
	
		String sheetName = LocalDate.now().toString();			
		isReportFolder();

		try {

			Workbook workbook = createWorkSheet(fileName, sheetName);
			Sheet sheet = workbook.createSheet(sheetName);
			workbook.setSheetOrder(sheetName, 0);

			//System.err.println(fileName);
			
			int index = 0;
			index = creatOrderHeader(order,  workbook, sheet, index);
			index = createOrderList(order, columnHeader, workbook, sheet, index);
			createOrderFooter(workbook, sheet, index);
			writeExcelToFile(fileName, sheetName, workbook);
			
		} catch (Exception e) {
			System.err.println("\n파일을 사용 중이거나 또는 출력 경로의 오류 입니다...\n\nFile Path : " + fileName + "\n\n");
			//JOptionPane.showMessageDialog(null, "\n파일을 사용 중이거나 또는 출력 경로의 오류 입니다...\n\nFile Path : " + fileName + "\n\n");
			e.printStackTrace();
			//return false;
		}
		return fileName;
	}

	
	public String printingShop(Order order) {
		
		String fileName = "/nanuri7788/tomcat/temp/shop/" + order.getInvoice() + "_" + order.getShop().getCompany() + ".xlsx";
	
		String sheetName = LocalDate.now().toString();
		
		isReportFolder();

		try {

			Workbook workbook = createWorkSheet(fileName, sheetName);
			Sheet sheet = workbook.createSheet(sheetName);
			workbook.setSheetOrder(sheetName, 0);

			//System.err.println(fileName);
			
			int index = 0;
			index = creatOrderShopHeader(order,  workbook, sheet, index);
			index = createOrderList(order, columnHeader, workbook, sheet, index);
			createOrderFooter(workbook, sheet, index);
			writeExcelToFile(fileName, sheetName, workbook);

		} catch (Exception e) {
			System.err.println("\n파일을 사용 중이거나 또는 출력 경로의 오류 입니다...\n\nFile Path : " + fileName + "\n\n");
			//JOptionPane.showMessageDialog(null, "\n파일을 사용 중이거나 또는 출력 경로의 오류 입니다...\n\nFile Path : " + fileName + "\n\n");
			e.printStackTrace();
			//return false;
		}
		return fileName;
	}
	

	private int creatOrderShopHeader(Order order, Workbook workbook, Sheet sheet, int index) {
		Shop account = order.getShop();
		Supplier supplier = order.getSupplier();
		
		
		if (account == null) {
			String message = "업체가 Account에 등록이 안되었습니다... \n" + order.getShop() + " 업체 상세 내역 확인 바랍니다.";
			System.err.println(message);
		}
		
		Row row = sheet.createRow(index++);
		Cell cell = null;

		// NewVista 기록
		//String sales = "Sales Rep : David Na";
	
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue(order.getInvoice());
		cell.setCellStyle(excelCellStyle.getLeftMiddleHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 6));
		
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("");
		
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("Supplier : " + supplier.getCompany());
		
		cell = row.createCell(4);
		cell.setCellValue(LocalDate.now().toString());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));
		
		return index;

	}

	
	private void writeExcelToFile(String fileName, String sheetName, Workbook workbook)
			throws FileNotFoundException, IOException {
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		workbook.setSheetOrder(sheetName, 0);
		fileOut.close();
		workbook.close();
	}


	private void createOrderFooter(Workbook workbook, Sheet sheet, int index) {
		
//		row = sheet.createRow(index++);
//		row = sheet.createRow(index++);
//		cell = row.createCell(0);
//		cell.setCellValue("  Credit : " );
//		cell.setCellStyle(excelCellStyle.getLeftCommentHeaderStyle(workbook));
//		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 6));
		
		
		// footer sutup
		Footer footer = sheet.getFooter();
		footer.setRight(HeaderFooter.page() + "/" + HeaderFooter.numPages());

		// print setup / write Output file
		workbook.setPrintArea(0, 0, 6, 0, index);
		XSSFPrintSetup ps = (XSSFPrintSetup) sheet.getPrintSetup();
		sheet.setAutobreaks(true);
		sheet.setFitToPage(true);
		// ps.setFitHeight((short) 1);
		ps.setFitWidth((short) 1);

		workbook.setActiveSheet(0);
		workbook.setSelectedTab(0);

		sheet.setColumnWidth(0, 4 * 256);
		
		sheet.setColumnWidth(1, 12 * 256);
		//sheet.setColumnWidth(3, 30 * 256);
		sheet.autoSizeColumn(2);
		sheet.setColumnWidth(3, 6 * 256);
		sheet.setColumnWidth(4, 8 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		//sheet.setColumnWidth(6, 23 * 256);
		sheet.autoSizeColumn(6);
	}


	private int createOrderList(Order order, List<String> columnHeader, Workbook workbook, Sheet sheet, int index) {
		Row row;
		Cell cell;
		List<OrderItem> orderDetailsList = order.getOrderItems();
		
		// title
		row = sheet.createRow(index++);
		row.setHeight((short) 400);
		for (int i = 0; i < columnHeader.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnHeader.get(i));
			cell.setCellStyle(excelCellStyle.getTitleStyle(workbook));
		}

		for (int i = 0; i < orderDetailsList.size(); i++) {
			OrderItem item = orderDetailsList.get(i);

			row = sheet.createRow(index++);
			row.setHeight((short) 370);
			cell = row.createCell(0);
			cell.setCellValue(i + 1 + ")");
			cell.setCellStyle(excelCellStyle.getCenterStyle(workbook));

			cell = row.createCell(1);
			cell.setCellValue(item.getCode());
			cell.setCellStyle(excelCellStyle.getCenterStyle(workbook));

			cell = row.createCell(2);
			cell.setCellValue(item.getDescription());
			cell.setCellStyle(excelCellStyle.getLeftStyle(workbook));

			cell = row.createCell(3);
			cell.setCellValue(item.getQty());
			cell.setCellStyle(excelCellStyle.getCenterStyle(workbook));
			
			cell = row.createCell(4);
			cell.setCellValue(item.getPrice());
			cell.setCellStyle(excelCellStyle.getCurrencyDotStyle(workbook));
			
			cell = row.createCell(5);
			cell.setCellValue(item.getAmount());
			cell.setCellStyle(excelCellStyle.getCurrencyDotStyle(workbook));

			cell = row.createCell(6);
			cell.setCellValue(""); //item.getComment()
			cell.setCellStyle(excelCellStyle.getLeftStyle(workbook));
		}

		// write Total Amount
		row = sheet.createRow(index++);
		row.setHeight((short) 400);
		for (int i = 0; i < columnHeader.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue("");
			cell.setCellStyle(excelCellStyle.getTitleStyle(workbook));
			if (i == 4) {
				cell = row.createCell(4);
				cell.setCellValue("Total");
				cell.setCellStyle(excelCellStyle.getTitleStyle(workbook));
			} else if (i == 5) {
				String sumFormula = "SUM(F9:F" + (row.getRowNum()) + ")";
				cell.setCellFormula(sumFormula);
				cell.setCellStyle(excelCellStyle.getCurrencyMediumStyle(workbook));
			} 
		}
		return index;
	}


	private int creatOrderHeader(Order order, Workbook workbook, Sheet sheet, int index) {
		
		Shop account = order.getShop();
		Supplier supplier = order.getSupplier();
		
		
		if (account == null) {
			String message = "업체가 Account에 등록이 안되었습니다... \n" + order.getShop() + " 업체 상세 내역 확인 바랍니다.";
			System.err.println(message);
		}
		
		Row row = sheet.createRow(index++);
		Cell cell = null;

		// NewVista 기록
		String sales = "Sales Rep : David Na";

		cell = row.createCell(0);
		cell.setCellValue(sales);
		cell.setCellStyle(excelCellStyle.getLeftMiddleHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));

		cell = row.createCell(4);
		cell.setCellValue(supplier.getCompany());
		cell.setCellStyle(excelCellStyle.getRighttMiddleHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));

		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("To : " + account.getCompany());
		cell.setCellStyle(excelCellStyle.getLeftCompanyStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));

		cell = row.createCell(4);
		cell.setCellValue(supplier.getAddress().getStreet());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));

		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("    " + account.getAddress().getStreet() + ", " + account.getAddress().getSurburb());
		cell.setCellStyle(excelCellStyle.getLeftHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));

		cell = row.createCell(4);
		cell.setCellValue(supplier.getAddress().getCity());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));

		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("    " + account.getAddress().getCity());
		cell.setCellStyle(excelCellStyle.getLeftHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));

		cell = row.createCell(4);
		cell.setCellValue(" ☎  " + supplier.getPhone());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));

		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("   ☎  " + account.getPhone());
		cell.setCellStyle(excelCellStyle.getLeftHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));

		cell = row.createCell(4);
		cell.setCellValue("ORDER NO : " + order.getInvoice());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));

		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue("");
		cell.setCellStyle(excelCellStyle.getLeftHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 3));
		
		cell = row.createCell(4);
		cell.setCellValue(LocalDate.now().toString());
		cell.setCellStyle(excelCellStyle.getRightHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, (short) 6));
		
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		String comment = order.getComment() == null ? "" : order.getComment();
		cell.setCellValue("  REMARK : " + comment);
		cell.setCellStyle(excelCellStyle.getLeftCommentHeaderStyle(workbook));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, (short) 6));
		return index;
	}


	private Workbook createWorkSheet(String fileName, String sheetName) throws FileNotFoundException, IOException {
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		if (new File(fileName).exists()) {
			InputStream in = new FileInputStream(fileName);
			workbook = new XSSFWorkbook(in);
			for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
				Sheet tmpSheet = workbook.getSheetAt(i);
				if (tmpSheet.getSheetName().equals(sheetName)) {
					workbook.removeSheetAt(i);
				}
			}
		} else {
			workbook = new XSSFWorkbook();
		}
		return workbook;
	}


	private void isReportFolder() {
	File reportFolder = new File("/nanuri7788/tomcat/temp/shop");
	if (!reportFolder.exists()) {
		reportFolder.mkdirs();
	}
}
}