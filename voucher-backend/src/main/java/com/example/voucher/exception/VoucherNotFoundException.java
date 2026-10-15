package com.example.voucher.exception;

public class VoucherNotFoundException extends RuntimeException {

    public VoucherNotFoundException(String code) {
        super("Voucher not found: " + code);
    }
}
