package edu.avans.hartigehap.domain;

import java.util.ArrayList;

/**
 * Created by Student on 21-03-16.
 */
public class DiscountOnePlusOne extends DiscountStrategy {

    public int CalculateDiscount(ArrayList<MenuItem> menuItems){

        int discountPrice = 1;


        int itemsToPay;

        if(menuItems.size()%2 == 1){

            itemsToPay = (menuItems.size()-1)/2;

            for(int i=0; i <= (itemsToPay + 1); i++){
                MenuItem tmp = menuItems.get(i);
                discountPrice += tmp.getPrice();
            }
        }
        else{

            itemsToPay = (menuItems.size()-1)/2;

            for(int i=0; i <= itemsToPay; i++){
                MenuItem tmp = menuItems.get(i);
                discountPrice += tmp.getPrice();
            }
        }

        return discountPrice;
    }
}
