package edu.avans.hartigehap.domain;
import java.util.ArrayList;
/**
 * Created by Student on 21-03-16.
 */
public class DiscountTwoPlusOne extends DiscountStrategy {
    public int CalculateDiscount(Bill bill){
        int priceToPay = 0;
        ArrayList<MenuItem> menuItems = this.getMenuItemList(bill);
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
    }
}