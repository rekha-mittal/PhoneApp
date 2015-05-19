import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.integnology.phoneapp.controller.PhoneOrderController;
import com.integnology.phoneapp.service.PhoneOrderService;
import com.integnology.phoneapp.model.PhoneOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.util.List;
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
        id1=UUID.randomUUID();
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
        id2=UUID.randomUUID();
        newOrder2.setId(id2);

    }

    @Test
    public void testGetAllOrders() {
        when(phoneOrderService.getAllOrders())
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List <PhoneOrder> results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        results.add(newOrder2);
                        return results;
                    }
                });
        List<PhoneOrder>expectedResult=new ArrayList<PhoneOrder>();
        expectedResult.add(newOrder1);
        expectedResult.add(newOrder2);
        assertTrue(phoneOrderController.getAllOrders().containsAll(expectedResult));
    }

    @Test
    public void testCreateOrder() {
        doNothing().when(phoneOrderService).createOrder(newOrder1);
        ResponseEntity<PhoneOrder> responseEntity=phoneOrderController.createOrder(newOrder1);
        assertEquals(newOrder1, responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    public void testGetOrderByID() {
        when(phoneOrderService.getOrdersById(id1))
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List<PhoneOrder> results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        return results;
                    }

                });
        assertTrue(phoneOrderController.getOrderById(id1).contains(newOrder1));
        assertEquals(1,phoneOrderController.getOrderById(id1).size());
    }

    @Test
    public void testGetOrdersByStatus() {
        when(phoneOrderService.getOrdersByStatus("pending.activation"))
                .thenAnswer(new Answer<List>() {
                    @Override
                    public List<PhoneOrder> answer(InvocationOnMock invocation) {
                        List<PhoneOrder>results = new ArrayList<PhoneOrder>();
                        results.add(newOrder1);
                        return results;
                    }
                });
        assertTrue(phoneOrderController.getOrdersByStatus("pending.activation").contains(newOrder1));
        assertEquals(1, phoneOrderController.getOrdersByStatus("pending.activation").size());
    }

    @Test
    public void testUpdateOrder() {
        doNothing().when(phoneOrderService).updateOrders(anyList());
        ResponseEntity<PhoneOrder> responseEntity=phoneOrderController.updateOrder(newOrder1);
        assertEquals(newOrder1,responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

}








