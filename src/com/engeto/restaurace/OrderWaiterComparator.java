package com.engeto.restaurace;

import java.util.Comparator;

public class OrderWaiterComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Integer.compare(o1.getWaiter(), o2.getWaiter());
    }
}
