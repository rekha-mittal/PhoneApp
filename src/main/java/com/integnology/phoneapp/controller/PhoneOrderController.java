package com.integnology.phoneapp.controller;

/**
 * Created by calvinmak on 5/15/15.
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.integnology.phoneapp.model.PhoneOrder;
import com.integnology.phoneapp.dao.OrderDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class PhoneOrderController {

    @RequestMapping(value="/")
    public String showIndex(){
        return "index";
    }


    @RequestMapping(value="order", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PhoneOrder> getAllOrders() {
        return OrderDao.getAllOrders();
    }

    @RequestMapping(value="order/id/{id}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrderById(@PathVariable("id") UUID id) {
        List<PhoneOrder> orders = OrderDao.getOrdersById(id);
        return orders;
    }

    @RequestMapping(value="order/status/{status}", method = RequestMethod.GET)
    public @ResponseBody List<PhoneOrder> getOrdersByStatus(@PathVariable("status") String status) {
        List<PhoneOrder> orders = OrderDao.getOrdersByStatus(status);
        return orders;
    }

    @RequestMapping(value="order", method = RequestMethod.POST)
    public ResponseEntity<PhoneOrder> createOrder(@RequestBody PhoneOrder order) {
        OrderDao.createOrder(order);
        return new ResponseEntity<PhoneOrder>(order, HttpStatus.OK);
    }

    @RequestMapping(value="order", method = RequestMethod.PUT)
    public ResponseEntity<PhoneOrder> updateOrder(@RequestBody PhoneOrder order) {
        List<PhoneOrder> updateOrder = new ArrayList<PhoneOrder>();
        updateOrder.add(order);
        OrderDao.updateOrders(updateOrder);
        return new ResponseEntity<PhoneOrder>(order, HttpStatus.OK);
    }

    @PostConstruct
    public void init() {

        ObjectMapper om = new ObjectMapper();

        InputStream inputStreamOrders = getClass().getResourceAsStream("/orders.json");
        //read file and insert events from the file to DB
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, PhoneOrder.class);
        try {
            List<PhoneOrder> orders = mapper.readValue(inputStreamOrders, collectionType);
            for (PhoneOrder order : orders) {
                OrderDao.createOrder(order);
            }
        } catch (FileNotFoundException exception) {
        } catch (IOException exception) {
        }


    }

}