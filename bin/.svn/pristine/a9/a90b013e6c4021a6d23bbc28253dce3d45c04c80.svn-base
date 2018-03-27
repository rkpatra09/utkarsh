package com.beroe.reporting;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Alignment;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@SuppressWarnings("deprecation")
public class WriteXlReports {

	public static WritableCellFormat timesBoldUnderline;
	public static WritableCellFormat times;
	public static String inputFile;
	public static int index = 2;
	public static WritableCellFormat cellFormat;
	public static Label lable;

	@SuppressWarnings("static-access")
	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void write() throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		WriteXlReports.createLabel(excelSheet);
		// createContent(excelSheet);
		workbook.write();
		workbook.close();
	}

	public static void createLabel(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont times12pt = new WritableFont(WritableFont.TIMES, 12);
		// Define the cell format
		times = new WritableCellFormat(times12pt);
		// Lets automatically wrap the cells
		times.setWrap(true);
		// create create a bold font with underlines
		WritableFont times12pt1 = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times12pt1);
		timesBoldUnderline.setWrap(false);
		cellFormat = new WritableCellFormat(times12pt1);
		cellFormat.setBackground(Colour.LIGHT_ORANGE);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
		sheet.mergeCells(0, 0, 5, 0);
		lable = new Label(0, 0, "Test Execution Summary Report ", cellFormat);
		sheet.addCell(lable);
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		//TODO commented for for making compliation success
		//cv.setAutosize(true);
		// Write a Report headers
		addCaption(sheet, 0, 1, "S.No", cv);
		addCaption(sheet, 1, 1, "Test Case Name", cv);
		addCaption(sheet, 2, 1, "Test Result", cv);
		addCaption(sheet, 3, 1, "Sheet Name", cv);
		addCaption(sheet, 4, 1, "Start Time", cv);
		addCaption(sheet, 5, 1, "End Time", cv);
		addCaption(sheet, 6, 1, "Duration", cv);

	}

	public static void createContent(WritableSheet sheet, String tName,
			String result, String sTime, String eTime, String diffTime,String sh)
			throws WriteException, RowsExceededException {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10,
				WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times10pt);
		addNumber(sheet, 0, index, index - 1);
		addLabel(sheet, 1, index, tName);
		// Setting Font Color based on the Result
		if (result.equals("Pass")) {
			WritableFont wf = new WritableFont(WritableFont.TIMES, 12);
			wf.setColour(Colour.BLUE);
			addLabel(sheet, 2, index, result, new WritableCellFormat(wf));
		} else {
			WritableFont wf = new WritableFont(WritableFont.TIMES, 12);
			wf.setColour(Colour.RED);
			addLabel(sheet, 2, index, result, new WritableCellFormat(wf));
		}
		addLabel(sheet, 3, index, sh);
		addLabel(sheet, 4, index, sTime);
		addLabel(sheet, 5, index, eTime);
		addLabel(sheet, 6, index, diffTime);
		index++;
	}

	private static void addCaption(WritableSheet sheet, int column, int row,
			String s, CellView cv) throws RowsExceededException, WriteException {
		cellFormat = new WritableCellFormat();
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
		//TODO commented for for making compliation success
		//cv.setAutosize(true);
		sheet.setColumnView(column, cv);
	}

	private static void addNumber(WritableSheet sheet, int column, int row,
			Integer integer) throws WriteException, RowsExceededException {
		Number number;
		WritableFont wf = new WritableFont(WritableFont.TIMES, 12);
		WritableCellFormat writableCellFormat = new WritableCellFormat(wf);
		writableCellFormat.setAlignment(Alignment.CENTRE);
		number = new Number(column, row, integer, writableCellFormat);
		sheet.addCell(number);
	}

	private static void addLabel(WritableSheet sheet, int column, int row,
			String s) throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	private static void addLabel(WritableSheet sheet, int column, int row,
			String s, WritableCellFormat wcf) throws WriteException,
			RowsExceededException {
		Label label;
		label = new Label(column, row, s, wcf);
		sheet.addCell(label);
	}

}