package com.gpr.apps.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class ResumeScorerSkills2 {

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
    // Other methods remain unchanged

    public static void main(String[] args) throws IOException, SQLException {
        // SpringApplication.run(ResumeScorerApplication.class, args);
        String filePath = "C:\\Users\\baghe\\Downloads\\Resume_sachinb_final_1704474630.pdf";
        String resume = readFile(filePath);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        System.out.println(resume);

        // Get skills input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter skills (comma-separated): ");
        String skillsInput = scanner.nextLine();
        String[] skills = skillsInput.split(",");

        // Establish database connection
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resumeparser", "root", "sachin@1995")) {
            int score = scoreResume(tokenizer, resume.toLowerCase(), skills, conn);
            System.out.println("Resume score: " + score);
        }
    }



    public static int scoreResume(Tokenizer tokenizer, String resume, String[] skills, Connection conn) throws SQLException {
        int totalRelevanceScore = 0;
        int maxPossibleRelevanceScore = skills.length * 5; // Assuming each skill has 5 relevant skills

        for (String skill : skills) {
            int relevanceScore = 0;

            try (PreparedStatement stmt = conn.prepareStatement("SELECT relevant1, relevant2, relevant3, relevant4, relevant5 FROM skill WHERE name = ?")) {
                stmt.setString(1, skill.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        for (int i = 1; i <= 5; i++) {
                            String relevantSkill = rs.getString(i);
                            if (relevantSkill != null && !relevantSkill.isEmpty()) {
                                if (resume.toLowerCase().contains(relevantSkill.toLowerCase())) {
                                    relevanceScore++;
                                }
                            }
                        }
                    }
                }
            }

            totalRelevanceScore += relevanceScore;
        }

        int normalizedScore = (int) Math.ceil(((double) totalRelevanceScore / maxPossibleRelevanceScore) * 10);
        return Math.min(Math.max(normalizedScore, 1), 10);
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

    // Other methods remain unchanged
}

 