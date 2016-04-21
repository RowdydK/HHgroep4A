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
    public void discountCalculator() {

        DiscountStrategy discountOne = new DiscountOnePlusOne();
        DiscountStrategy discountTwo = new DiscountTwoPlusOne();

        Bill bill = new MockClasses().SetupBill();
        bill.setStrategy(discountOne);

        assertEquals(38, bill.getDiscPrice());
        //assertNotEquals(24, bill.getDiscPrice());
        bill.setStrategy(discountTwo);
        assertEquals(47, bill.getDiscPrice());
        //assertNotEquals(20, disc.getDiscountPrice(bill));

    }
}
