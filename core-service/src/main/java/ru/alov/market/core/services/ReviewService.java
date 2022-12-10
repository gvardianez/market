package ru.alov.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alov.market.api.dto.ReviewDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.core.entities.Order;
import ru.alov.market.core.entities.Product;
import ru.alov.market.core.entities.Review;
import ru.alov.market.core.repositories.ReviewRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final OrderService orderService;

    public Review createReview(String username, ReviewDto reviewDto) {
        Product product = productService.findById(reviewDto.getProductId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("Продукт не найден, id = " + reviewDto.getProductId());
        });
        if (orderService.findOrdersByProductIdAndUsernameAndOrderStatus(username, reviewDto.getProductId(), Order.OrderStatus.CREATED.name()).isEmpty()) {
            throw new IllegalStateException("Вы не приобретали у нас данный товар");
        }
        if (findReviewByUsernameAndProductId(username, product.getId()).isPresent()) {
            throw new IllegalStateException("Вы уже оставили отзыв да данный товар");
        }
        Review review = new Review(product, username, reviewDto.getGrade(), reviewDto.getDescription());
        return reviewRepository.save(review);
    }

    public Optional<Review> findReviewByUsernameAndProductId(String username, Long productId) {
        return reviewRepository.findByUsernameAndProductId(username, productId);
    }

}
