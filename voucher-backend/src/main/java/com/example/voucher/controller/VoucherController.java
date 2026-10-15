package com.example.voucher.controller;

import com.example.voucher.dto.CreateVoucherRequest;
import com.example.voucher.dto.VoucherResponse;
import com.example.voucher.model.Voucher;
import com.example.voucher.service.VoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // POST /api/vouchers
    // Body: {"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoucherResponse create(@RequestBody CreateVoucherRequest request) {
        Voucher voucher = new Voucher(
                request.code(),
                request.amount(),
                request.expirationDate(),
                false
        );
        return VoucherResponse.from(voucherService.createVoucher(voucher));
    }

    // GET /api/vouchers/ABC123
    @GetMapping("/{code}")
    public VoucherResponse get(@PathVariable String code) {
        return VoucherResponse.from(voucherService.getVoucher(code));
    }
}
