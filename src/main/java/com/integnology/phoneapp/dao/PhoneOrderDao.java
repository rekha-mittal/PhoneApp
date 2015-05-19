package com.integnology.phoneapp.dao;

import com.integnology.phoneapp.model.PhoneOrder;

import java.util.List;
import java.util.UUID;

/**
 * Created by calvinmak on 5/18/15.
 */
public interface PhoneOrderDao {
    public void updateOrders(List<PhoneOrder> phoneOrders);

    public List<PhoneOrder> getOrdersById(final UUID id);

    public List<PhoneOrder> getOrdersByStatus(final String status);

    public void clearOrders();

    public void createOrder(PhoneOrder order);

    public List<PhoneOrder> getAllOrders();

    public List<PhoneOrder> findOrders(Matcher<PhoneOrder> m);

    interface Matcher<T> {
        public boolean matches(T t);
    }
}
