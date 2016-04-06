package edu.avans.hartigehap.web.form;

import java.util.List;
import edu.avans.hartigehap.domain.*;

public class CustomerGrid {

    private int totalPages;

    private int currentPage;

    private long totalRecords;

    private List<CopyCustomer> customerData;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<CopyCustomer> getCustomerData() {
        return customerData;
    }

    public void setCustomerData(List<CopyCustomer> customerData) {
        this.customerData = customerData;
    }

}
