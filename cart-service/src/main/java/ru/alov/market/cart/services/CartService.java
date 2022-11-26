package ru.alov.market.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.ProductDto;
import ru.alov.market.cart.integrations.ProductServiceIntegration;
import ru.alov.market.cart.utils.Cart;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductServiceIntegration productServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

    public Cart getCurrentCart(String cartId) {
        if (!redisTemplate.hasKey(cartId)) {
            Cart cart = new Cart();
            redisTemplate.opsForValue().set(cartId, cart);
        }
        return (Cart) redisTemplate.opsForValue().get(cartId);
    }

    public void addToCart(String cartId, Long productId) {
        productServiceIntegration.findById(productId).subscribe(productDto -> execute(cartId, cart -> cart.add(productDto)));
    }

    public void removeFromCart(String cartId, Long productId) {
        execute(cartId, cart -> cart.remove(productId));
    }

    public void changeProductQuantity(String cartId, Long id, Integer delta) {
        execute(cartId, cart -> cart.changeProductQuantity(id, delta));
    }

    public void setProductQuantity(String cartId, Long id, Integer newQuantity) {
        execute(cartId, cart -> cart.setProductQuantity(id, newQuantity));
    }

    private void execute(String cartId, Consumer<Cart> action) {
        Cart cart = getCurrentCart(cartId);
        action.accept(cart);
        redisTemplate.opsForValue().set(cartId, cart);
    }

    public void mergeCart(String username, String guestCartId) {
        Cart guestCart = getCurrentCart(guestCartId);
        if (guestCart.getItems().isEmpty()) return;
        Cart userCart = getCurrentCart(username);
        userCart.merge(guestCart);
        updateCart(username, userCart);
        updateCart(guestCartId, guestCart);
    }

    public void updateCart(String cartId, Cart cart) {
        redisTemplate.opsForValue().set(cartId, cart);
    }

    public void clearCart(String cartId) {
        execute(cartId, Cart::clear);
    }

}
