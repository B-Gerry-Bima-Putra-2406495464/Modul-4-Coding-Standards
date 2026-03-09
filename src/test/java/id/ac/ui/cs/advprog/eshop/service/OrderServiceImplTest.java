package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;

    List<Order> orders;

    @BeforeEach
    void setup() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orders = new ArrayList<>();

        Order order1 = new Order("13652556-0128-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);
    }

    @Test
    void testCreateOrder() {
        Order order = orders.get(0);
        doReturn(null).when(orderRepository).findById(order.getId());
        doReturn(order).when(orderRepository).save(order);

        Order result = orderService.createOrder(order);
        verify(orderRepository, times(1)).save(order);
        assertEquals(order.getId(), result.getId());
    }

    @Test
    void testUpdateStatusInvalidOrderId() {
        doReturn(null).when(orderRepository).findById("zczc");
        assertThrows(NoSuchElementException.class, () ->
                orderService.updateStatus("zczc", OrderStatus.SUCCESS.getValue()));
    }

    @Test
    void testFindByIdIfIdFound() {
        Order order = orders.get(0);
        doReturn(order).when(orderRepository).findById(order.getId());

        Order result = orderService.findById(order.getId());

        assertEquals(order.getId(), result.getId());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        doReturn(null).when(orderRepository).findById("zczc");

        Order result = orderService.findById("zczc");

        assertNull(result);
    }

    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        Order order = orders.get(0);
        doReturn(orders).when(orderRepository).findAllByAuthor(order.getAuthor());

        List<Order> results = orderService.findAllByAuthor(order.getAuthor());

        for (Order result : results) {
            assertEquals(order.getAuthor(), result.getAuthor());
        }
        assertEquals(1, results.size());
    }

    @Test
    void testFindAllByAuthorIfAllLowercase() {
        Order order = orders.get(0);
        String lowerAuthor = order.getAuthor().toLowerCase();

        doReturn(new ArrayList<Order>()).when(orderRepository).findAllByAuthor(lowerAuthor);

        List<Order> results = orderService.findAllByAuthor(lowerAuthor);

        assertTrue(results.isEmpty());
    }
}
