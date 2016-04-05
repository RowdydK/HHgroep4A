package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@ToString(callSuper = true, includeFieldNames = true, of = { "menuItem", "quantity" })
@NoArgsConstructor
public class OrderItem extends DomainObject {
    private static final long serialVersionUID = 1L;

    // unidirectional many-to-one; deliberately no cascade
    @ManyToOne
    private MenuItem menuItem;
    
    @OneToMany
	private Collection<Ingredient> ingredients;

    private int quantity = 0;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }
    
    public OrderItem(OrderItemBuilder oib) {
    	this.menuItem = oib.menuItem;
    	this.quantity = oib.quantity;
    	this.ingredients = oib.ingredients;
    }
    
    public static class OrderItemBuilder {
		private MenuItem menuItem;
		private int quantity;
		private ArrayList<Ingredient> ingredients = new ArrayList<>();
		
		public OrderItemBuilder(MenuItem menuItem, int quantity){
			this.menuItem = menuItem;
			this.quantity = quantity;
		}
		
		public OrderItemBuilder ingredient(Ingredient ingredient){
			ingredients.add(ingredient);
			return this;
		}
		
		public OrderItemBuilder ingredients(ArrayList<Ingredient> ingredients){
			this.ingredients = ingredients;
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
}
