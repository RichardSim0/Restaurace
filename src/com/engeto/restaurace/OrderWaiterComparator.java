package com.engeto.restaurace;

import java.util.Comparator;

public class OrderWaiterComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        int mainWaiterId1 = getMainWaiterId(o1);
        int mainWaiterId2 = getMainWaiterId(o2);
        return Integer.compare(mainWaiterId1, mainWaiterId2);
    }

    private int getMainWaiterId(Order order) {
        if (!order.getWaiters().isEmpty()) {
            return order.getWaiters().get(0).getId();
        }
        return Integer.MAX_VALUE;
    }
}
