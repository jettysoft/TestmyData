/**
 * 
 */
package com.testmydata.util;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
//import com.testmydata.Reports;
import com.testmydata.main.UserDashboard;

public class FileIOOperations {

	public FileIOOperations() {

	}

	public boolean createPropertyFile(HashMap<String, String> fileDetails, String fileName) {
		try {
			Properties props = new Properties();
			for (Entry<String, String> entry : fileDetails.entrySet()) {
				props.setProperty(entry.getKey(), entry.getValue());
			}

			File f = new File(new File(".", "/conf" + File.separator + fileName).getAbsolutePath());
			OutputStream out = new FileOutputStream(f);
			props.store(out, "This is an optional header comment string");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public Properties readPropertyFile(String fileName) {

		Properties props = new Properties();
		InputStream is = null;

		// First try loading from the current directory
		try {
			File f = new File(new File(".", "/conf" + File.separator + fileName).getAbsolutePath());
			is = new FileInputStream(f);
		} catch (Exception e) {
			is = null;
		}

		try {
			if (is == null) {
				// Try loading from classpath
				is = getClass().getResourceAsStream(fileName);
			}
			// Try loading properties from the file (if found)
			props.load(is);
			is.close();
		} catch (Exception e) {
		}
		return props;
	}

	/**
	 * saveFile - Utility to open the file chooser to select the location to
	 * store the file in the system.
	 * 
	 * @param uBoard
	 * @return string - file location to be stored.
	 */
	public static String saveFile(UserDashboard uBoard) {
		File fileToSave = null;
		String filePath = null;

		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Choose a directory to save");
			// fileChooser.setCurrentDirectory(new java.io.File("."));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MS Office Documents", "doc", "docx"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
			fileChooser.setAcceptAllFileFilterUsed(false);

			int userSelection = fileChooser.showSaveDialog(uBoard);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				fileToSave = fileChooser.getSelectedFile();

				filePath = fileToSave.getAbsolutePath();

				String[] extn = fileToSave.getName().split(".");

				if (extn.length <= 1) {
					filePath = filePath + ".pdf";
				}
			} else {
				filePath = "notSelected";
			}

		} catch (HeadlessException e) {
			filePath = "error";
		}

		return filePath;
	}

	public static String openFile(UserDashboard uBoard) {
		File fileToSave = null;
		String filePath = null;
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to open");
			// fileChooser.setCurrentDirectory(new java.io.File("."));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MS Office Documents", "doc", "docx"));
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
			fileChooser.setAcceptAllFileFilterUsed(false);

			int userSelection = fileChooser.showOpenDialog(uBoard);

			if (userSelection == JFileChooser.APPROVE_OPTION) {
				fileToSave = fileChooser.getSelectedFile();
				filePath = fileToSave.getAbsolutePath();
			} else {
				filePath = "notSelected";
			}
		} catch (HeadlessException e) {
			filePath = "error";
		}

		return filePath;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public static void createExpenseReport(String filename, HashMap<String, String> details) {

		try {
			if (!(details.size() > 0)) {
				JOptionPane.showMessageDialog(null, "Empty Document!", "Printing Status", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Document document = new Document(PageSize.LETTER);
			PdfWriter pdfWriter = PdfWriter.getInstance(document,
					new FileOutputStream("conf" + File.separator + filename + ".pdf"));
			document.open();
			HTMLWorker htmlWorker = new HTMLWorker(document);
			StringBuffer str = new StringBuffer();
			str.append("<html><head></head><body>");
			str.append("<h1>Expense reports</h1>");
			str.append("<P><br>");
			str.append("<TABLE BORDER=1>");
			str.append("<TR>");
			str.append("<TD><B>Components</B></TD>");
			str.append("<TD><B>values</B></TD>");
			Iterator iterate = details.keySet().iterator();
			while (iterate.hasNext()) {
				String key = (String) iterate.next();
				str.append("<TR>");
				str.append("<TD>" + key + "</TD>");
				str.append("<TD>" + details.get(key) + "</TD>");
				str.append("<TR>");
			}
			str.append("</TABLE>");
			str.append(" </body></html>");
			htmlWorker.parse(new StringReader(str.toString()));
			document.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static boolean checkDirectoryExists(String rootDir, String subDir, File path) {

		try {
			File f = new File(path + File.separator + rootDir);
			if (f.isDirectory()) {
				File ff = new File(path + File.separator + rootDir + File.separator + subDir);
				if (ff.isDirectory()) {
					return true;
				} else {
					ff.mkdirs();
					return true;
				}
			} else {
				f.mkdir();
				File ff = new File(path + File.separator + rootDir + File.separator + subDir);
				ff.mkdirs();
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	public static String uploadDocuments(UserDashboard uBoard, String expenseName, String projectName) {
		String returnValue = "failure";

		try {
			String fileFullName = openFile(uBoard);

			if (fileFullName == "notSelected") {
				returnValue = fileFullName;
			} else if (fileFullName == "error") {
				returnValue = fileFullName;
			} else {
				File srcFile = new File(fileFullName);
				FileInputStream inStream = new FileInputStream(srcFile);

				File fPath = new File(".");
				File path = new File(fPath.getCanonicalPath() + File.separator + "ExpenseReport");
				if (!(path.isDirectory())) {
					path.mkdir();
				}

				checkDirectoryExists(projectName, expenseName, path);

				FileOutputStream outStream = new FileOutputStream(path + File.separator + projectName + File.separator
						+ expenseName + File.separator + srcFile.getName());

				byte[] buffer = new byte[1024];

				int length;
				// copy the file content in bytes
				while ((length = inStream.read(buffer)) > 0) {

					outStream.write(buffer, 0, length);

				}
				inStream.close();
				outStream.close();

				returnValue = "success";

			}
		} catch (Exception e) {
			returnValue = "failure";
		}

		return returnValue;
	}
}
