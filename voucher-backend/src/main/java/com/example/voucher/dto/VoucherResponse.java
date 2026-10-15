package com.example.voucher.dto;

import com.example.voucher.model.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VoucherResponse(
        Long id,
        String code,
        BigDecimal amount,
        LocalDate expirationDate,
        boolean redeemed
) {


    public static VoucherResponse from(Voucher voucher) {
        return new VoucherResponse(
                voucher.getId(),
                voucher.getCode(),
                voucher.getAmount(),
                voucher.getExpirationDate(),
                voucher.isRedeemed());
    }
}
