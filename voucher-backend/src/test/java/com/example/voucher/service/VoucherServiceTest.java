package com.example.voucher.service;

import com.example.voucher.model.Voucher;
import com.example.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// @SpringBootTest - nicht machen, da wir keine Spring-Komponenten testen, sondern nur die Logik der VoucherService-Klasse. Wir können stattdessen einfache Unit-Tests schreiben.

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;
    @InjectMocks
    private VoucherService voucherService;

    @Test
    @DisplayName("should return voucher when code exists")
    void shouldReturnVoucherWhenCodeExists() {//Fix: Rename Create to  Return
        Voucher voucher = new Voucher();

        voucher.setCode("ABC123");
        //when(voucherRepository.save(voucher)).thenReturn(voucher);// FIXME keine Datenbank - save() sollte nicht aufgerufen werden, da wir nur den Code testen wollen. Stattdessen sollten wir die Logik der VoucherService-Klasse testen, die den Voucher erstellt und zurückgibt.


        when(voucherRepository.findByCode("ABC123"))
                .thenReturn(Optional.of(voucher));

        Voucher result = voucherService.getVoucher("ABC123");

        assertEquals("ABC123", result.getCode());

        // when (...) = Umgebung vorbereiten
        // voucherService... = das eigentliche Testobjekt aufrufen
        // assert... = Ergebnis prüfen
    }
    @Test
    void shouldThrowExceptionWhenVoucherDoesNotExist() {
        when(voucherRepository.findByCode("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(
                //   VoucherNotFoundException.class,
                RuntimeException.class,
                () -> voucherService.getVoucher("UNKNOWN")
        );
    }

    // ---- redeemVoucher ----

    private Voucher voucher(boolean redeemed, LocalDate expirationDate) {
        return new Voucher("ABC123", new BigDecimal("50.00"), expirationDate, redeemed);
    }

    @Test
    @DisplayName("should redeem voucher when valid")
    void shouldRedeemVoucherWhenValid() {
        Voucher voucher = voucher(false, LocalDate.now().plusDays(1));
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(Voucher.class))).thenAnswer(i -> i.getArgument(0));

        Voucher result = voucherService.redeemVoucher("ABC123");

        assertTrue(result.isRedeemed());
        verify(voucherRepository).save(voucher);
    }

    @Test
    @DisplayName("should throw when voucher is already redeemed")
    void shouldThrowWhenVoucherAlreadyRedeemed() {
        Voucher voucher = voucher(true, LocalDate.now().plusDays(1));
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));

        assertThrows(IllegalStateException.class, () -> voucherService.redeemVoucher("ABC123"));

        verify(voucherRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw when voucher is expired")
    void shouldThrowWhenVoucherExpired() {
        Voucher voucher = voucher(false, LocalDate.now().minusDays(1));
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));

        assertThrows(IllegalStateException.class, () -> voucherService.redeemVoucher("ABC123"));

        assertFalse(voucher.isRedeemed());
        verify(voucherRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw when voucher to redeem does not exist")
    void shouldThrowWhenVoucherToRedeemNotFound() {
        when(voucherRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.redeemVoucher("UNKNOWN"));

        verify(voucherRepository, never()).save(any());
    }

    @Test
    @DisplayName("should redeem voucher when expiration date is today")
    void shouldRedeemVoucherWhenExpirationDateIsToday() {
        Voucher voucher = voucher(false, LocalDate.now());
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(Voucher.class))).thenAnswer(i -> i.getArgument(0));

        Voucher result = voucherService.redeemVoucher("ABC123");

        assertTrue(result.isRedeemed());
    }

    @Test
    @DisplayName("should redeem voucher when expiration date is null")
    void shouldRedeemVoucherWhenExpirationDateIsNull() {
        Voucher voucher = voucher(false, null);
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(Voucher.class))).thenAnswer(i -> i.getArgument(0));

        Voucher result = voucherService.redeemVoucher("ABC123");

        assertTrue(result.isRedeemed());
    }

    @Test
    @DisplayName("should not redeem voucher twice")
    void shouldNotRedeemVoucherTwice() {
        Voucher voucher = voucher(false, LocalDate.now().plusDays(1));
        when(voucherRepository.findByCode("ABC123")).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(Voucher.class))).thenAnswer(i -> i.getArgument(0));

        voucherService.redeemVoucher("ABC123");

        assertThrows(IllegalStateException.class, () -> voucherService.redeemVoucher("ABC123"));
        verify(voucherRepository, times(1)).save(voucher);
    }

}