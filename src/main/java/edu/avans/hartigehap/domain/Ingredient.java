package edu.avans.hartigehap.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@ToString(callSuper = true, includeFieldNames = true, of = { "ingredientName", "quantity" })
public class Ingredient extends DomainObject {

	private static final long serialVersionUID = 1L;
	
	private int quantity;
	private String ingredientName;
	
	@OneToOne
	private FoodCategory foodCategory;
	
	public Ingredient(String ingredientName, int quantity){
		this.ingredientName = ingredientName;
		this.quantity = quantity;
	}
}
