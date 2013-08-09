package pdfmate.operator;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import pdfmate.ui.*;

public class Insert {
	public static void operate() {
		if (CommandLine.getOperatorName().equals("insert-toc")) {
			insertToc();
		}
	}
	
	private static void insertToc() {
		String oldPdf = CommandLine.getOperandValue();
		String newPdf = oldPdf+".new.pdf";
		// copy to a new PDF file
		UI.notice("pdfmate", "Create a new PDF file "+newPdf+".");
		Document document = new Document();
		PdfCopy copy = null;
		try {
			PdfReader reader = new PdfReader(oldPdf);
			copy = new PdfCopy(document, new FileOutputStream(newPdf));
			document.open();
			for (int i = 1; i <= reader.getNumberOfPages(); ++i)
				copy.addPage(copy.getImportedPage(reader, i));
		} catch (Exception e1) {
			document.close();
			// TODO Auto-generated catch block
			e1.printStackTrace();
			UI.error("pdfmate", "Encounter error while copying PDF file!");
		}
		// create the TOC
		UI.notice("pdfmate", "Insert TOC into this PDF.");
		String tocFile = CommandLine.getOptionValue("--toc");
		try {
			PdfOutline outlineRoot = copy.getDirectContent().getRootOutline();
			Stack<PdfOutline> outlineStack = new Stack<PdfOutline>();
			FileInputStream inputStream = new FileInputStream(tocFile);
			String encoding = "UTF-8";
			if (CommandLine.hasOption("--encoding")) {
				encoding = CommandLine.getOptionValue("--encoding");
			}
			if (!Charset.isSupported(encoding))
				UI.error("pdfmate",
						"When reading TOC text file, encoding "+
						encoding+" is not supported!");
			BufferedReader inputBuffer = new BufferedReader(
					new InputStreamReader(inputStream,
							Charset.forName(encoding)));
			String line = null;
			int lastLevel = -1;
			while ((line = inputBuffer.readLine()) != null) {
				if (!line.matches("(^ *#.*|^\\s*$)")) {
					String[] tmp = splitTocEntry(line);
					String hierLevel = tmp[0];
					String title = tmp[1];
					Integer pageNumber = new Integer(tmp[2]);
					// determine the hierarchy level
					Pattern dotPattern = Pattern.compile("\\.");
					Matcher matcher = dotPattern.matcher(hierLevel);
					int level = 0;
					while (matcher.find()) {
						level++;
					}
					// set outline
					PdfOutline outline = null;
					if (lastLevel == -1) {
						outlineStack.push(outlineRoot);
					} else if (lastLevel >= level) {
						for (int i = level; i <= lastLevel; ++i)
							outlineStack.pop();
					}
					if (hierLevel.matches("^\\.+$"))
						hierLevel = "";
					outline = new PdfOutline(
							outlineStack.lastElement(),
							PdfAction.gotoLocalPage(pageNumber,
									new PdfDestination(PdfDestination.FIT),
									copy),
							hierLevel+" "+title);
					outlineStack.push(outline);
					lastLevel = level;
				}
			}
			inputBuffer.close();
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			UI.error("pdfmate", "Encounter error while inserting TOC!");
		}
	}
	
	private static String[] splitTocEntry(String entry) {
		String[] res = {"", "", ""};
		int i = 0;
		while (i < entry.length()) {
			if (entry.substring(i, i+1).matches("\\d|\\.")) {
				while (entry.charAt(i) != ' ')
					res[0] += entry.charAt(i++);
				break;
			}
			i++;
		}
		int j = entry.length()-1;
		while (j >= 0) {
			if (entry.substring(j, j+1).matches("\\d")) {
				while (entry.charAt(j) != ' ')
					j--;
				break;
			}
			j--;
		}
		res[2] = entry.substring(j+1, entry.length());
		res[1] = entry.substring(i, j+1).replaceAll("(^ *| *$)", "");
		return res;
	}
}
