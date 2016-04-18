package edu.avans.hartigehap.domain;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by Student on 16-03-16.
 */
public abstract class DiscountStrategy extends DomainObject{
    public int CalculateDiscount(Bill bill){
        return 0;
    }
    public ArrayList<MenuItem> getMenuItemList(Bill bill){
        Collection<Order> orders = new ArrayList<>();
        Collection<OrderItem> orderItems = new ArrayList<>();
        Collection<FoodCategory> fc;
        for(Order order : bill.getSubmittedOrders()){
            for(OrderItem orderItem : order.getOrderItems()){
                fc = orderItem.getMenuItem().getFoodCategories();
                for(FoodCategory foodCategory : fc){
                    if(foodCategory.getTag().equals("pizza")){
                        orderItems.add(orderItem);
                    }
                }
            }
        }
        ArrayList<MenuItem> menuItemList = BubbleSort((ArrayList)orderItems);
        return menuItemList;
    }
    //Sorting the OrderItems in order to get highest PriceValues
    private ArrayList<MenuItem> BubbleSort(ArrayList<OrderItem> itemList){
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        for(OrderItem item : itemList){
            for(int i=0;i<=item.getQuantity();i++){
                menuItems.add(item.getMenuItem());
            }
        }
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