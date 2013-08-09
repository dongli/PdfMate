package pdfmate.operator;

import pdfmate.ui.*;

public class Master {
	public static void main(String[] args) {
		initCommandLine();
		CommandLine.parse(args);
		
		if (CommandLine.getOperatorName() == null) {
			if (CommandLine.hasOption("-v"))
				printVersion();
		} else if (CommandLine.getOperatorName().matches("extract-.*")) {
			Extract.operate();
		} else if (CommandLine.getOperatorName().matches("insert-.*")) {
			Insert.operate();
		} else if (CommandLine.getOperatorName().equals("help")) {
			printHelp();
		}
	}

	private static void printVersion() {
		String version = "1.0.0-alpha1";
		System.out.println(version);
	}
	
	private static void initCommandLine() {
		CommandLine.setCommand(
				"pdfmate",
				"@|bold PdfMate|@ is a tool for providing convenience when "+
				"reading PDF\nfiles.\n");
		CommandLine.addOption(null,
				"-v", "Print the version information", false, null);
		// ---------------------------------------------------------------------
		CommandLine.addOperator("extract-pages",
				"Extract pages from PDF file", true);
		CommandLine.addOperand("extract-pages",
				"Page range, e.g., 1-10", true, "all", "all the pages");
		// ---------------------------------------------------------------------
		CommandLine.addOperator("insert-toc",
				"Insert table of contents into PDF file", true);
		CommandLine.addOperand("insert-toc",
				"Text file contains contents information", false, null, null);
		CommandLine.addOption("insert-toc",
				"--toc", "Specify the TOC txt file", true, "TOC file");
		// ---------------------------------------------------------------------
		CommandLine.addOperator("help",
				"Print help information or usage.", true);
		CommandLine.addOperand("help",
				"Operator to help",
				true, "all", "all help");
	}
	
	private static void printHelp() {
		String header =
				"@|green "+
				" _____   _ ___ _____     _       \n" + 
				"|  _  |_| |  _|     |___| |_ ___ \n" + 
				"|   __| . |  _| | | | .'|  _| -_|\n" + 
				"|__|  |___|_| |_|_|_|__,|_| |___|\n" + 
				"|@\n";
		
		CommandLine.printUsage(header, CommandLine.getOperandValue());
	}
}
