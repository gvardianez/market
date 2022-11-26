package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.OrderDetailsDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.entities.Order;
import ru.alov.market.core.entities.OrderItem;
import ru.alov.market.core.exceptions.FieldValidationException;
import ru.alov.market.core.integrations.CartServiceIntegration;
import ru.alov.market.core.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartServiceIntegration cartServiceIntegration;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public Mono<Order> createNewOrder(String username, String email, OrderDetailsDto orderDetailsDto) {
        if (orderDetailsDto.getPhone() == null || orderDetailsDto.getAddress() == null)
            throw new FieldValidationException("Необходимо указать телефон и адрес");
        return cartServiceIntegration.getCurrentUserCart(username).map(cart -> {
            if (cart.getItems().isEmpty()) {
                throw new IllegalStateException("Нельзя оформить заказ для пустой корзины");
            }

            Order order = new Order();
            if (orderDetailsDto.getEmail() == null) {
                order.setEmail(email);
            } else order.setEmail(orderDetailsDto.getEmail());
            order.setTotalPrice(cart.getTotalPrice());
            order.setUsername(username);
            order.setItems(new ArrayList<>());
            order.setAddress(orderDetailsDto.getAddress());
            order.setPhone(orderDetailsDto.getPhone());
            Order finalOrder = order;
            cart.getItems().forEach(ci -> {
                OrderItem oi = new OrderItem();
                oi.setOrder(finalOrder);
                oi.setPrice(ci.getPrice());
                oi.setQuantity(ci.getQuantity());
                oi.setPricePerProduct(ci.getPricePerProduct());
                oi.setProduct(productService.findById(ci.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found")));
                finalOrder.getItems().add(oi);
            });
            order.setStatus(Order.OrderStatus.CREATED.name());
            order = orderRepository.save(order);
            return order;
        }).doOnSuccess(order -> cartServiceIntegration.clearCart(username));
    }

    @Transactional
    public Order changeOrderStatus(Long id, Order.OrderStatus orderStatus) {
        Order order = findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Order not found");
        });
        order.setStatus(orderStatus.name());
        return order;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findUserOrders(String username) {
        return orderRepository.findAllByUsername(username);
    }

}
