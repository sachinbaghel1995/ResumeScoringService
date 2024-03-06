package com.gpr.apps.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpr.apps.pojo.Skill;
import com.gpr.apps.repository.SkillRepository;

import opennlp.tools.tokenize.Tokenizer;

@Service
public class SkillService {

    @Autowired
    SkillRepository skillRepository;
    
    public int scoreResume(Tokenizer tokenizer, String resume, String[] skills) {
    	String cleanedResume = replaceSpecialCharacters(resume);
        int totalRelevanceScore = 0;
        int maxPossibleRelevanceScore = skills.length * 5; // Assuming each skill has 5 relevant skillsS
        

        for (String skill : skills) {
            int relevanceScore = 0;

            Skill skillEntity = skillRepository.findByName(skill.trim());
            if (skillEntity != null) {
                if (cleanedResume != null && !cleanedResume.isEmpty()) {
                    // Check each relevant skill for relevance in the resume
                    for (int i = 1; i <= 5; i++) {
                        String relevantSkill = "";
                        switch (i) {
                            case 1:
                                relevantSkill = skillEntity.getRelevant1();
                                break;
                            case 2:
                                relevantSkill = skillEntity.getRelevant2();
                                break;
                            case 3:
                                relevantSkill = skillEntity.getRelevant3();
                                break;
                            case 4:
                                relevantSkill = skillEntity.getRelevant4();
                                break;
                            case 5:
                                relevantSkill = skillEntity.getRelevant5();
                                break;
                        }
                        if (relevantSkill != null && !relevantSkill.isEmpty()) {
                            if (cleanedResume.toLowerCase().contains(relevantSkill.toLowerCase())) {
                                relevanceScore++;
                            }
                        }
                    }
                }
            }

            totalRelevanceScore += relevanceScore;
        }

        // Normalize and return the relevance score
        int normalizedScore = (int) Math.ceil(((double) totalRelevanceScore / maxPossibleRelevanceScore) * 10);
        return Math.min(Math.max(normalizedScore, 1), 10);
    }


    private String replaceSpecialCharacters(String text) {
        // Define a regex pattern to match special characters
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        // Replace special characters with an empty string
        return pattern.matcher(text).replaceAll("");
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
