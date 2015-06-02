package com.integnology.phoneapp.dao; /**
 * Created by calvinmak on 5/19/15.
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.integnology.phoneapp.dao.PhoneOrderDao;
import com.integnology.phoneapp.dao.PhoneOrderDaoImpl;
import com.integnology.phoneapp.model.PhoneOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class TestPhoneOrderDaoImpl {

    private PhoneOrderDao phoneOrderDao = new PhoneOrderDaoImpl();
    private PhoneOrder newOrder1;
    private PhoneOrder newOrder2;
    private UUID id1;
    private UUID id2;

    @Before
    public void setup() {
        phoneOrderDao.clearPhoneOrders();
        newOrder1 = new PhoneOrder();
        newOrder1.setFirstName("James");
        newOrder1.setLastName("Rege");
        newOrder1.setStreet("3945, Freedom Drive");
        newOrder1.setCity("Santa Clara");
        newOrder1.setZip("95051");
        newOrder1.setTimestamp(new Date());
        newOrder1.setPhone("");
        newOrder1.setStatus("pending.approval");
        id1 = UUID.randomUUID();
        newOrder1.setId(id1);

        newOrder2 = new PhoneOrder();
        newOrder2.setFirstName("Randy");
        newOrder2.setLastName("Gill");
        newOrder2.setStreet("12 Stelling Way");
        newOrder2.setCity("Cupertino");
        newOrder2.setZip("94086");
        newOrder2.setTimestamp(new Date());
        newOrder2.setPhone("(555) 555-5555");
        newOrder2.setStatus("pending.activation");
        id2 = UUID.randomUUID();
        newOrder2.setId(id2);
    }

    @Test
    public void updatePhoneOrders_CanUpdateExistingOrder() {
        phoneOrderDao.createPhoneOrder(newOrder1);
        newOrder1.setStatus("closed");
        List<PhoneOrder> orders = new ArrayList<PhoneOrder>();
        orders.add(newOrder1);
        phoneOrderDao.updatePhoneOrders(orders);
        PhoneOrder result = phoneOrderDao.getPhoneOrdersById(newOrder1.getId()).get(0);
        assertEquals("closed",result.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void updatePhoneOrders_UpdatingNonExistingOrderThrowsRuntimeException() {
        phoneOrderDao.createPhoneOrder(newOrder1);
        newOrder2.setStatus("closed");
        List<PhoneOrder> orders = new ArrayList<PhoneOrder>();
        orders.add(newOrder2);
        phoneOrderDao.updatePhoneOrders(orders);
    }

    @Test
    public void getAllPhoneOrders_ReturnsAllOrders() {
        phoneOrderDao.createPhoneOrder(newOrder1);
        phoneOrderDao.createPhoneOrder(newOrder2);
        List results = phoneOrderDao.getAllPhoneOrders();
        assertEquals(2,results.size());
        assertTrue(results.contains(newOrder1));
        assertTrue(results.contains(newOrder2));
    }

    @Test
    public void getPhoneOrdersById_ValidIdReturnsCorrectPhoneOrder() {
        phoneOrderDao.createPhoneOrder(newOrder1);
        id1 = newOrder1.getId();
        List results = phoneOrderDao.getPhoneOrdersById(id1);
        assertEquals(1, results.size());
        assertTrue(results.contains(newOrder1));
    }

    @Test
    public void getPhoneOrdersByStatus_ReturnsOrdersWithSpecifiedStatus() {
        phoneOrderDao.createPhoneOrder(newOrder1);
        phoneOrderDao.createPhoneOrder(newOrder2);
        List results = phoneOrderDao.getPhoneOrdersByStatus("pending.approval");
        assertEquals(1,results.size());
        assertEquals("pending.approval",((PhoneOrder)results.get(0)).getStatus());
    }

    @Test
    public void createOrder_CreatesAnOrder() {
        phoneOrderDao.createPhoneOrder(newOrder2);
        List results = phoneOrderDao.getAllPhoneOrders();
        assertTrue(results.contains(newOrder2));
    }
}
