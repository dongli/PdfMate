package pdfmate.operator;

import java.io.IOException;
import java.util.*;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;

import pdfmate.ui.*;
import pdfmate.utils.SystemUtils;

public class Extract {
	public static void operate() {
		if (CommandLine.getOperatorName().equals("extract-pages")) {
			extractPages();
		}
	}
	
	private static void extractPages() {
		// get the page numbers
		UI.notice("pdfmate", "Input the page range or list:");
		String pageRange = UI.getAnswer(null)[0];
		List<Integer> pageNumbers = new ArrayList<Integer>();
		if (pageRange.matches("\\d+-\\d+")) {
			Integer startPageNumber =
					new Integer(pageRange.substring(0, pageRange.indexOf('-')));
			Integer endPageNumber =
					new Integer(pageRange.substring(pageRange.indexOf('-')+1));
			for (Integer i = startPageNumber; i <= endPageNumber; ++i )
				pageNumbers.add(i);
		} else if (pageRange.matches("(\\d+,)+")) {
			String[] tmp = pageRange.split(",");
			for (int i = 0; i < tmp.length; ++i)
				pageNumbers.add(new Integer(tmp[i]));
		} else if (pageRange.matches("^\\d+$")) {
			pageNumbers.add(new Integer(pageRange));
		}
		// extract the content of each selected pages
		String filePath = CommandLine.getOperandValue();
		PdfReader reader;
		try {
			reader = new PdfReader(filePath);
			for (Integer pageNumber : pageNumbers) {
				String content = PdfTextExtractor
						.getTextFromPage(reader, pageNumber);
				SystemUtils.print(
						"@|bold --------------------------------------------- PAGE "+pageNumber+
						       " ---------------------------------------------|@\n");
				System.out.println(content);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
