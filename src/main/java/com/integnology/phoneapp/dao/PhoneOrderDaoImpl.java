package com.integnology.phoneapp.dao;

import com.integnology.phoneapp.model.PhoneOrder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * Created by rekhamittal on 5/5/15.
 */

@Repository
public class PhoneOrderDaoImpl implements PhoneOrderDao {
    private List<PhoneOrder> phoneOrders = new ArrayList<PhoneOrder>();
    private static final String ORDER_FILE = "/orders.json";
    private static final String ORDER_ERR_MSG = "Order not exist or there are multiple phoneOrders with id:";
    private static final String FILE_ERR_MSG = "Error reading file";

    /**
     *Initializes the in memory repository with the default phone orders.
     */
    public PhoneOrderDaoImpl() {
        InputStream inputStreamOrders = getClass().getResourceAsStream(ORDER_FILE);
        //read file and insert events from the file to DB
        ObjectMapper objectMapper = new ObjectMapper();
        final CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, PhoneOrder.class);
        try {
            List<PhoneOrder> phoneOrders = objectMapper.readValue(inputStreamOrders, collectionType);
            for (PhoneOrder phoneOrder : phoneOrders) {
                createPhoneOrder(phoneOrder);
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_ERR_MSG, e);
        } finally {
            try {
                inputStreamOrders.close();
            } catch (IOException e) {
                throw new RuntimeException(FILE_ERR_MSG, e);
            }
        }
    }

    /**
     * Updates the phone order to the repository.
     * @param phoneOrders List of PhoneOrder to be updated to the repo
     */
    public void updatePhoneOrders(List<PhoneOrder> phoneOrders) {
        for (PhoneOrder phoneOrder : phoneOrders) {
            UUID id = phoneOrder.getId();
            List<PhoneOrder> existingOrders = getPhoneOrdersById(id);
            if (existingOrders.isEmpty() || existingOrders.size() > 1) {
                throw new RuntimeException(ORDER_ERR_MSG + id);
            }
            this.phoneOrders.remove(existingOrders.get(0));
            this.phoneOrders.add(phoneOrder);
        }
    }

    /**
     * Gets the phone orders from the repository.
     * @param id order id of the PhoneOrder.
     * @return  the list of PhoneOrder
     */
    public List<PhoneOrder> getPhoneOrdersById(final UUID id) {
        if (id == null) {
            String test = "TEST";
        }
        return findPhoneOrders(new Matcher<PhoneOrder>() {
            public boolean matches(PhoneOrder phoneOrder) {
                if (phoneOrder.getId().equals(id)) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Gets the phone orders from the repository.
     * @param status order id of the PhoneOrder.
     * @return the list of PhoneOrder
     */
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

    /**
     * Deletes the phone orders from the repository.
     */
    public void clearPhoneOrders() {
        phoneOrders.clear();
    }

    /**
     * Adds the phone order to the repository
     * Creates a unique id for the order.
     * @param phoneOrder new PhoneOrder to be created
     */
    public void createPhoneOrder(PhoneOrder phoneOrder) {
        phoneOrder.setId(UUID.randomUUID());
        phoneOrders.add(phoneOrder);
    }

    /**
     * Gets all the phone orders from the repository.
     * @return the list of PhoneOrder
     */
    public List<PhoneOrder> getAllPhoneOrders() {
        return phoneOrders;
    }

    /**
     * Search the repository for a phoneorder based on the interface.
     * @param m interface with the matches implementation
     * @return the list of PhoneOrder
     */
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
