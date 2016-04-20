package edu.avans.hartigehap.domain;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Created by Student on 21-03-16.
 */
public class DiscountTwoPlusOne extends DiscountStrategy {
	
	public int CalculateDiscount(Bill bill){
		ArrayList<OrderItem> orderItems = new ArrayList();
		for (Order order : bill.getAllOrders()){
			orderItems.addAll(order.getOrderItems());
		}
		ArrayList<MenuItem> menuItems = OrderItemtoMenuItems(orderItems);
		ArrayList<MenuItem> discountItems = new ArrayList<>();
		for (MenuItem mi : menuItems){
			Collection<FoodCategory> fc = mi.getFoodCategories();
			for (FoodCategory foodCat : fc){
				if (foodCat.getTag().equals("pizza")){
					discountItems.add(mi);
				}
			}
		}
		int normalPrice = 0;
		for (MenuItem mi: menuItems){
			normalPrice = normalPrice + mi.getPrice();
		}
		discountItems = BubbleSort(discountItems);
		
		return (normalPrice - getDiscPrice(discountItems));

    }
	
	public ArrayList<MenuItem> OrderItemtoMenuItems(ArrayList<OrderItem> itemList){
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        for(OrderItem item : itemList){
            for(int i=0;i<item.getQuantity();i++){
                menuItems.add(item.getMenuItem());
            }
        }
        return menuItems;
	}
    
	
    public int getDiscPrice(ArrayList<MenuItem> menuItems){
		int priceToPay = 0;
		int totalPrice = 0;
		for (MenuItem mi: menuItems){
			totalPrice = totalPrice + mi.getPrice();
		}
		int numberOfPizzas = menuItems.size();
		int index = 0;
		while(numberOfPizzas>=3){
		    menuItems.remove(menuItems.size()-1);
		    numberOfPizzas-=3;
		}
		for(MenuItem mi : menuItems){
		    priceToPay+=mi.getPrice();
		}
		return totalPrice - priceToPay;
	}
	
	
//	
//    public int CalculateDiscount(Bill bill){
//        int priceToPay = 0;
//        ArrayList<MenuItem> menuItems = this.getMenuItemList(bill);
//        int numberOfPizzas = menuItems.size();
//        int index = 0;
//        while(numberOfPizzas>=3){
//            menuItems.remove(menuItems.size()-1);
//            numberOfPizzas-=3;
//        }
//        for(MenuItem mi : menuItems){
//            priceToPay+=mi.getPrice();
//        }
//        return priceToPay;
//    }
    
}