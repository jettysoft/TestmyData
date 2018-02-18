package com.testmydata.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFPatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ReportsDownloader {

	public void download(String reportname, int batchid, String folderpath, String filetype,
			ArrayList<String> reportlist, ArrayList<String> reportdata) {
		boolean status = false;
		if (filetype.equals("pdf")) {
			String filepath = (new File(".", "/" + folderpath + File.separator + reportname + "_" + batchid + ".pdf")
					.getAbsolutePath());
			status = generatepdf(filepath, reportname, reportlist, reportdata);
		} else if (filetype.equals("excel")) {
			String filepath = (new File(".", "/" + folderpath + File.separator + reportname + "_" + batchid + ".xlsx")
					.getAbsolutePath());
			status = genenratexlsx(batchid, filepath, reportname, reportlist, reportdata);
		}

		if (status) {
			CommonFunctions.message = reportname + " Saved Successfully\n\n@ " + folderpath + "";
			CommonFunctions.invokeAlertBox(getClass());
		} else {
			CommonFunctions.message = reportname + " Failed to save...!";
			CommonFunctions.invokeAlertBox(getClass());
		}
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private boolean generatepdf(String filePath, String reportname, ArrayList<String> reportlist,
			ArrayList<String> reportdata) {
		boolean returnValue = false;
		Document document = null;
		if (reportlist.size() > 4) {
			document = new Document(PageSize.A4.rotate());
		} else {
			document = new Document();
		}
		Vector allChequeDetailsVector = new Vector();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			// Image image1 = Image.getInstance(new File(".",
			// "com/trademen/FXImages/logo.png").getAbsolutePath());
			// image1.setAbsolutePosition(0, 0);
			//
			// PdfContentByte byte2 = writer.getDirectContent();
			// PdfTemplate tp2 = byte2.createTemplate(600, 150);
			// tp2.addImage(image1);
			// byte2.addTemplate(tp2, 0, 0);
			// Phrase phrase2 = new Phrase(byte2 + "",
			// FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));
			// HeaderFooter footer = new HeaderFooter(phrase2, true);
			// document.setFooter(footer);

			document.add(new Paragraph("" + "\t\t\t"));
			document.add(new Paragraph("\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + reportname.toUpperCase()));
			document.add(new Paragraph("" + "\t\t\t"));
			PdfPTable table = new PdfPTable(reportlist.size());
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

			for (int i = 0; i < reportlist.size(); i++) {
				PdfPCell test = new PdfPCell(new Phrase(reportlist.get(i).toString().toUpperCase()));
				// test.setBackgroundColor(new Color(100, 221, 23));
				test.setBackgroundColor(new Color(186, 239, 9));
				test.setBorderColor(Color.lightGray);
				test.setHorizontalAlignment(Element.ALIGN_CENTER);
				test.setVerticalAlignment(Element.ALIGN_CENTER);
				table.addCell(test);
			}
			int countcells = 0;
			int countreversecells = 0;
			for (int i = 0; i < reportdata.size(); i++) {
				String celldata = null;
				if (reportdata.get(i) == null) {
					celldata = "";
				} else {
					celldata = reportdata.get(i).toString();
				}
				PdfPCell test2 = new PdfPCell(new Phrase(celldata));
				countcells++;
				test2.setBorderColor(Color.lightGray);
				test2.setHorizontalAlignment(Element.ALIGN_CENTER);
				test2.setVerticalAlignment(Element.ALIGN_CENTER);
				if (countcells > reportlist.size()) {
					// test2.setBackgroundColor(new Color(204, 255, 144));
					test2.setBackgroundColor(new Color(244, 249, 229));
					countreversecells++;
					if (countreversecells == reportlist.size()) {
						countcells = 0;
						countreversecells = 0;
					}
				}
				if (celldata.contains("Fail")) {
					test2.setBackgroundColor(new Color(249, 66, 49));
				}

				table.addCell(test2);
			}

			document.add(table);
			document.close();
			returnValue = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			returnValue = false;
		} catch (DocumentException e) {
			e.printStackTrace();
			returnValue = false;
		}

		return returnValue;
	}

	private boolean genenratexlsx(int batchid, String filePath, String reportname, ArrayList<String> reportlist,
			ArrayList<String> reportdata) { // excel
		boolean returnValue = false;
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(batchid + "_Test_Results");
			sheet.createFreezePane(0, 1, 0, 1);
			createHeaderRow(sheet, reportlist);

			XSSFCellStyle mycellstyle = workbook.createCellStyle();
			mycellstyle.setAlignment(HorizontalAlignment.CENTER);
			mycellstyle.setVerticalAlignment(VerticalAlignment.CENTER);

			int maxrowcount = (reportdata.size() / reportlist.size());
			int m = 0;
			for (int k = 0; k < maxrowcount; k++) {
				Row row1 = sheet.createRow(k + 1);
				int cloumncount = 0;
				for (int j = m; j < reportdata.size(); j++) { // {1,2,3},{4,5,6},{7,8,9}
					Cell cell = row1.createCell(++cloumncount);
					if (reportdata.get(j).matches("\\d+(\\.\\d+)?")) {
						cell.setCellValue(Double.parseDouble(reportdata.get(j)));
					} else {
						cell.setCellValue(reportdata.get(j).toString().toUpperCase());
					}
					cell.setCellStyle(mycellstyle);
					cell.setAsActiveCell();

					if (cloumncount == reportlist.size()) {
						m += cloumncount;
						break;
					}
				}
			}

			autoSizeColumns(workbook);

			XSSFSheetConditionalFormatting myconditions = sheet.getSheetConditionalFormatting();

			XSSFConditionalFormattingRule myrules1 = myconditions.createConditionalFormattingRule(ComparisonOperator.GE,
					"1");
			XSSFFontFormatting ruletype1 = myrules1.createFontFormatting();
			ruletype1.setFontColorIndex(IndexedColors.RED.getIndex());
			ruletype1.setFontStyle(false, true);

			XSSFConditionalFormattingRule myrules2 = myconditions.createConditionalFormattingRule(ComparisonOperator.LT,
					"1");
			XSSFFontFormatting ruletype2 = myrules2.createFontFormatting();
			ruletype2.setFontColorIndex(IndexedColors.GREEN.getIndex());
			ruletype2.setFontStyle(false, true);

			ConditionalFormattingRule[] rules = { myrules1, myrules2 };
			CellRangeAddress[] rulerange1 = { CellRangeAddress.valueOf("G2:G100000"),
					CellRangeAddress.valueOf("I2:I100000"), CellRangeAddress.valueOf("K2:K100000"),
					CellRangeAddress.valueOf("M2:M100000"), CellRangeAddress.valueOf("O2:O100000"),
					CellRangeAddress.valueOf("R2:R100000"), CellRangeAddress.valueOf("T2:T100000"),
					CellRangeAddress.valueOf("V2:V100000"), CellRangeAddress.valueOf("X2:X100000") };

			myconditions.addConditionalFormatting(rulerange1, rules);

			XSSFConditionalFormattingRule myrules3 = myconditions
					.createConditionalFormattingRule(ComparisonOperator.EQUAL, "FAILED");
			XSSFFontFormatting ruletype3 = myrules3.createFontFormatting();
			ruletype3.setFontColorIndex(IndexedColors.WHITE.getIndex());
			ruletype3.setFontStyle(false, true);
			// set background color
			XSSFPatternFormatting rulepattern1 = myrules3.createPatternFormatting();
			rulepattern1.setFillBackgroundColor(IndexedColors.RED.getIndex());

			XSSFConditionalFormattingRule myrules4 = myconditions
					.createConditionalFormattingRule(ComparisonOperator.EQUAL, "PASSED");
			XSSFFontFormatting ruletype4 = myrules4.createFontFormatting();
			ruletype4.setFontColorIndex(IndexedColors.WHITE.getIndex());
			ruletype4.setFontStyle(false, true);
			// set background color
			XSSFPatternFormatting rulepattern2 = myrules4.createPatternFormatting();
			rulepattern2.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			CellRangeAddress[] rulerange9 = { CellRangeAddress.valueOf("Z2:Z100000") };
			myconditions.addConditionalFormatting(rulerange9, myrules3);
			myconditions.addConditionalFormatting(rulerange9, myrules4);

			try (FileOutputStream outputStream = new FileOutputStream(new File(filePath))) {
				workbook.write(outputStream);
				workbook.close();
				returnValue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}

	private void createHeaderRow(Sheet sheet, ArrayList<String> reportlist) {

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 13);
		cellStyle.setFont(font);
		cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		Row row = sheet.createRow(0);
		int columnCount = 0;
		for (int i = 0; i < reportlist.size(); i++) {
			Cell cell = row.createCell(++columnCount);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(reportlist.get(i).toString().toUpperCase());
		}
	}

	public void autoSizeColumns(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}
}
