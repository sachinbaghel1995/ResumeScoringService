package com.gpr.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gpr.apps.pojo.Degree;

public interface DegreeRepository extends JpaRepository<Degree, Long> {

	
}