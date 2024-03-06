package com.gpr.apps.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Skill {
    @Id
    private Long id;
    private String name;
    private String relevant1;
    private String relevant2;
    private String relevant3;
    private String relevant4;
    private String relevant5;
    
    public String getRelevant1() {
        return relevant1;
    }
    public String getRelevant2() {
        return relevant2;
    }
    
    public String getRelevant3() {
        return relevant3;
    }
    public String getRelevant4() {
        return relevant4;
    }
    
    public String getRelevant5() {
        return relevant5;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRelevant1(String relevant1) {
		this.relevant1 = relevant1;
	}
	public void setRelevant2(String relevant2) {
		this.relevant2 = relevant2;
	}
	public void setRelevant3(String relevant3) {
		this.relevant3 = relevant3;
	}
	public void setRelevant4(String relevant4) {
		this.relevant4 = relevant4;
	}
	public void setRelevant5(String relevant5) {
		this.relevant5 = relevant5;
	}
	@Override
	public String toString() {
		return "Skill [id=" + id + ", name=" + name + ", relevant1=" + relevant1 + ", relevant2=" + relevant2
				+ ", relevant3=" + relevant3 + ", relevant4=" + relevant4 + ", relevant5=" + relevant5 + "]";
	}
    

    
}

