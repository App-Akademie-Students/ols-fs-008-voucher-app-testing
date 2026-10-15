package com.example.voucher.service;


import com.example.voucher.exception.VoucherNotFoundException;
import com.example.voucher.model.Voucher;
import com.example.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Voucher createVoucher(Voucher voucher){

        return voucherRepository.save(voucher);
    }

    public Voucher getVoucher(String code){
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new VoucherNotFoundException(code));
    }

    public Voucher redeemVoucher(String code){
        Voucher voucher = getVoucher(code);

        if (voucher.isRedeemed()) {
            throw new IllegalStateException("Voucher already redeemed");
        }
        if (voucher.getExpirationDate() != null && voucher.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Voucher expired");
        }

        voucher.redeem();
        return voucherRepository.save(voucher);
    }

}
