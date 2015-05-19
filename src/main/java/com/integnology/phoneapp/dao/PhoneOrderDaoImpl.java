package com.integnology.phoneapp.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.integnology.phoneapp.model.PhoneOrder;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rekhamittal on 5/5/15.
 */

@Repository
public class PhoneOrderDaoImpl implements PhoneOrderDao {
    private List<PhoneOrder> orders = new ArrayList<PhoneOrder>();

    public PhoneOrderDaoImpl() {

        ObjectMapper om = new ObjectMapper();

        InputStream inputStreamOrders = getClass().getResourceAsStream("/orders.json");
        //read file and insert events from the file to DB
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, PhoneOrder.class);
        try {
            List<PhoneOrder> orders = mapper.readValue(inputStreamOrders, collectionType);
            for (PhoneOrder order : orders) {
                createOrder(order);
            }
        } catch (FileNotFoundException exception) {
        } catch (IOException exception) {
        }
    }

    public void updateOrders(List<PhoneOrder> phoneOrders) {
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

    public List<PhoneOrder> getOrdersById(final UUID id) {
        return findOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder order) {
                if (order.getId().equals(id)) {
                    return true;
                }
                return false;
            }
        });
    }

    public List<PhoneOrder> getOrdersByStatus(final String status) {
        return findOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder order) {
                if (order.getStatus().equalsIgnoreCase(status)) {
                    return true;
                }
                return false;
            }
        });
    }

    public void clearOrders() {
        orders.clear();
    }

    public void createOrder(PhoneOrder order) {
        order.setId(UUID.randomUUID());
        orders.add(order);
    }

    public List<PhoneOrder> getAllOrders() {
        return orders;
    }

    public List<PhoneOrder> findOrders(Matcher<PhoneOrder> m) {
        List<PhoneOrder> result = new ArrayList<PhoneOrder>();
        for (PhoneOrder order : orders) {
            if (m.matches(order)) {
                result.add(order);
            }
        }
        return result;
    }


}
