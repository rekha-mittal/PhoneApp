import com.integnology.phoneapp.controller.PhoneOrderController;
import com.integnology.phoneapp.dao.OrderDaoImpl;
import com.integnology.phoneapp.model.PhoneOrder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by calvinmak on 5/18/15.
 */
public class TestPhoneOrderController {

    private static PhoneOrderController phoneOrderController;
    private static OrderDaoImpl orderDao;
    private PhoneOrder newOrder1;
    private PhoneOrder newOrder2;
    
    @BeforeClass
    public static void init() {
        orderDao=new OrderDaoImpl();
        phoneOrderController=new PhoneOrderController();
        phoneOrderController.setOrderDao(orderDao);
        
    }
    
    @Before
    public void setup() {
        orderDao.clearOrders();
        newOrder1=new PhoneOrder();
        newOrder1.setFirstName("James");
        newOrder1.setLastName("Rege");
        newOrder1.setStreet("3945, Freedom Drive");
        newOrder1.setCity("Santa Clara");
        newOrder1.setZip("95051");
        newOrder1.setTimestamp(new Date());
        newOrder1.setPhone("");
        newOrder1.setStatus("pending.approval");

        newOrder2=new PhoneOrder();
        newOrder2.setFirstName("Randy");
        newOrder2.setLastName("Gill");
        newOrder2.setStreet("12 Stelling Way");
        newOrder2.setCity("Cupertion");
        newOrder2.setZip("94086");
        newOrder2.setTimestamp(new Date());
        newOrder2.setPhone("(555) 555-5555");
        newOrder2.setStatus("pending.activation");
    }

    @Test public void testCreateOrder() {
        ResponseEntity response=phoneOrderController.createOrder(newOrder1);
        UUID id=((PhoneOrder)(response.getBody())).getId();
        List<PhoneOrder> results=phoneOrderController.getOrderById(id);
        assertTrue(results.contains(newOrder1));
    }

    @Test public void testGetAllOrders() {
        phoneOrderController.createOrder(newOrder1);
        phoneOrderController.createOrder(newOrder2);
        List results=phoneOrderController.getAllOrders();
        List expectedResults=new ArrayList<PhoneOrder>();
        expectedResults.add(newOrder1);
        expectedResults.add(newOrder2);
        assertTrue(results.containsAll(expectedResults));
    }

    @Test public void testUpdateOrderStatusToPendingApproval() {
        ResponseEntity response=phoneOrderController.createOrder(newOrder2);
        newOrder2.setStatus("pending.approval");
        phoneOrderController.updateOrder(newOrder2);
        UUID id=((PhoneOrder)(response.getBody())).getId();
        List<PhoneOrder> result=phoneOrderController.getOrderById(id);
        PhoneOrder resultOrder=(result.get(0));
        assertEquals("pending.approval", resultOrder.getStatus());
    }

    @Test public void testUpdateOrderStatusToClosed() {
        ResponseEntity response=phoneOrderController.createOrder(newOrder2);
        newOrder2.setStatus("closed");
        phoneOrderController.updateOrder(newOrder2);
        UUID id=((PhoneOrder)(response.getBody())).getId();
        List<PhoneOrder> result=phoneOrderController.getOrderById(id);
        PhoneOrder resultOrder=(result.get(0));
        assertEquals("closed", resultOrder.getStatus());
    }

    @Test public void testUpdateOrderStatusToPendingActivation() {
        ResponseEntity response=phoneOrderController.createOrder(newOrder1);
        newOrder1.setStatus("pending.activation");
        phoneOrderController.updateOrder(newOrder1);
        UUID id=((PhoneOrder)(response.getBody())).getId();
        List<PhoneOrder> result=phoneOrderController.getOrderById(id);
        PhoneOrder resultOrder=(result.get(0));
        assertEquals("pending.activation", resultOrder.getStatus());
    }
    @Test(expected = RuntimeException.class) public void testUpdateOrderWithNonExistingOrderThrowsRuntimeException() {
        phoneOrderController.createOrder(newOrder1);
        phoneOrderController.updateOrder(newOrder2);
    }

    @Test public void testGetOrderByStatus() {
        phoneOrderController.createOrder(newOrder1);
        phoneOrderController.createOrder(newOrder2);
        List<PhoneOrder> results=phoneOrderController.getOrdersByStatus("pending.activation");
        assertTrue((results.size() == 1) && (results.contains(newOrder2)));
    }

    @Test public void testGetOrderById() {
        ResponseEntity response=phoneOrderController.createOrder(newOrder1);
        UUID id=((PhoneOrder)(response.getBody())).getId();
        List<PhoneOrder> results=phoneOrderController.getOrderById(id);
        assertTrue(results.contains(newOrder1));
    }
}
