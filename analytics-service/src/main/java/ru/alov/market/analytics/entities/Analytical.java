package ru.alov.market.analytics.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "analytical_data")
public class Analytical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "username")
    private String username;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_per_product")
    private BigDecimal pricePerProduct;

    @Column(name = "bought_at")
    private LocalDateTime boughtAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Analytical( Long productId, Long orderId, String username, Integer quantity, BigDecimal pricePerProduct, LocalDateTime boughtAt) {
        this.productId = productId;
        this.orderId = orderId;
        this.username = username;
        this.quantity = quantity;
        this.pricePerProduct = pricePerProduct;
        this.boughtAt = boughtAt;
    }

}
