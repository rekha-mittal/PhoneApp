package com.integnology.phoneapp.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.integnology.phoneapp.controller.PhoneOrderController;
import com.integnology.phoneapp.model.PhoneOrder;
import com.integnology.phoneapp.service.PhoneOrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * Created by calvinmak on 5/18/15.
 */
public class TestPhoneOrderController {

    private PhoneOrderService phoneOrderService;
    private PhoneOrderController phoneOrderController;
    private PhoneOrder newOrder1;
    private PhoneOrder newOrder2;
    private UUID id1;
    private UUID id2;


    @Before
    public void setup() {
        phoneOrderService = mock(PhoneOrderService.class);
        phoneOrderController = new PhoneOrderController();
        phoneOrderController.setPhoneOrderService(phoneOrderService);
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
    public void getAllPhoneOrders_ReturnsAllOrders() {
        when(phoneOrderService.getAllPhoneOrders())
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List<PhoneOrder> results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        results.add(newOrder2);
                        return results;
                    }
                });
        List<PhoneOrder> results = phoneOrderController.getAllOrders();
        assertTrue(results.contains(newOrder1));
        assertTrue(results.contains(newOrder2));
        assertEquals(2,results.size());
    }

    @Test
    public void createPhoneOrder_ReturnsResponseEntityWithPhoneOrderAndOKStatus() {
        doNothing().when(phoneOrderService).createPhoneOrder(newOrder1);
        ResponseEntity<PhoneOrder> responseEntity = phoneOrderController.createOrder(newOrder1);
        assertEquals(newOrder1, responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void getPhoneOrderByID_ReturnsOrderCorrectOrder() {
        when(phoneOrderService.getPhoneOrdersById(id1))
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List<PhoneOrder> results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        return results;
                    }

                });
        List<PhoneOrder> results = phoneOrderController.getOrderById(id1);
        assertTrue(results.contains(newOrder1));
        assertEquals(1, results.size());
        assertEquals(id1,((PhoneOrder)results.get(0)).getId());
    }

    @Test
    public void getPhoneOrdersByStatus_ReturnOrderWithSpecifiedStatus() {
        when(phoneOrderService.getPhoneOrdersByStatus("pending.approval"))
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List<PhoneOrder> results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        return results;
                    }
                });
        List<PhoneOrder> results = phoneOrderController.getOrdersByStatus("pending.approval");
        assertTrue(results.contains(newOrder1));
        assertEquals(1, results.size());
        assertEquals("pending.approval",((PhoneOrder)results.get(0)).getStatus());
    }

    @Test
    public void updateOrder_ReturnsResponseEntityWithPhoneOrderAndOKStatus() {
        doNothing().when(phoneOrderService).updatePhoneOrders(anyList());
        ResponseEntity<PhoneOrder> responseEntity = phoneOrderController.updateOrder(newOrder1);
        assertEquals(newOrder1,responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

}








