package edu.avans.hartigehap.domain;
import org.joda.time.Chronology;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by Student on 16-03-16.
 */
@Entity
public abstract class DiscountStrategy extends DomainObject{
    public int CalculateDiscount(Bill bill){
        return 0;
    }
    
    public ArrayList<MenuItem> BubbleSort(ArrayList<MenuItem> menuItems){
        sortPrice(menuItems.toArray(new MenuItem[menuItems.size()]));
        return menuItems;
    }
    
    public static void sortPrice(MenuItem arr[]){
        Arrays.sort(arr, PRICE_ORDER);
        String out="";
        for(MenuItem item : arr){
            out+= item.toString()+"\n";
        }
    }
    private static final Comparator<MenuItem> PRICE_ORDER = new Comparator<MenuItem>() {
        public int compare(MenuItem e1, MenuItem e2) {
            return Double.compare(e1.getPrice(), e2.getPrice());
        }
    };
}