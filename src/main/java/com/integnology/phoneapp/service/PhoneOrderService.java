package com.integnology.phoneapp.service;

import com.integnology.phoneapp.dao.PhoneOrderDao;
import com.integnology.phoneapp.model.PhoneOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by calvinmak on 5/19/15.
 */
@Service
public class PhoneOrderService {

    @Autowired
    private PhoneOrderDao phoneOrderDao;

    public List<PhoneOrder> getAllPhoneOrders() {
        return phoneOrderDao.getAllPhoneOrders();
    }

    public List<PhoneOrder> getPhoneOrdersById(UUID id) {
        return phoneOrderDao.getPhoneOrdersById(id);
    }

    public List<PhoneOrder> getPhoneOrdersByStatus(final String status) {
        return phoneOrderDao.getPhoneOrdersByStatus(status);
    }

    public void createPhoneOrder(PhoneOrder phoneOrder) {
        phoneOrderDao.createPhoneOrder(phoneOrder);
    }

    public void updatePhoneOrders(List<PhoneOrder> phoneOrders) {
        phoneOrderDao.updatePhoneOrders(phoneOrders);
    }
}
