package edu.avans.hartigehap.domain;
import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * Created by Student on 21-03-16.
 */
public class DiscountOnePlusOne extends DiscountStrategy {
    public int CalculateDiscount(Bill bill){
        int discountPrice = 0;
        ArrayList<MenuItem> menuItems = this.getMenuItemList(bill);
        int itemsToPay;
        
        itemsToPay = (menuItems.size()/2);
        
        for (int i = 0; i < itemsToPay; i++){
        	MenuItem tmp = menuItems.get(i);
        	discountPrice += tmp.getPrice();
        }
        
//        if(menuItems.size()%2 == 1){
//            itemsToPay = (menuItems.size()-1)/2;
//            for(int i=0; i < itemsToPay; i++){
//                MenuItem tmp = menuItems.get(i);
//                discountPrice += tmp.getPrice();
//            }
//        }
//        else{
//            itemsToPay = (menuItems.size()-1)/2;
//            for(int i=0; i < itemsToPay; i++){
//                MenuItem tmp = menuItems.get(i);
//                discountPrice += tmp.getPrice();
//            }
//        }
        return discountPrice;
    }
}