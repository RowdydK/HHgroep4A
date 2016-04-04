package edu.avans.hartigehap.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class OrderDecorator {
	

	public void submit(){
		
	}
	
	public void plan(){
		
	}
	
	public void prepared(){
		
	}
	
	public void served(){
		
	}
	
}
