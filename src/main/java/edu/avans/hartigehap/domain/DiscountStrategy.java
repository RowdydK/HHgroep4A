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
    public class DiscountStrategy {

        public int getDiscountPrice (Bill bill, int discountValue){
            int newPrice = 0;

            Collection<Order> orders = new ArrayList<>();
            Collection<OrderItem> orderItems = new ArrayList<>();
            Collection<FoodCategory> fc;

            for(Order order : bill.getOrders()){
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

            switch(discountValue){
                case 1: newPrice = new DiscountOnePlusOne().CalculateDiscount(menuItemList);
                    break;
                case 2: newPrice = new DiscountTwoPlusOne().CalculateDiscount(menuItemList, bill);
                    break;
                default: break;
            }

            return newPrice;
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
            String out = "";
            for(MenuItem item : arr){
                out+= item.toString()+"\n";
            }
        }

        private static final Comparator<MenuItem> PRICE_ORDER = new Comparator<MenuItem>() {
            public int compare(MenuItem m1, MenuItem m2) {
                return Double.compare(m1.getPrice(), m2.getPrice());
            }
        };
    }

