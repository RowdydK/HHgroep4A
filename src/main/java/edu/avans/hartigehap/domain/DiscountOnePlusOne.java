package edu.avans.hartigehap.domain;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Created by Student on 21-03-16.
 */
public class DiscountOnePlusOne extends DiscountStrategy {

	@Override
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
		ArrayList<MenuItem> bubbleSortedItems;
		bubbleSortedItems = BubbleSort(discountItems);

		int discountprice;
		discountprice = getDiscPrice(bubbleSortedItems);

		return normalPrice  - getDiscPrice(bubbleSortedItems);

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
    
	// n aantal pizza's voor de discount
	//
    public int getDiscPrice(ArrayList<MenuItem> menuItems) {
		int discountPrice = 0;

		int itemsToPay;
		itemsToPay = (menuItems.size() / 2);
		for (int i = 0; i < itemsToPay; i++) {
			MenuItem tmp = menuItems.get(i);
			discountPrice += tmp.getPrice();
		}

		return discountPrice;
	}
}