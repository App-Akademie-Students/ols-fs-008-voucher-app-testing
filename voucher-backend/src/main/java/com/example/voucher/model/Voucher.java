package com.example.voucher.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "VOUCHER")
public class Voucher {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(nullable = false, unique = true)
    private String code;
    private BigDecimal amount;
    private LocalDate expirationDate;
    private boolean redeemed;
    public Voucher(
            String code,
            BigDecimal amount,
            LocalDate expirationDate,
            boolean redeemed
    ) {
        this.code = code;
        this.amount = amount;
        this.expirationDate = expirationDate;
        this.redeemed = redeemed;
    }

    public void redeem() {
        this.redeemed = true;
    }
}
