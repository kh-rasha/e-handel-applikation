package se.yh.ehandel.domain.entity;

import jakarta.persistence.*;
import se.yh.ehandel.domain.enums.PaymentMethod;
import se.yh.ehandel.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime timestamp;
}

