package edu.avans.hartigehap.service;

import edu.avans.hartigehap.service.testutil.AbstractTransactionRollbackTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Rowdy on 16-2-2016.
 */
public class OwnerServiceTest extends AbstractTransactionRollbackTest {

    @Autowired
    private OwnerService ownerService;

    @Test
    public void setOwnerService() {

    }

}
