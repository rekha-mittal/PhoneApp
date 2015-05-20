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
    private List<PhoneOrder> phoneOrders = new ArrayList<PhoneOrder>();

    public PhoneOrderDaoImpl() {
        ObjectMapper om = new ObjectMapper();

        InputStream inputStreamOrders = getClass().getResourceAsStream("/orders.json");
        //read file and insert events from the file to DB
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, PhoneOrder.class);
        try {
            List<PhoneOrder> phoneOrders = mapper.readValue(inputStreamOrders, collectionType);
            for (PhoneOrder phoneOrder : phoneOrders) {
                createPhoneOrder(phoneOrder);
            }
        } catch (FileNotFoundException exception) {
        } catch (IOException exception) {
        }
    }

    public void updatePhoneOrders(List<PhoneOrder> phoneOrders) {
        for (PhoneOrder phoneOrder : phoneOrders) {
            UUID id = phoneOrder.getId();
            List<PhoneOrder> existingOrders = getPhoneOrdersById(id);
            if (existingOrders.isEmpty() || existingOrders.size() > 1) {
                throw new RuntimeException("Order not exist or there are multiple phoneOrders with id: " + id);
            }
            this.phoneOrders.remove(existingOrders.get(0));
            this.phoneOrders.add(phoneOrder);
        }
    }

    public List<PhoneOrder> getPhoneOrdersById(final UUID id) {
        return findPhoneOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder phoneOrder) {
                if (phoneOrder.getId().equals(id)) {
                    return true;
                }
                return false;
            }
        });
    }

    public List<PhoneOrder> getPhoneOrdersByStatus(final String status) {
        return findPhoneOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder phoneOrder) {
                if (phoneOrder.getStatus().equalsIgnoreCase(status)) {
                    return true;
                }
                return false;
            }
        });
    }

    public void clearPhoneOrders() {
        phoneOrders.clear();
    }

    public void createPhoneOrder(PhoneOrder phoneOrder) {
        phoneOrder.setId(UUID.randomUUID());
        phoneOrders.add(phoneOrder);
    }

    public List<PhoneOrder> getAllPhoneOrders() {
        return phoneOrders;
    }

    public List<PhoneOrder> findPhoneOrders(Matcher<PhoneOrder> m) {
        List<PhoneOrder> result = new ArrayList<PhoneOrder>();
        for (PhoneOrder phoneOrder : phoneOrders) {
            if (m.matches(phoneOrder)) {
                result.add(phoneOrder);
            }
        }
        return result;
    }


}
