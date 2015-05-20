package com.integnology.phoneapp.dao;

import com.integnology.phoneapp.model.PhoneOrder;

import java.util.List;
import java.util.UUID;

/**
 * Created by calvinmak on 5/18/15.
 */
public interface PhoneOrderDao {
    public void updatePhoneOrders(List<PhoneOrder> phoneOrders);

    public List<PhoneOrder> getPhoneOrdersById(final UUID id);

    public List<PhoneOrder> getPhoneOrdersByStatus(final String status);

    public void clearPhoneOrders();

    public void createPhoneOrder(PhoneOrder phoneOrder);

    public List<PhoneOrder> getAllPhoneOrders();

    public List<PhoneOrder> findPhoneOrders(Matcher<PhoneOrder> m);

    interface Matcher<T> {
        public boolean matches(T t);
    }
}
