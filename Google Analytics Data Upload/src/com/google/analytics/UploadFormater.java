package com.google.analytics;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UploadFormater {

	static final int BUFFER = 512;
	static final int TOOBIG = 0x6400000; // max size of unzipped data, 100MB

	ZipEntry entry;
	int entries = 0;
	long total = 0;

	String src;
	String mdm;
	
	/**
	 * @return returns a string formatted from a bing keyword performance report into the Google Cost Data upload format
	 * @param http location of a zipped bing keyword performance report 
	 * @throws IOException
	 */
	public String unzipAndFormatData(String newUrl)
			throws IOException {
		setSrcMdm("\"bing\"", "\"cpc\"");
		URL url = new URL(newUrl);
		URLConnection connection = url.openConnection();
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
				connection.getInputStream()));
		String formattedData = formatData(zis);
		return formattedData;
	}
	
	/**
	 * setters for source and medium
	 * @param source expected by Google Analytics, case sensitive
	 * @param medium expected by Google Analytics, case sensitive
	 * @throws IOException
	 */
	public void setSrcMdm(String src, String mdm) {
		this.src = src;
		this.mdm = mdm;
	}

	/**
	 * loops through each zip entry file
	 * @return returns a string formatted from a bing keyword performance report into the Google Cost Data upload format
	 * @param zip file input stream from a bing keyword performance report
	 * @throws IOException
	 */
	public String formatData(ZipInputStream zis) throws IOException {
		String formattedData = null;
		try {
			while ((entry = zis.getNextEntry()) != null) {
				formattedData = zipDataToString(zis);
			}
		} finally {
			zis.close();
		}
		return formattedData;
	}

	/**
	 * @return a string of the zipped data
	 * @param zip file input stream from a bing keyword performance report
	 */
	public String zipDataToString(ZipInputStream zis) {
		String formattedData = null;
		try {
			String s = byteArrayToString(zis);
			String[] rows = s.split("\\r?\\n");
			List<String> uploadList = buildUploadList(rows);
			formattedData = replaceInvalidChars(uploadList);
			zis.closeEntry();
			entries++;
			if (total > TOOBIG) {
				throw new IllegalStateException(
						"File being unzipped is too big.");
			}
		} catch (Exception IO) {
			System.out.println("cant select file");
		}
		return formattedData;

	}

	/**
	 * takes a zip input stream, stores in a byte array, converts it to the correct formatted structre
	 * @return a string encoded in UTF-8
	 * @param zip file input stream
	 * @throws IOException
	 */
	public String byteArrayToString(ZipInputStream zis) throws IOException {
		int count;
		byte data[] = new byte[BUFFER];
		ByteArrayOutputStream dest = new ByteArrayOutputStream(BUFFER);
		while (total + BUFFER <= TOOBIG
				&& (count = zis.read(data, 0, BUFFER)) != -1) {
			dest.write(data, 0, count);
			total += count;
		}
		String s = new String(dest.toByteArray(), "UTF-8");
		dest.flush();
		dest.close();
		return s;
	}

	/**
	 * 
	 * @return formated data list
	 * @param array of data rows
	 */
	public List<String> buildUploadList(String[] rows) {
		List<String> uploadList = new ArrayList<String>();
		String headerRow = setHeaders();
		uploadList.add(headerRow);
		for (int x = 0; x < rows.length; x = x + 1) {
			if (x > 11) {
				try {
					String[] col = rows[x].split(",");
					if (col.length > 2) {
						String uploadRow = setRow(col);
						uploadList.add(uploadRow);
					}
				} catch (Exception IO) {
					System.out.println("cannot split the line");
				}
			}
		}
		return uploadList;
	}

	/**
	 * 
	 * @return returns a string representation of the object with invalid chars removed
	 * @param data String
	 */
	public String replaceInvalidChars(List<String> data) {
		String objectString = data.toString();
		objectString = removeTrailingCommas(objectString);
		return objectString;
	}

	/**
	 * 
	 * @return returns a copy of the string with any ($) with a new line (\n)
	 * @param data String
	 */
	public String removeTrailingCommas(String data) {
		String objectString = data.replace("$, ", "\n");
		objectString = removeOpenBracket(objectString);
		return objectString;
	}

	/**
	 * 
	 * @return returns a copy of the string with any ([) with a new line (\n)
	 * @param data String
	 */
	public String removeOpenBracket(String data) {
		String objectString = data.replace("[", "\n");
		objectString = removeClosingBracket(objectString);
		return objectString;
	}

	/**
	 * 
	 * @return returns a copy of the string with any ($]) with a new line (\n)
	 * @param data String
	 */
	public String removeClosingBracket(String data) {
		String objectString = data.replace("$]", "\n");
		objectString = removeTrailingBracket(objectString);
		return objectString;
	}

	/**
	 * 
	 * @return returns a copy of the string with any opening square brackets ([) with a new line (\n)
	 * @param data String
	 */
	public String removeTrailingBracket(String data) {
		String objectString = data.replace("[", "\n");
		objectString = trimSpaces(objectString);
		return objectString;
	}

	/**
	 * 
	 * @return returns a copy of the string, with leading and trailing whitespace omitted
	 * @param data String
	 */
	public String trimSpaces(String data) {
		String objectString = data.trim();
		return objectString;
	}

	/**
	 * 
	 * @return formated row String
	 * @param row of data in a String array
	 * @throws ParseException
	 */
	public String setRow(String[] col) {
		String formattedDate = null;
		formattedDate = formatDate(col[0]);
		String uploadRow = formattedDate + "," + src + "," + mdm + "," + col[3]
				+ "," + col[8] + "," + col[7] + "," + col[9] + "$";
		return uploadRow;
	}

	/**
	 * 
	 * @return formatted date string (yyyyMMdd)
	 * @param the date field of the parsed bing keyword performance data array string (MM/dd/yyyy)
	 * @throws ParseException
	 */
	private String formatDate(String oldDate) {
		String removeSpMarks = oldDate.replace("\"", "");
		SimpleDateFormat setDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date dateFormat = null;
		try {
			dateFormat = setDateFormat.parse(removeSpMarks);
		} catch (ParseException e) {
			System.err.println("couldn't format date");
			e.printStackTrace();
		}
		SimpleDateFormat reformatDate = new SimpleDateFormat("yyyyMMdd");
		String newDate = reformatDate.format(dateFormat);
		return newDate;
	}

	/**
	 * 
	 * @return returns the header row as a string
	 */
	public String setHeaders() {
		String headerRow = "\"ga:date\",\"ga:source\",\"ga:medium\",\"ga:campaign\",\"ga:impressions\",\"ga:adClicks\",\"ga:adCost\"$";
		return headerRow;
	}
}
