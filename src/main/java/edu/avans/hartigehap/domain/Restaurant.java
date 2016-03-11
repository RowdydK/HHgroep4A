package edu.avans.hartigehap.domain;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * @author Erco
 */
@Entity
@Table(name = "RESTAURANTS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "menu", "diningTables", "customers" })
@NoArgsConstructor
public class Restaurant extends DomainObjectNaturalId {
    private static final long serialVersionUID = 1L;

    private String imageFileName;

    @ManyToMany(mappedBy="restaurants")
    private List<Owner> owners;

    // unidirectional one-to-one
    @OneToOne(cascade = CascadeType.ALL)
    private Menu menu = new Menu();

    @OneToOne(cascade = CascadeType.ALL)
    private Menu onlineMenu = new Menu();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private Collection<DiningTable> diningTables = new ArrayList<>();

    // no cascading
    @ManyToMany(mappedBy = "restaurants")
    private Collection<Customer> customers = new ArrayList<>();

    public Restaurant(String name, String imageFileName) {
        super(name);
        this.imageFileName = imageFileName;
    }
//
//    // business methods
//    public void warmup() {
//        Iterator<DiningTable> diningTableIterator = diningTables.iterator();
//        while (diningTableIterator.hasNext()) {
//            diningTableIterator.next().getId();
//        }
//
//        Iterator<MenuItem> mealsIterator = menu.getMeals().iterator();
//        while (mealsIterator.hasNext()) {
//            MenuItem mi = mealsIterator.next();
//            mi.getId();
//            Iterator<FoodCategory> fcIterator = mi.getFoodCategories().iterator();
//            while (fcIterator.hasNext()) {
//                fcIterator.next().getId();
//            }
//        }
//
//        Iterator<MenuItem> drinksIterator = menu.getDrinks().iterator();
//        while (drinksIterator.hasNext()) {
//            MenuItem mi = drinksIterator.next();
//            mi.getId();
//            Iterator<FoodCategory> fcIterator = mi.getFoodCategories().iterator();
//            while (fcIterator.hasNext()) {
//                fcIterator.next().getId();
//            }
//        }
//
//        Iterator<FoodCategory> foodCategoryIterator = menu.getFoodCategories().iterator();
//        while (foodCategoryIterator.hasNext()) {
//            FoodCategory fc = foodCategoryIterator.next();
//            Iterator<MenuItem> miIterator = fc.getMenuItems().iterator();
//            while (miIterator.hasNext()) {
//                miIterator.next().getId();
//            }
//        }
//
//        //OnlineMenuWarmup
//        Iterator<MenuItem> onlineMealsIterator = onlineMenu.getMeals().iterator();
//        while (onlineMealsIterator.hasNext()){
//            MenuItem mi = onlineMealsIterator.next();
//            mi.getId();
//            Iterator<FoodCategory> fcIterator = mi.getFoodCategories().iterator();
//            while (fcIterator.hasNext()){
//                fcIterator.next().getId();
//            }
//        }
//
//        Iterator<MenuItem> onlineMenuDrinksIterator = onlineMenu.getDrinks().iterator();
//        while (onlineMenuDrinksIterator.hasNext()){
//            MenuItem mi = onlineMenuDrinksIterator.next();
//            mi.getId();
//            Iterator<FoodCategory> fcIterator = mi.getFoodCategories().iterator();
//            while (fcIterator.hasNext()) {
//                fcIterator.next().getId();
//            }
//        }
//
//        Iterator<FoodCategory> foodCategoryOnlineMenuIterator = onlineMenu.getFoodCategories().iterator();
//        while (foodCategoryOnlineMenuIterator.hasNext()) {
//            FoodCategory fc = foodCategoryIterator.next();
//            if(fc.getId().equals(9) || fc.getId().equals(10)) {
//                Iterator<MenuItem> miIterator = fc.getMenuItems().iterator();
//                while (miIterator.hasNext()) {
//                    miIterator.next().getId();
//                }
//            }
//        }
//
//    }
}
