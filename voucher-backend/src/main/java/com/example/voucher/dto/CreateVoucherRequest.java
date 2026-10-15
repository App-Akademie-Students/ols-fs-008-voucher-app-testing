package com.example.voucher.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateVoucherRequest(
        String code,
        BigDecimal amount,
        LocalDate expirationDate
) {


}
