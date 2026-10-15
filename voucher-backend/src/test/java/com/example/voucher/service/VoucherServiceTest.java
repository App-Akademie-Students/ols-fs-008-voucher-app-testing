package com.example.voucher.service;

import com.example.voucher.model.Voucher;
import com.example.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
// @SpringBootTest - nicht machen, da wir keine Spring-Komponenten testen, sondern nur die Logik der VoucherService-Klasse. Wir können stattdessen einfache Unit-Tests schreiben.

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;
    @InjectMocks
    private VoucherService voucherService;

    @Test
    void shouldReturnVoucherWhenCodeExists() {//Fix: Rename Create to  Return
        Voucher voucher = new Voucher();

        voucher.setCode("ABC123");
        when(voucherRepository.save(voucher)).thenReturn(voucher);// FIXME keine Datenbank - save() sollte nicht aufgerufen werden, da wir nur den Code testen wollen. Stattdessen sollten wir die Logik der VoucherService-Klasse testen, die den Voucher erstellt und zurückgibt.

    }

    @Test
    void getVoucher() {
    }
}