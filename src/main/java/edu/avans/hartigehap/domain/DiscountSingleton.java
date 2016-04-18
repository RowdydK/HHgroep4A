package edu.avans.hartigehap.domain;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DiscountSingleton {
	
	private DiscountStrategy discountOnePlusOne;
	private DiscountStrategy discountTwoPlusOne;
	

	private static DiscountSingleton singleton = new DiscountSingleton();
	
	private DiscountSingleton(){
		discountOnePlusOne = new DiscountOnePlusOne();
		discountTwoPlusOne = new DiscountTwoPlusOne();
	}
	
	public static DiscountSingleton getInstance(){
		return singleton;
	}
	
	
	public DiscountStrategy getDiscountStrategy(){
		//return new DiscountOnePlusOne();
		LocalDate date = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int weekNumber= date.get(weekFields.weekOfWeekBasedYear());
		
		if ((weekNumber%2)==0){
			return discountTwoPlusOne;
		}else{
			return discountOnePlusOne;
		}
	}
	
}
