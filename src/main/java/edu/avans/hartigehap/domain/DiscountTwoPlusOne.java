package edu.avans.hartigehap.domain;

import java.util.ArrayList;

/**
 * Created by Student on 21-03-16.
 */
public class DiscountTwoPlusOne extends DiscountStrategy {

    public int CalculateDiscount(ArrayList<MenuItem> menuItems, Bill bill){
        int priceToPay = 0;
        int numberOfPizzas = menuItems.size();
        int index = 0;

        while(numberOfPizzas>=3){
            menuItems.remove(menuItems.size()-1);

            numberOfPizzas-=3;
        }

        for(MenuItem mi : menuItems){
            priceToPay+=mi.getPrice();
        }

        return priceToPay;
//
//
//        if(menuItems.size() <= 3 && menuItems.size() > 2) {
//            MenuItem menuItem = menuItems.get(0);
//            priceToPay = menuItem.getPrice();
//            menuItem = menuItems.get(1);
//            priceToPay += menuItem.getPrice();
//
//            return priceToPay;
//        }
//        return bill.getPriceAllOrders();
    }
}
