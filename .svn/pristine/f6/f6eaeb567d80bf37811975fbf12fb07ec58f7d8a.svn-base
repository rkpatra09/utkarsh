package com.beroe.reporting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteHTMLReport {
	static BufferedWriter bw1=null;
	
	/**
	 * @param curr_tc_name
	 * @param tc_result
	 * @param tc_start_time
	 * @param tc_end_time
	 * @param timeDiff
	 * @throws IOException
	 */
	
	public static void updatePassHtmlReport(String curr_tc_name,
			String tc_result, String tc_start_time, String tc_end_time,
			String timeDiff,int sl_no,String sh) throws IOException {
		bw1.write("<TR><TD COLSPAN=1 BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ sl_no
				+ "</B></FONT></TD><TD WIDTH=15% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ curr_tc_name
				+ "</B></FONT></TD><TD WIDTH=15% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=GREEN SIZE=2><B>"
				+ tc_result
				+ "</B></FONT></TD><TD WIDTH=15% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ sh
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ tc_start_time
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ tc_end_time
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ timeDiff + "</B></FONT></TD></TR>");
	}

	/**
	 * @param curr_tc_name
	 * @param tc_result
	 * @param tc_start_time
	 * @param tc_end_time
	 * @param timeDiff
	 * @throws IOException
	 */
	public static void updateFailureHtmlReport(String curr_tc_name,
			String tc_result, String tc_start_time, String tc_end_time,
			String timeDiff,int sl_no,String sh) throws IOException {
		bw1.write("<TR><TD COLSPAN=1 BGCOLOR=WHITE><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ sl_no
				+ "</B></FONT></TD><TD WIDTH=25% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ curr_tc_name
				+ "</B></FONT></TD><TD WIDTH=15%  BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=RED SIZE=2><B>"
				+ tc_result
				+ "</B></FONT></TD><TD WIDTH=15% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ sh
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ tc_start_time
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ tc_end_time
				+ "</B></FONT></TD><TD WIDTH=10% BGCOLOR=WHITE WIDTH=100><FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>"
				+ timeDiff + "</B></FONT></TD></TR>");
	}

	/**
	 * @param testReport
	 * @throws IOException
	 */
	public static void htmlReportHeaders(String testReport) throws IOException {
		bw1 = new BufferedWriter(new FileWriter(testReport));
		bw1.write("<HTML><BODY><TABLE BORDER=0 CELLPADDING=1 CELLSPACING=1 WIDTH=45%>");
		bw1.write("<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=1 CELLSPACING=1 WIDTH=45%>");
		bw1.write("<Caption><FONT FACE=VERDANA COLOR=Black SIZE=2><B>Test Execution Summary Report</B></FONT></<Caption>");
		bw1.write("<TR><TD WIDTH=5% ALIGN=CENTER COLSPAN=0 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>S.No</B></FONT></TD>"
				+ "<TD WIDTH=15% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test Case</B></FONT></TD>"
				+ "<TD WIDTH=10% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test Result</B></FONT></TD>"
				+ "<TD WIDTH=15% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>SHEET NAME</B></FONT></TD>"
				+ "<TD WIDTH=10% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Start Time</B></FONT></TD>"
				+ "<TD WIDTH=10% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>End Time</B></FONT></TD>"
				+ "<TD WIDTH=10% ALIGN=CENTER BGCOLOR=#66699 WIDTH=100><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Duration</B></FONT></TD></TR>");
	}
	
	public static void closeReport() throws IOException {
		bw1.close();
		
	}

}
