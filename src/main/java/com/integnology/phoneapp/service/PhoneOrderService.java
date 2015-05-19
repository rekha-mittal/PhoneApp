package com.integnology.phoneapp.service;

import com.integnology.phoneapp.model.PhoneOrder;
import com.integnology.phoneapp.model.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import com.integnology.phoneapp.dao.PhoneOrderDao;
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

    public List<PhoneOrder> getAllOrders() {
        return phoneOrderDao.getAllOrders();
    }

    public List<PhoneOrder> getOrdersById(UUID id) {
        return phoneOrderDao.getOrdersById(id);
    }

    public List<PhoneOrder> getOrdersByStatus(final String status) {
        return phoneOrderDao.getOrdersByStatus(status);
    }

    public void createOrder(PhoneOrder phoneOrder) {
        phoneOrderDao.createOrder(phoneOrder);
    }

    public void updateOrders(List<PhoneOrder> phoneOrders) {
        phoneOrderDao.updateOrders(phoneOrders);
    }
}
