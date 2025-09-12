package at.fhv.SimpleBankingSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}
