package com.integnology.phoneapp.dao;

import com.integnology.phoneapp.model.PhoneOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by rekhamittal on 5/5/15.
 */
public class OrderDao {
    private static List<PhoneOrder> orders = new ArrayList<PhoneOrder>();

    public static void updateOrders(List<PhoneOrder> phoneOrders) {
        for (PhoneOrder order : phoneOrders) {
            UUID id = order.getId();
            List<PhoneOrder> existingOrders = getOrdersById(id);
            if (existingOrders.isEmpty() || existingOrders.size() > 1) {
                throw new RuntimeException("Order not exist or there are multiple orders with id: " + id);
            }
            orders.remove(existingOrders.get(0));
            orders.add(order);
        }
    }

    public static List<PhoneOrder> getOrdersById(final UUID id) {
        return findOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder order) {
                if (order.getId().equals(id)) {
                    return true;
                }
                return false;
            }
        });
    }

    public static List<PhoneOrder> getOrdersByStatus(final String status) {
        return findOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder order) {
                if (order.getStatus().equalsIgnoreCase(status)) {
                    return true;
                }
                return false;
            }
        });
    }

    public static void clearOrders() {
        orders.clear();
    }

    public static void createOrder(PhoneOrder order) {
        order.setId(UUID.randomUUID());
        orders.add(order);
    }

    public static List<PhoneOrder> getAllOrders() {
        return orders;
    }

    public static List<PhoneOrder> findOrders(Matcher<PhoneOrder> m) {
        List<PhoneOrder> result = new ArrayList<PhoneOrder>();
        for (PhoneOrder order : orders) {
            if (m.matches(order)) {
                result.add(order);
            }
        }
        return result;
    }

    interface Matcher<T> {
        public boolean matches(T t);
    }

}
