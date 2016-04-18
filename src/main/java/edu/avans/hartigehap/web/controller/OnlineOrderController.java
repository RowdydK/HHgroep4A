package edu.avans.hartigehap.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.domain.Bill.BillStateId;
import edu.avans.hartigehap.service.*;
import edu.avans.hartigehap.web.form.Message;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@Slf4j
public class OnlineOrderController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OnlineOrderService onlineOrderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BillService billService;
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online", method = RequestMethod.GET)
    public String nShowTable(@PathVariable("restaurantId") String restaurantId, Model uiModel) {
        log.info("restaurantId = " + restaurantId);

        nFillModel(restaurantId, null , uiModel);

        return "hartigehap/onlineorder";
    }
    
    
    private void nFillModel(String restaurantId, String billId, Model uiModel){
    	Collection<Restaurant> restaurants = restaurantService.findAll();
        uiModel.addAttribute("restaurants", restaurants);
        for(Restaurant r : restaurants){
        	if(r.getId().equals(restaurantId)) {
        		uiModel.addAttribute("restaurant", r);
        	}
        }
        Bill bill;
        if (billId == null){
        	bill = billService.save(new Bill());
        }
        else{
        	bill = billService.findById(Long.valueOf(billId));
        }
        uiModel.addAttribute("bill", bill);
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}", method = RequestMethod.GET)
    public String nShowTable(@PathVariable("restaurantId") String restaurantId, @PathVariable("billId") String billId, Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        Bill bill = billService.findById(Long.valueOf(billId));
        bill.setStrategy(new DiscountOnePlusOne());
        int newPrice = bill.getStrategy().CalculateDiscount(bill);
        
        nFillModel(restaurantId,billId,uiModel);
        uiModel.addAttribute("discountPrice", newPrice);
        return "hartigehap/onlineorder";
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/orderItems", method = RequestMethod.POST)
    public String nAddOrderItem(@PathVariable("restaurantId") String restaurantId, @PathVariable ("billId") String billId,
    		@RequestParam String menuItemName, Model uiModel){
    	
    	Bill bill = billService.findById(Long.valueOf(billId));
    	
    	onlineOrderService.addOrderItem(bill, menuItemName);
    	
    	return "redirect:/restaurants/" + restaurantId + "/online/bill/"+ billId;
    	
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/orderItems/{orderItemId}", method = RequestMethod.POST)
    public String nAddOrderItemIngredient(@PathVariable("restaurantId") String restaurantId, @PathVariable ("billId") String billId, @PathVariable ("orderItemId") String orderItemId,
    		@RequestParam Long ingredientId, Model uiModel){
    	log.debug("IngredientId: " + ingredientId);
    	onlineOrderService.addOrderItemIngredient(Long.valueOf(orderItemId), ingredientId);
    	
    	return "redirect:/restaurants/" + restaurantId + "/online/bill/"+ billId;
    	
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/orderItems/{menuItemName}", method = RequestMethod.DELETE)
    public String nDeleteMenuItem(@PathVariable("restaurantId") String restaurantId,
            @PathVariable("menuItemName") String menuItemName,@PathVariable("billId") String billId,
            Model uiModel) {

    	Bill bill = billService.findById(Long.valueOf(billId));
    	
    	onlineOrderService.deleteOrderItem(bill, menuItemName);


    	return "redirect:/restaurants/" + restaurantId + "/online/bill/"+ billId;
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}", method = RequestMethod.PUT)
    public String nReceiveEvent(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId
    		,@RequestParam String event, Model uiModel, Locale locale) {
        log.info("(receiveEvent) restaurant = " + restaurantId);

        // because of REST, the "event" parameter is part of the body. It
        // therefore cannot be used for
        // the request mapping so all events for the same resource will be
        // handled by the same
        // controller method; so we end up with an if statement        
        Bill bill = billService.findById(Long.valueOf(billId));
        switch (event) {
        case "submitOrder":
            try {
            	bill.submit();
            	onlineOrderService.submitBill(bill);
            	//onlineOrderService.submitOrder(bill.getCurrentOrder());
            }catch(Exception e){
            	System.out.println(e);
            }
            nFillModel(restaurantId, billId, uiModel);
            Order order = orderService.findById(bill.getCurrentOrder().getId());
            uiModel.addAttribute("order", order);
            
            // store the message temporarily in the session to allow displaying
            // after redirect
            
            return "redirect:/restaurants/"+ restaurantId + "/online/bill/"+billId+"/customer";
        default:
            //warmupRestaurant(diningTableId, uiModel);
            //log.error("internal error: event " + event + "not recognized");
            //return "hartigehap/diningtable";
        			return null;
        }
    }
    
    private String nSubmitOrder(String restaurantId, Bill bill, Model uiModel,
            Locale locale) {
        
        //DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
        //Bill bill = billService.findById(Long.valueOf(billId));
        try {
        	onlineOrderService.submitBill(bill);
        	billService.save(bill);
        }catch(Exception e){
        	
        }
        String billId = bill.getId().toString();
        nFillModel(restaurantId, billId, uiModel);
        
        // store the message temporarily in the session to allow displaying
        // after redirect
        
        return "redirect:/restaurants/"+ restaurantId + "/online/bill/"+billId+"/customer";

    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/customer", method = RequestMethod.GET)
    public String nShowCustomer(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId,
    		Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        
        Bill bill = billService.findById(Long.valueOf(billId));
        
        
        Collection<Order> orders = bill.getAllOrders();
        
        Order order = null;
        
        if (orders.iterator().hasNext()){
        	order = orders.iterator().next();
        }


        nFillModel(restaurantId, billId , uiModel);
        uiModel.addAttribute("order", order);

        return "hartigehap/onlineordercustomer";
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/customer/newcustomer",params = "firstName", method = RequestMethod.POST)
    public String nReceiveCustomer(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId
    		,@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName , @RequestParam("zipCode") String zipCode,
    		@RequestParam("zipCode") String cityName,@RequestParam("number") String number,
    		Model uiModel, Locale locale) {
        log.info("(receiveEvent) restaurant = " + restaurantId);

        Customer customer;
        Bill bill = billService.findById(Long.valueOf(billId));
        customer = customerService.save(new Customer(firstName,lastName,zipCode,cityName,number,bill));
        
        try{
			bill.submit();
			billService.save(bill);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   

        System.out.println(firstName);
        
        return "redirect:/restaurants/"+ restaurantId + "/online/bill/"+billId+"/customer/"+customer.getId();

//        }
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bill/{billId}/customer/{customerId}", method = RequestMethod.GET)
    public String nShowPayment(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId,
    		@PathVariable("customerId") String customerId, Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        nFillModel(restaurantId, billId , uiModel);
        
        
        Bill bill = billService.findById(Long.valueOf(billId));

        try {
			bill.paid();
			billService.save(bill);
		} catch (StateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("paid");
        return "hartigehap/onlineorderpayment";
    }
    
    
    ///// OLD CODE 
    
    
    
    
//    @RequestMapping(value = "/restaurants/{restaurantId}/online", method = RequestMethod.GET)
//    public String showTable(@PathVariable("restaurantId") String restaurantId, Model uiModel) {
//        log.info("restaurantId = " + restaurantId);
//
//        fillModel(restaurantId, null , null, uiModel);
//
//        return "hartigehap/onlineorder";
//    }
//    
//    @RequestMapping(value = "/restaurants/{restaurantId}/online/{customerId}", method = RequestMethod.GET)
//    public String showTable(@PathVariable("restaurantId") String restaurantId, @PathVariable("customerId") String customerId, Model uiModel) {
//        log.info("restaurantId = " + restaurantId);
//
//        fillModel(restaurantId, customerId, uiModel);
//
//        return "hartigehap/onlineorder";
//    }
//    
//    private void fillModel(String restaurantId, Model uiModel) {
//        Collection<Restaurant> restaurants = restaurantService.findAll();
//        uiModel.addAttribute("restaurants", restaurants);
//        for(Restaurant r : restaurants){
//        	if(r.getId().equals(restaurantId)) {
//        		uiModel.addAttribute("restaurant", r);
//        	}
//        }
//    }
//    
//    private void fillModel(String restaurantId, String customerId, Model uiModel) {
//        Collection<Restaurant> restaurants = restaurantService.findAll();
//        uiModel.addAttribute("restaurants", restaurants);
//        Customer customer;
//        if (customerId == null){
//        	byte[] photo = new byte[] { 127, -128, 0 };
//        	customer = customerService.save(new Customer("null", "null", new DateTime(), 1, "null", photo));
//        	System.out.println("test");
//        }else{
//        	customer = customerService.findById(Long.valueOf(customerId));
//        }
//        uiModel.addAttribute("customer", customer);
//        for(Restaurant r : restaurants){
//        	if(r.getId().equals(restaurantId)) {
//        		uiModel.addAttribute("restaurant", r);
//        	}
//        }
//
//        //return customer;
//    }
//    
//    
//    private void fillModel(String restaurantId, String customerId,String orderId, Model uiModel) {
//        Collection<Restaurant> restaurants = restaurantService.findAll();
//        uiModel.addAttribute("restaurants", restaurants);
//        Customer customer;
//        if (customerId == null){
//        	byte[] photo = new byte[] { 127, -128, 0 };
//        	customer = customerService.save(new Customer("null", "null", new DateTime(), 1, "null", photo));
//        	System.out.println("test");
//        }else{
//        	customer = customerService.findById(Long.valueOf(customerId));
//        }
//        uiModel.addAttribute("customer", customer);
//        for(Restaurant r : restaurants){
//        	if(r.getId().equals(restaurantId)) {
//        		uiModel.addAttribute("restaurant", r);
//        	}
//        }
//        uiModel.addAttribute("order", customer.getCurrentBill().getCurrentOrder());
//    }
//
//    @RequestMapping(value = "/restaurants/{restaurantId}/online/{customerId}/", method = RequestMethod.PUT)
//    public String receiveEvent(@PathVariable("restaurantId") String restaurantId,@PathVariable("customerId") String customerId
//    		,@RequestParam String event, Model uiModel, Locale locale) {
//
//        log.info("(receiveEvent) restaurant = " + restaurantId);
//
//        // because of REST, the "event" parameter is part of the body. It
//        // therefore cannot be used for
//        // the request mapping so all events for the same resource will be
//        // handled by the same
//        // controller method; so we end up with an if statement
//
//        switch (event) {
//        case "submitOrder":
//        	
//            return submitOrder(restaurantId,customerId, uiModel, locale);
//        // break unreachable
//
//        case "submitBill":
//            //return submitBill(diningTableId, redirectAttributes, uiModel, locale);
//        // break unreachable
//        			return null;
//        default:
//            //warmupRestaurant(diningTableId, uiModel);
//            //log.error("internal error: event " + event + "not recognized");
//            //return "hartigehap/diningtable";
//        			return null;
//        }
//    }
//    
//
//    private String submitOrder(String restaurantId, String customerId, Model uiModel,
//            Locale locale) {
//        
//        //DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
//        Customer customer =customerService.findById(Long.valueOf(customerId));
////        try {
////            onlineOrderService.submitOrder(customer);
////        } catch (StateException e) {
////            //return handleStateException(e, "message_submit_order_fail", customerId, uiModel, locale);
////        }
//        
//        // store the message temporarily in the session to allow displaying
//        // after redirect
//        
//        return "redirect:/restaurants/"+ restaurantId;
//
//    }
//    
//    @RequestMapping(value = "/restaurants/{restaurantId}/online/{customerId}/{orderId}/orderItems", method = RequestMethod.POST)
//    public String addOrderItem(@PathVariable("restaurantId") String restaurantId, @PathVariable ("customerId") String customerId,
//    		@PathVariable("orderId") String orderId, @RequestParam String menuItemName, Model uiModel){
//    	
//    	System.out.println("testing");
//    	Customer customer = customerService.findById(Long.valueOf(customerId));
//    	
//    	//onlineOrderService.addOrderItem(customer, menuItemName);
//    	
//    	
////        DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
////        uiModel.addAttribute("diningTable", diningTable);
////
////        diningTableService.addOrderItem(diningTable, menuItemName);
//
//        return "redirect:/restaurants/" + restaurantId + "/online/"+ customerId +"/" + orderId;
//    }
//    
//    @RequestMapping(value = "/restaurants/{restaurantId}/online/{customerId}/{orderId}", method = RequestMethod.GET)
//    public String newShowTable(@PathVariable("restaurantId") String restaurantId, @PathVariable("customerId") String customerId, 
//    		@PathVariable("orderId") String orderId,
//    		Model uiModel) {
//        log.info("restaurantId = " + restaurantId);
//        fillModel(restaurantId,customerId,orderId,uiModel);
//
//        return "hartigehap/onlineorder";
//    }
//    
//  @RequestMapping(value = "/restaurants/{restaurantId}/online/{customerId}/{orderId}/orderItems/{menuItemName}", method = RequestMethod.DELETE)
//  public String deleteMenuItem(@PathVariable("restaurantId") String restaurantId,
//          @PathVariable("menuItemName") String menuItemName,@PathVariable("customerId") String customerId,
//          @PathVariable("orderId") String orderId, Model uiModel) {
//
//	  Customer customer = customerService.findById(Long.valueOf(customerId));
//
//	  return "redirect:/restaurants/" + restaurantId + "/online/"+ customerId +"/" + orderId;
//  }
//  
//  
//    
//    
//    @RequestMapping(value = "/diningTables/{diningTableId}/menuItems", method = RequestMethod.POST)
//    public String addMenuItem(@PathVariable("diningTableId") String diningTableId, @RequestParam String menuItemName,
//            Model uiModel) {
//
//        DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
//        uiModel.addAttribute("diningTable", diningTable);
//
//        diningTableService.addOrderItem(diningTable, menuItemName);
//
//        return "redirect:/diningTables/" + diningTableId;
//    }
    

//    @RequestMapping(value = "/diningTables/{diningTableId}", method = RequestMethod.GET)
//    public String showTable(@PathVariable("diningTableId") String diningTableId, Model uiModel) {
//        log.info("diningTable = " + diningTableId);
//
//        warmupRestaurant(diningTableId, uiModel);
//
//        return "hartigehap/diningtable";
//    }
//
//    @RequestMapping(value = "/diningTables/{diningTableId}/menuItems", method = RequestMethod.POST)
//    public String addMenuItem(@PathVariable("diningTableId") String diningTableId, @RequestParam String menuItemName,
//            Model uiModel) {
//
//        DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
//        uiModel.addAttribute("diningTable", diningTable);
//
//        diningTableService.addOrderItem(diningTable, menuItemName);
//
//        return "redirect:/diningTables/" + diningTableId;
//    }
//
//    @RequestMapping(value = "/diningTables/{diningTableId}/menuItems/{menuItemName}", method = RequestMethod.DELETE)
//    public String deleteMenuItem(@PathVariable("diningTableId") String diningTableId,
//            @PathVariable("menuItemName") String menuItemName, Model uiModel) {
//
//        DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
//        uiModel.addAttribute("diningTable", diningTable);
//
//        diningTableService.deleteOrderItem(diningTable, menuItemName);
//
//        return "redirect:/diningTables/" + diningTableId;
//    }
//
//    @RequestMapping(value = "/diningTables/{diningTableId}", method = RequestMethod.PUT)
//    public String receiveEvent(@PathVariable("diningTableId") String diningTableId, @RequestParam String event,
//            RedirectAttributes redirectAttributes, Model uiModel, Locale locale) {
//
//        log.info("(receiveEvent) diningTable = " + diningTableId);
//
//        // because of REST, the "event" parameter is part of the body. It
//        // therefore cannot be used for
//        // the request mapping so all events for the same resource will be
//        // handled by the same
//        // controller method; so we end up with an if statement
//
//        switch (event) {
//        case "submitOrder":
//            return submitOrder(diningTableId, redirectAttributes, uiModel, locale);
//        // break unreachable
//
//        case "submitBill":
//            return submitBill(diningTableId, redirectAttributes, uiModel, locale);
//        // break unreachable
//
//        default:
//            warmupRestaurant(diningTableId, uiModel);
//            log.error("internal error: event " + event + "not recognized");
//            return "hartigehap/diningtable";
//        }
//    }
//
//    private String submitOrder(String diningTableId, RedirectAttributes redirectAttributes, Model uiModel,
//            Locale locale) {
//        
//        DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
//        
//        try {
//            diningTableService.submitOrder(diningTable);
//        } catch (StateException e) {
//            return handleStateException(e, "message_submit_order_fail", diningTableId, uiModel, locale);
//        }
//        
//        // store the message temporarily in the session to allow displaying
//        // after redirect
//        redirectAttributes.addFlashAttribute("message", new Message("success",
//                messageSource.getMessage("message_submit_order_success", new Object[] {}, locale)));
//        
//        return "redirect:/diningTables/" + diningTableId;
//
//    }
//
//    private String submitBill(String diningTableId, RedirectAttributes redirectAttributes, Model uiModel,
//            Locale locale) {
//        
//        DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
//        
//        try {
//            diningTableService.submitBill(diningTable);
//        } catch (EmptyBillException e) {
//            log.error("EmptyBillException", e);
//            uiModel.addAttribute("message", new Message("error",
//                    messageSource.getMessage("message_submit_empty_bill_fail", new Object[] {}, locale)));
//            return "hartigehap/diningtable";
//        } catch (StateException e) {
//            return handleStateException(e, "message_submit_bill_fail", diningTableId, uiModel, locale);
//        }
//        
//        // store the message temporarily in the session to allow displaying
//        // after redirect
//        redirectAttributes.addFlashAttribute("message", new Message("success",
//                messageSource.getMessage("message_submit_bill_success", new Object[] {}, locale)));
//        
//        return "redirect:/diningTables/" + diningTableId;
//    }
//
//    private DiningTable warmupRestaurant(String diningTableId, Model uiModel) {
//        Collection<Restaurant> restaurants = restaurantService.findAll();
//        uiModel.addAttribute("restaurants", restaurants);
//        DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
//        uiModel.addAttribute("diningTable", diningTable);
//        Restaurant restaurant = restaurantService.fetchWarmedUp(diningTable.getRestaurant().getId());
//        uiModel.addAttribute("restaurant", restaurant);
//
//        return diningTable;
//    }
//    
//    private String handleStateException(StateException e, String errorMessage, String diningTableId, 
//            Model uiModel, Locale locale) {
//        log.error("StateException", e);
//        uiModel.addAttribute("message", new Message("error",
//                messageSource.getMessage(errorMessage, new Object[] {}, locale)));
//
//        // StateException triggers a rollback; consequently all Entities are
//        // invalidated by Hibernate
//        // So new warmup needed
//        warmupRestaurant(diningTableId, uiModel);
//
//        return "hartigehap/diningtable";
//    }
}
