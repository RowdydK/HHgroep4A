package edu.avans.hartigehap.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemIngredient extends DomainObject implements Cloneable {
	
	@OneToOne
	private Ingredient ingredient;
	private int quantity;
	
	public OrderItemIngredient(Ingredient ingredient){
		this.ingredient = ingredient;
		quantity+=1;
	}
	
	public void incrementQuantity() {
        this.quantity++;
    }
	
	@Override
    public OrderItemIngredient clone() {
		try {
			return (OrderItemIngredient) super.clone();
		} catch (CloneNotSupportedException e) {		
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
