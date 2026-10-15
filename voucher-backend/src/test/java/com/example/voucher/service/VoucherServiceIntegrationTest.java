package com.example.voucher.service;

import com.example.voucher.model.Voucher;
import com.example.voucher.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class VoucherServiceIntegrationTest {

    @Autowired
    private  VoucherService voucherService;
// Constructor injection is not needed here since we are using @Autowired for field injection. If you want to use constructor injection, you can uncomment the following code and remove the @Autowired annotation from the field.
//    public VoucherServiceIntegrationTest( VoucherService voucherService) {
//        this.voucherService = voucherService;
//    }

    @Autowired
    private VoucherRepository voucherRepository;



    @Test
    void shouldReturnVoucherWhenCodeExists() {
        // Test implementation goes here
        Voucher voucher = new Voucher("ABC123", new BigDecimal("20.20"), LocalDate.now(), false);
        voucherRepository.save(voucher);
        Voucher result = voucherService.getVoucher("ABC123");
        assertEquals("ABC123", result.getCode());
    }

}



