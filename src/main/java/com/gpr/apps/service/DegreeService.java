package com.gpr.apps.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpr.apps.pojo.Degree;
import com.gpr.apps.repository.DegreeRepository;

@Service
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    public Optional<Degree> getDegreeById(Long id) {
        return degreeRepository.findById(id);
    }

    public Degree createDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    public Optional<Degree> updateDegree(Long id, Degree updatedDegree) {
        if (degreeRepository.existsById(id)) {
            updatedDegree.setId(id);
            return Optional.of(degreeRepository.save(updatedDegree));
        } else {
            return Optional.empty(); // Indicate that the degree was not found
        }
    }

    public void deleteDegree(long id) {
        try {
            degreeRepository.deleteById(id);;
        } catch (Exception e) {
            // Log the exception or handle it in some way
            e.printStackTrace();
            // Optionally, rethrow the exception or handle it differently
            // throw new RuntimeException("Failed to delete degree with ID: " + id, e);
        }
    }

	public boolean getDegreeByName(String edu) {
		// TODO Auto-generated method stub
		   List<Degree> degree = degreeRepository.findAll();;
	        
	         for (Degree degree1 : degree) {
	             if (degree1.getName().equals(edu)) {
	                 return true;
	             }
	         }
		return false;
	}


}
