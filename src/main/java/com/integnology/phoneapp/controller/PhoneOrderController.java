package com.integnology.phoneapp.controller;

/**
 * Created by calvinmak on 5/15/15.
 */


import com.integnology.phoneapp.service.PhoneOrderService;
import com.integnology.phoneapp.model.PhoneOrder;
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
    private PhoneOrderService phoneOrderService;

    public void setPhoneOrderService(PhoneOrderService phoneOrderService) {
        this.phoneOrderService = phoneOrderService;
    }

    @RequestMapping(value="/")
    public String showIndex(){
        return "index";
    }


    @RequestMapping(value="order", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PhoneOrder> getAllOrders() {
        return phoneOrderService.getAllPhoneOrders();
    }

    @RequestMapping(value="order/id/{id}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrderById(@PathVariable("id") UUID id) {
        return phoneOrderService.getPhoneOrdersById(id);
    }

    @RequestMapping(value="order/status/{status}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrdersByStatus(@PathVariable("status") String status) {
        return phoneOrderService.getPhoneOrdersByStatus(status);
    }

    @RequestMapping(value="order", method = RequestMethod.POST)
    public ResponseEntity<PhoneOrder> createOrder(@RequestBody PhoneOrder phoneOrder) {
        phoneOrderService.createPhoneOrder(phoneOrder);
        return new ResponseEntity<PhoneOrder>(phoneOrder, HttpStatus.OK);
    }

    @RequestMapping(value="order", method = RequestMethod.PUT)
    public ResponseEntity<PhoneOrder> updateOrder(@RequestBody PhoneOrder phoneOrder) {
        List<PhoneOrder> updateOrder = new ArrayList<PhoneOrder>();
        updateOrder.add(phoneOrder);
        phoneOrderService.updatePhoneOrders(updateOrder);
        return new ResponseEntity<PhoneOrder>(phoneOrder, HttpStatus.OK);
    }
}