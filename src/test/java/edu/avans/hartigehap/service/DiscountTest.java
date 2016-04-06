package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.DiscountStrategy;
import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Created by Student on 21-03-16.
 */
public class DiscountTest {


    @Test
    public void discountCalculator (){

        FilledClasses filledClasses = new FilledClasses();
        DiscountStrategy disc = new DiscountStrategy();

        assertEquals(30, disc.getDiscountPrice(filledClasses.SetupBill(), 1));
        assertNotEquals(24, disc.getDiscountPrice(filledClasses.SetupBill(), 1));

        assertEquals(44, disc.getDiscountPrice(filledClasses.SetupBill(), 2));
        assertNotEquals(20, disc.getDiscountPrice(filledClasses.SetupBill(), 2));
    }

}
