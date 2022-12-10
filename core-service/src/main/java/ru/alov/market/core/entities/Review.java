package ru.alov.market.core.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "username")
    private String username;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Review(Product product, String username, Integer grade, String description) {
        this.product = product;
        this.username = username;
        this.grade = grade;
        this.description = description;
    }
}
