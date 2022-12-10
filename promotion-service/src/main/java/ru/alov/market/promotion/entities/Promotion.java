package ru.alov.market.promotion.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotion")
@NoArgsConstructor
@Data
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<PromotionItem> promotionItems;

    @Column(name = "start_at")
    private LocalDateTime startedAt;

    @Column(name = "end_at")
    private LocalDateTime endedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
