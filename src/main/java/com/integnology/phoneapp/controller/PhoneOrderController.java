package com.integnology.phoneapp.controller;

/**
 * Created by calvinmak on 5/15/15.
 */


import com.integnology.phoneapp.model.PhoneOrder;
import com.integnology.phoneapp.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;



@Controller
public class PhoneOrderController {

    @Autowired
    private OrderDao orderDao;

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao=orderDao;
    }

    @RequestMapping(value="/")
    public String showIndex(){
        return "index";
    }


    @RequestMapping(value="order", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PhoneOrder> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @RequestMapping(value="order/id/{id}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrderById(@PathVariable("id") UUID id) {
        List<PhoneOrder> orders = orderDao.getOrdersById(id);
        return orders;
    }

    @RequestMapping(value="order/status/{status}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrdersByStatus(@PathVariable("status") String status) {
        List<PhoneOrder> orders = orderDao.getOrdersByStatus(status);
        return orders;
    }

    @RequestMapping(value="order", method = RequestMethod.POST)
    public ResponseEntity<PhoneOrder> createOrder(@RequestBody PhoneOrder order) {
        orderDao.createOrder(order);
        return new ResponseEntity<PhoneOrder>(order, HttpStatus.OK);
    }

    @RequestMapping(value="order", method = RequestMethod.PUT)
    public ResponseEntity<PhoneOrder> updateOrder(@RequestBody PhoneOrder order) {
        List<PhoneOrder> updateOrder = new ArrayList<PhoneOrder>();
        updateOrder.add(order);
        orderDao.updateOrders(updateOrder);
        return new ResponseEntity<PhoneOrder>(order, HttpStatus.OK);
    }
}