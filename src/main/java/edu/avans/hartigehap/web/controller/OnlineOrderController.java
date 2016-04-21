package edu.avans.hartigehap.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}", method = RequestMethod.GET)
    public String nShowTable(@PathVariable("restaurantId") String restaurantId, @PathVariable("billId") String billId, Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        Bill bill = billService.findById(Long.valueOf(billId));
        //bill.setStrategy(new DiscountOnePlusOne());
        int newPrice = bill.getStrategy().CalculateDiscount(bill);
        
        nFillModel(restaurantId,billId,uiModel);
        uiModel.addAttribute("discountPrice", newPrice);
        return "hartigehap/onlineorder";
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/orderItems", method = RequestMethod.POST)
    public String nAddOrderItem(@PathVariable("restaurantId") String restaurantId, @PathVariable ("billId") String billId,
    		@RequestParam String menuItemName, Model uiModel){
    	
    	Bill bill = billService.findById(Long.valueOf(billId));
    	
    	onlineOrderService.addOrderItem(bill, menuItemName);
    	
    	return "redirect:/restaurants/" + restaurantId + "/online/bills/"+ billId;
    	
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/orderItems/{orderItemId}", method = RequestMethod.POST)
    public String nAddOrderItemIngredient(@PathVariable("restaurantId") String restaurantId, @PathVariable ("billId") String billId, @PathVariable ("orderItemId") String orderItemId,
    		@RequestParam Long ingredientId, Model uiModel){
    	log.debug("IngredientId: " + ingredientId);
    	onlineOrderService.addOrderItemIngredient(Long.valueOf(orderItemId), ingredientId);
    	
    	return "redirect:/restaurants/" + restaurantId + "/online/bills/"+ billId;
    	
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/orderItems/{menuItemName}", method = RequestMethod.DELETE)
    public String nDeleteMenuItem(@PathVariable("restaurantId") String restaurantId,
            @PathVariable("menuItemName") String menuItemName,@PathVariable("billId") String billId,
            Model uiModel) {

    	Bill bill = billService.findById(Long.valueOf(billId));
    	
    	onlineOrderService.deleteOrderItem(bill, menuItemName);


    	return "redirect:/restaurants/" + restaurantId + "/online/bills/"+ billId;
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}", method = RequestMethod.PUT)
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
            
            return "redirect:/restaurants/"+ restaurantId + "/online/bills/"+billId+"/customer";
        default:
            //warmupRestaurant(diningTableId, uiModel);
            //log.error("internal error: event " + event + "not recognized");
            //return "hartigehap/diningtable";
        			return null;
        }
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/customer", method = RequestMethod.GET)
    public String nShowCustomer(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId,
    		Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        
        Bill bill = billService.findById(Long.valueOf(billId));
        
        int newPrice = bill.getStrategy().CalculateDiscount(bill);

        nFillModel(restaurantId,billId,uiModel);
        uiModel.addAttribute("discountPrice", newPrice);

        nFillModel(restaurantId, billId , uiModel);

        return "hartigehap/onlineordercustomer";
    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/customers/newcustomer",params = "firstName", method = RequestMethod.POST)
    public String nReceiveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult, @PathVariable("restaurantId") String restaurantId, @PathVariable("billId") String billId, Locale locale, Model uiModel){
                                   //@RequestParam("firstName") @Valid String firstName, @Valid @RequestParam("lastName") String lastName , @Valid @RequestParam("zipCode") String zipCode,
                                    //@Valid @RequestParam("zipCode") String cityName, @Valid @RequestParam("number") String number,
                                   //Model uiModel, Locale locale) {
        log.info("(receiveEvent) restaurant = " + restaurantId);

        

        if(bindingResult.hasErrors()){
            uiModel.addAttribute("message",new Message("error",
                    messageSource.getMessage("customer_save_fail", new Object[] {}, locale)));
            uiModel.addAttribute("customer", customer);
            nFillModel(restaurantId, billId , uiModel);
            return "hartigehap/onlineordercustomer";
        }else {
        	Bill bill = billService.findById(Long.valueOf(billId));
        	Restaurant restaurant = restaurantService.findById(restaurantId);
            customer.addOnlineOrderSpecs(restaurant, bill);
            customer = customerService.save(customer);
            bill = billService.save(bill);
            try{
//                bill.submit();
//                billService.save(bill);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            System.out.println(customer.getFirstName());

            return "redirect:/restaurants/"+ restaurantId + "/online/bills/"+billId+"/customers/"+customer.getId();
        }

    }
    
    @RequestMapping(value = "/restaurants/{restaurantId}/online/bills/{billId}/customers/{customerId}", method = RequestMethod.GET)
    public String nShowPayment(@PathVariable("restaurantId") String restaurantId,@PathVariable("billId") String billId,
    		@PathVariable("customerId") String customerId, Model uiModel) {
        log.info("restaurantId = " + restaurantId);
        nFillModel(restaurantId, billId , uiModel);
        
        
        Bill bill = billService.findById(Long.valueOf(billId));

        try {
			bill.paid();
			orderService.planOrder(bill.getCurrentOrder());
			bill = billService.save(bill);
		} catch (StateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("paid");
        return "hartigehap/onlineorderpayment";
    }
    
    
}
