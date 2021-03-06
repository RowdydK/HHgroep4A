package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Student on 22-03-16.
 */
public class MockClasses {

    public Bill SetupBill(){

        Bill bill = new Bill();

        FoodCategory foodCat = new FoodCategory("vegetarisch");
        FoodCategory foodCat2 = new FoodCategory("pizza");

        ArrayList<FoodCategory> foodCats1 = new ArrayList<FoodCategory>();
        ArrayList<FoodCategory> foodCats2 = new ArrayList<FoodCategory>();
        foodCats1.add(foodCat);
        foodCats2.add(foodCat2);

        Meal meal1 = new Meal("spaghetti", "spaghetti.jpg", 10, "lekkerman");
        meal1.addFoodCategories(foodCats1);
        Meal meal2 = new Meal("pizza", "pizza.jpg", 9, "facking goud");
        meal2.addFoodCategories(foodCats2);
        Meal meal3 = new Meal("pizzaatje", "pizza.jpg", 9, "superlekker");
        meal3.addFoodCategories(foodCats2);
        Meal meal4 = new Meal("pasta", "macaroni.jpg", 10, "lekkerman");
        meal4.addFoodCategories(foodCats1);
        Meal meal5 = new Meal("pizzatonijn", "pizza.jpg", 9, "facking goud");
        meal5.addFoodCategories(foodCats2);
        Meal meal6 = new Meal("pizzahamas", "pizza.jpg", 9, "superlekker");
        meal6.addFoodCategories(foodCats2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setMenuItem(meal1);
        orderItem1.incrementQuantity();
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setMenuItem(meal2);
        orderItem2.incrementQuantity();
        OrderItem orderItem3 = new OrderItem();
        orderItem3.setMenuItem(meal3);
        orderItem3.incrementQuantity();
        OrderItem orderItem4 = new OrderItem();
        orderItem4.setMenuItem(meal4);
        orderItem4.incrementQuantity();
        OrderItem orderItem5 = new OrderItem();
        orderItem5.setMenuItem(meal5);
        orderItem5.incrementQuantity();
        OrderItem orderItem6 = new OrderItem();
        orderItem6.setMenuItem(meal6);
        orderItem6.incrementQuantity();


        Collection<Order> orders = new ArrayList<>();
        Collection<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(orderItem1);
        orderItems1.add(orderItem2);
        orderItems1.add(orderItem3);
        Collection<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(orderItem4);
        orderItems2.add(orderItem5);
        orderItems2.add(orderItem6);

        Order order1 = new Order();
        Order order2 = new Order();

        order1.setOrderItems(orderItems1);
        order2.setOrderItems(orderItems2);

        orders.add(order1);
        orders.add(order2);

        bill.setOrders(orders);

        return bill;

    }
}
