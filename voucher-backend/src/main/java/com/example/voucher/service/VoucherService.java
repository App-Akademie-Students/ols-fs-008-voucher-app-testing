package com.example.voucher.service;


import com.example.voucher.model.Voucher;
import com.example.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("Voucher not found"));//TODO VoucherNotFound
    }

}
