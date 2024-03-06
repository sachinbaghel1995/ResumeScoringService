package com.gpr.apps.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

//@SpringBootApplication
public class ResumeScorerApplication2 {
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
    public static void main(String[] args) throws IOException {
    //    SpringApplication.run(ResumeScorerApplication.class, args);
        String filePath = "C:\\Users\\baghe\\Downloads\\Resume_sachinb_final_1704474630.pdf";
        String resume = readFile(filePath);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
      
    //    String resume = "John Doe has a degree in Civil Engineering. He has experience in Java programming and web development.";
        System.out.println(resume);
        String jobDescription = " You'll be responsible for crafting responsive user interfaces, collaborating closely with designers and backend developers to deliver seamless user experiences. Proficiency in Reactjs, JavaScript, HTML5, and CSS3";
        int score = scoreResume(tokenizer, resume.toLowerCase(), jobDescription.toLowerCase());
        System.out.println("Resume score: " + score);
    }

    public static int scoreResume(Tokenizer tokenizer, String resume, String jobDescription) {
        Map<String, Integer> keywordFreqInResume = new HashMap<>();

        // Tokenize resume and job description
        String[] resumeTokens = tokenizeText(tokenizer, resume);
        String[] jobDescriptionTokens = tokenizeText(tokenizer, jobDescription);

        // Count the frequency of each token in the resume
        for (String token : resumeTokens) {
            keywordFreqInResume.put(token, keywordFreqInResume.getOrDefault(token, 0) + 1);
        }

        // Calculate the score based on the frequency of tokens in the job description
        int score = 0;
        for (String keyword : jobDescriptionTokens) {
            // Count occurrences of the keyword in the resume
            int keywordCountInResume = countOccurrences(resume.toLowerCase(), keyword);
            // Increment score based on the count (limiting to 10)
            score += Math.min(keywordCountInResume, 10);
        }

        // Normalize the score between 1 and 10
        int maxScore = jobDescriptionTokens.length * 10;
        return (int) Math.ceil(((double) score / maxScore) * 10);
    }

    // Helper method to count occurrences of a substring within a string
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
