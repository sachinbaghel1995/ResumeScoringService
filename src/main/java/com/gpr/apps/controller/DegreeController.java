package com.gpr.apps.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpr.apps.pojo.Degree;
import com.gpr.apps.service.DegreeService;
import com.gpr.apps.service.ResumeScorerSkills;
import com.gpr.apps.service.SkillService;

import opennlp.tools.tokenize.SimpleTokenizer;

@RestController
@RequestMapping("/")
public class DegreeController {
	int count;
	String education = "btech";
 
    @Autowired
    private DegreeService degreeService;
    
    @Autowired
    ResumeScorerSkills resumeScorerSkills;

    
    @Autowired
    SkillService skillService;
    
    @GetMapping
    public List<Degree> getAllDegrees() {
    	return degreeService.getAllDegrees();
    }

    @GetMapping("/degree/{degree1}")
    public int getDegree(@PathVariable("degree1") String degree1) {
         List<Degree> degree = degreeService.getAllDegrees();
        
         for (Degree degrees : degree) {
             if (degrees.getName().equals(degree1)) {
            	 count=10;
             }
         }
       return count;
   }

    @GetMapping("/count/{skill}")
    public int getCount(@PathVariable("skill") String skill) {
        System.out.println("Received request for skill: " + skill);
        
        String filePath = "C:\\Users\\baghe\\Downloads\\Resume_sachinb_final_1704474630.pdf";
        String resume = null;
        try {
            resume = resumeScorerSkills.readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        System.out.println(resume);

        // Get skills input from the user
        String[] skills = skill.split(",");

        // Get the SkillService bean from the application context

        int score = skillService.scoreResume(tokenizer, resume.toLowerCase(), skills);
        System.out.println("Resume score: " + score);

        return score;
    }


    @PostMapping
    public Degree createDegree(@RequestBody Degree degree) {
        return degreeService.createDegree(degree);
    }

    @PutMapping("/update/{id}")
    public Optional<Degree> updateDegree(@PathVariable Long id, @RequestBody Degree degree) {
        return degreeService.updateDegree(id, degree);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDegree(@PathVariable("id") int id) {
        degreeService.deleteDegree(id);
    }

    
}
