package ru.alov.market.cart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.api.exception.FieldValidationException;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.cart.integrations.CoreServiceIntegration;
import ru.alov.market.cart.services.CartService;
import ru.alov.market.cart.utils.Cart;

import java.math.BigDecimal;

@SpringBootTest(classes = CartService.class)
public class CartServiceTests {

    @Autowired
    private CartService cartService;

    @MockBean
    private CoreServiceIntegration coreServiceIntegration;

    @MockBean
    private RedisTemplate<String,Object> redisTemplate;

    @MockBean
    private ValueOperations<String,Object> valueOperations;

    public static ProductDto milkDto;
    public static ProductDto breadDto;
    public static ProductDto cheeseDto;
    public static Cart testCart;
    public static String cartId = "test_cart";

    @BeforeAll
    public static void initProductsDto() {
        testCart = new Cart();
        milkDto = new ProductDto(1L, "Молоко", BigDecimal.valueOf(50.00), "Еда");
        breadDto = new ProductDto(2L, "Хлеб", BigDecimal.valueOf(30.00), "Еда");
        cheeseDto = new ProductDto(3L, "Сыр", BigDecimal.valueOf(150.00), "Еда");
    }

    @BeforeEach
    public void clearCart() {
        Mockito.doReturn(true)
               .when(redisTemplate).hasKey(cartId);
        Mockito.doReturn(valueOperations)
               .when(redisTemplate)
               .opsForValue();
        Mockito.doReturn(testCart)
               .when(valueOperations)
               .get(cartId);
        cartService.clearCart(cartId);
    }

    @Test
    public void addToCartTest() {
        Mockito.doReturn(milkDto)
                .when(coreServiceIntegration)
                .findById(milkDto.getId());
        cartService.addToCart(cartId, milkDto.getId());
        cartService.addToCart(cartId, milkDto.getId());
        cartService.addToCart(cartId, milkDto.getId());
        Assertions.assertEquals(1, cartService.getCurrentCart(cartId).getItems().size());
        Assertions.assertEquals(3, cartService.getCurrentCart(cartId).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(150.00), cartService.getCurrentCart(cartId).getTotalPrice());
        Mockito.doReturn(breadDto)
                .when(coreServiceIntegration)
                .findById(breadDto.getId());
        cartService.addToCart(cartId, breadDto.getId());
        Assertions.assertEquals(2, cartService.getCurrentCart(cartId).getItems().size());
        Assertions.assertEquals(BigDecimal.valueOf(180.00), cartService.getCurrentCart(cartId).getTotalPrice());
    }

    @Test
    public void changeProductQuantityTest() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.changeProductQuantity(cartId, 1L, 10));

        Mockito.doReturn(milkDto)
                .when(coreServiceIntegration)
                .findById(milkDto.getId());
        cartService.addToCart(cartId, milkDto.getId());

        cartService.changeProductQuantity(cartId, milkDto.getId(), 2);
        Assertions.assertEquals(3, cartService.getCurrentCart(cartId).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(150.00), cartService.getCurrentCart(cartId).getTotalPrice());

        cartService.changeProductQuantity(cartId, milkDto.getId(), -1);
        Assertions.assertEquals(2, cartService.getCurrentCart(cartId).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), cartService.getCurrentCart(cartId).getTotalPrice());

        cartService.changeProductQuantity(cartId, milkDto.getId(), -5);
        Assertions.assertEquals(0, cartService.getCurrentCart(cartId).getItems().size());
        Assertions.assertEquals(BigDecimal.ZERO, cartService.getCurrentCart(cartId).getTotalPrice());
    }

    @Test
    public void setProductQuantityTest() {
        Assertions.assertThrows(FieldValidationException.class, () -> cartService.setProductQuantity(cartId, cheeseDto.getId(), -5));

        Mockito.doReturn(cheeseDto)
                .when(coreServiceIntegration)
                .findById(cheeseDto.getId());
        cartService.addToCart(cartId, cheeseDto.getId());

        cartService.setProductQuantity(cartId, cheeseDto.getId(), 10);
        Assertions.assertEquals(10, cartService.getCurrentCart(cartId).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(1500.00), cartService.getCurrentCart(cartId).getTotalPrice());

        cartService.setProductQuantity(cartId, cheeseDto.getId(), 0);
        Assertions.assertEquals(0, cartService.getCurrentCart(cartId).getItems().size());
        Assertions.assertEquals(BigDecimal.ZERO, cartService.getCurrentCart(cartId).getTotalPrice());
    }

    @Test
    public void removeProductByIdTest() {
        Mockito.doReturn(cheeseDto)
                .when(coreServiceIntegration)
                .findById(cheeseDto.getId());
        cartService.addToCart(cartId, cheeseDto.getId());

        Mockito.doReturn(milkDto)
                .when(coreServiceIntegration)
                .findById(milkDto.getId());
        cartService.addToCart(cartId, milkDto.getId());

        cartService.removeFromCart(cartId, cheeseDto.getId());
        Assertions.assertEquals(1, cartService.getCurrentCart(cartId).getItems().size());
    }


}