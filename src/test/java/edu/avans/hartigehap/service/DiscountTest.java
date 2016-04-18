package edu.avans.hartigehap.service;

import edu.avans.hartigehap.domain.DiscountOnePlusOne;
import edu.avans.hartigehap.domain.DiscountStrategy;
import static org.junit.Assert.*;

import org.junit.Test;
import edu.avans.hartigehap.domain.*;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by Student on 21-03-16.
 */
public class DiscountTest {


    @Test
    public void discountCalculator (){

        DiscountStrategy discountStrategyOnePlusOne = new DiscountOnePlusOne();
        DiscountStrategy discountStrategyTwoPlusOne = new DiscountTwoPlusOne();

        Bill bill;
        bill = new MockClasses().SetupBill();

        bill.setStrategy(discountStrategyOnePlusOne);

        assertEquals(30, bill.getDiscPrice());
        assertNotEquals(24, bill.getDiscPrice());
        //assertEquals(44, disc.getDiscountPrice(setupBill.SetupBill(), 2));
        //assertNotEquals(20, disc.getDiscountPrice(setupBill.SetupBill(), 2));
    }

}
