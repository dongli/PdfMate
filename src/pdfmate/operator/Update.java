package pdfmate.operator;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import pdfmate.ui.*;
import pdfmate.utils.*;

public class Update {
	public static void operate() {
		Pattern hashPattern = Pattern.compile("prev_commit_hash = \\w*");
		String urlBase = "https://raw.github.com/dongli/PdfMate/"+
						 "master/products/installer/payload/";
		String dirBase = System.getenv("HOME")+"/.pdfmate/";
		// ---------------------------------------------------------------------
		// get remote hash
		UI.notice("pdfmate", "Fetch information from remote repository.");
		String hashRemote = null;
		String content = SystemUtils.downloadAndRead(urlBase+"install.info");
		Matcher hashMatcher = hashPattern.matcher(content);
		if (hashMatcher.find())
			hashRemote = hashMatcher.group().split("=")[1];
		else
			UI.error("pdfmate", "Failed to find remote commit hash!");
		// ---------------------------------------------------------------------
		// get local hash
		String hashLocal = null;
		File file = new File(dirBase+"install.info");
		if (!file.isFile()) {
			SystemUtils.download(
					"https://raw.github.com/dongli/PdfMate/"+
					"master/products/installer/pdfmate.installer",
					"pdfmate.installer");
			UI.error("pdfmate", "There is no install.info in ~/.pdfmate. "+
					"New pdfmate.installer has been downloaded, "+
					"reinstall it please!");
		}
		try {
			content = new Scanner(file).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			UI.error("pdfmate", "Failed to find local commit hash!");
		}
		hashMatcher = hashPattern.matcher(content);
		if (hashMatcher.find())
			hashLocal = hashMatcher.group().split("=")[1];
		else
			UI.error("pdfmate", "Failed to find local commit hash!");
		// ---------------------------------------------------------------------
		// compare
		if (hashRemote.equals(hashLocal)) {
			UI.notice("pdfmate", "PdfMate is already up to date!");
		} else {
			UI.notice("pdfmate", "Update PdfMate.");
			String[] fileNames = {
				"pdfmate", "pdfmate.jar", "install.info", "setup.sh"
			};
			String[] existFileNames = (new File(dirBase)).list();
			for (String fileName : fileNames) {
				// check if there is any incomplete download file
				for (String existFileName : existFileNames)
					if (existFileName.matches(fileName+":.*"))
						if (!(new File(dirBase+existFileName)).delete())
							UI.error("pdfmate",
									"Failed to delete incomplete file "+
									existFileName+"!");
			}
			for (String fileName : fileNames) {
				UI.notice("pdfmate", "Download "+urlBase+fileName+".");
				SystemUtils.download(urlBase+fileName, dirBase+fileName);
			}
		}
	}
}
