
package com.gpr.apps.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

@Service
public class ResumeScorerSkills {

	@Autowired
	SkillService skillService;

    

	public static String readPdf(String filePath) throws IOException {
		try (PDDocument document = PDDocument.load(new File(filePath))) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			return pdfStripper.getText(document);
		}
	}

	public static String readDoc(String filePath) throws IOException {
		try (FileInputStream fis = new FileInputStream(filePath)) {
			HWPFDocument doc = new HWPFDocument(fis);
			WordExtractor extractor = new WordExtractor(doc);
			return extractor.getText();
		}

	}

	public static String readDocx(String filePath) throws IOException {
		try (FileInputStream fis = new FileInputStream(filePath)) {
			XWPFDocument docx = new XWPFDocument(fis);
			XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
			return extractor.getText();
		}
	}

	public static String getFileType(String filePath) {
		String extension = "";

		int i = filePath.lastIndexOf('.');
		if (i > 0) {
			extension = filePath.substring(i + 1);
		}
		return extension.toLowerCase();
	}

	public static String readFile(String filePath) throws IOException {
		String fileType = getFileType(filePath);
		switch (fileType) {
		case "pdf":
			return readPdf(filePath);
		case "doc":
			return readDoc(filePath);
		case "docx":
			return readDocx(filePath);
		default:
			throw new IllegalArgumentException("Unsupported file type: " + fileType);
		}
	}

	private static int countOccurrences(String text, String keyword) {
		int count = 0;
		int index = 0;
		while ((index = text.indexOf(keyword, index)) != -1) {
			count++;
			index += keyword.length();
		}
		return count;
	}

	private static String[] tokenizeText(Tokenizer tokenizer, String text) {
		return tokenizer.tokenize(text);
	}
}