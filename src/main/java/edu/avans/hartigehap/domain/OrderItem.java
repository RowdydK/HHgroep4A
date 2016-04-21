package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Erco
 */
@Entity
@Table(name = "ORDERITEMS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "menuItem", "ingredients" })
@NoArgsConstructor
public class OrderItem extends DomainObject implements Cloneable{
    private static final long serialVersionUID = 1L;

    // unidirectional many-to-one; deliberately no cascade
    @ManyToOne
    private MenuItem menuItem;
    
    @ManyToMany(cascade = javax.persistence.CascadeType.ALL)
	private Collection<OrderItemIngredient> ingredients = new ArrayList<>();
    
    private int quantity = 0;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }
    
    private OrderItem(OrderItemBuilder oib) {
    	this.menuItem = oib.menuItem;
    	this.quantity = oib.quantity;
	    if(oib.getBuilderIngredients() != null){
	    	ingredients = new ArrayList<>();
	    	for (OrderItemIngredient oii : oib.getBuilderIngredients()){
	    		ingredients.add(oii);
	    	}
	    }
    }
    
    @Getter
    public static class OrderItemBuilder {
		private MenuItem menuItem;
		private int quantity;
		@Transient
		private Collection<OrderItemIngredient> builderIngredients = new ArrayList<>();
		
		public OrderItemBuilder(MenuItem menuItem, int quantity){
			this.menuItem = menuItem;
			this.quantity = quantity;
		}
		
		public OrderItemBuilder(OrderItem orderItem){
	    	this.menuItem = orderItem.menuItem;
	    	this.quantity = orderItem.quantity;
	    	this.builderIngredients = orderItem.getIngredients();
			
		}
		
		public OrderItemBuilder ingredient(Ingredient ingredient){
			boolean containsIngredient = false;
			for(OrderItemIngredient i : builderIngredients){
				if(i.getIngredient().equals(ingredient)){
					containsIngredient = true;
					i.incrementQuantity();
				}
			}
			if(!containsIngredient){
				OrderItemIngredient oii = new OrderItemIngredient(ingredient);
				builderIngredients.add(oii);
			}
			return this;
		}

		@Transient
		public OrderItemBuilder ingredients(ArrayList<OrderItemIngredient> ingredients){
			this.builderIngredients = ingredients;
			return this;
		}
		
		public OrderItem build(){
			return new OrderItem(this);
		}
	}

    /* business logic */

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        assert quantity > 0 : "quantity cannot be below 0";
        this.quantity--;
    }

    @Transient
    public int getPrice() {
        return menuItem.getPrice() * quantity;
    }
    
    @Override
    public OrderItem clone() {
		try {
			OrderItem clone = (OrderItem) super.clone();
			Collection<OrderItemIngredient> thisIngredients = ingredients;
			ArrayList<OrderItemIngredient> cloneIngredients = new ArrayList<>();
			for(OrderItemIngredient orderItem : thisIngredients){
				cloneIngredients.add(orderItem.clone());
			}
			clone.setIngredients(cloneIngredients);
			return clone;
		} catch (CloneNotSupportedException e) {		
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
