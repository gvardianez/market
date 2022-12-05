package ru.alov.market.core.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.alov.market.core.entities.Category;
import ru.alov.market.core.entities.Order;
import ru.alov.market.core.entities.OrderItem;
import ru.alov.market.core.entities.Product;
import ru.alov.market.core.repositories.OrderRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void cascadeSaveOrderTest() {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Еда");
        category.setProducts(Collections.emptyList());

        Product productButter = new Product();
        productButter.setId(1L);
        productButter.setTitle("Масло");
        productButter.setPrice(BigDecimal.valueOf(50));
        productButter.setCategory(category);

        Product productBread = new Product();
        productBread.setId(2L);
        productBread.setTitle("Хлеб");
        productBread.setPrice(BigDecimal.valueOf(25));
        productBread.setCategory(category);

        Order order = new Order("Vasya Dudkin", List.of(new OrderItem(productBread, 3), new OrderItem(productButter, 2)), BigDecimal.valueOf(175));
        order = orderRepository.save(order);
        Assertions.assertEquals(1L, order.getId());
        Assertions.assertNotNull(order.getItems().get(0).getId());

    }

}