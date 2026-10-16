package com.example.voucher.controller;

import com.example.voucher.exception.VoucherNotFoundException;
import com.example.voucher.model.Voucher;
import com.example.voucher.service.VoucherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoucherController.class)
class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VoucherService voucherService;

    private Voucher savedVoucher() {
        Voucher voucher = new Voucher("ABC123", new BigDecimal("50.00"), LocalDate.of(2026, 12, 31), false);
        voucher.setId(1L);
        return voucher;
    }

    // ---- GET /api/vouchers/{code} ----

    @Test
    @DisplayName("GET should return voucher when code exists")
    void shouldReturnVoucherWhenCodeExists() throws Exception {
        when(voucherService.getVoucher("ABC123")).thenReturn(savedVoucher());

        mockMvc.perform(get("/api/vouchers/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.amount").value(50.00))
                .andExpect(jsonPath("$.expirationDate").value("2026-12-31"))
                .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    @DisplayName("GET should return 404 when voucher not found")
    void shouldReturn404WhenVoucherNotFound() throws Exception {
        when(voucherService.getVoucher("UNKNOWN")).thenThrow(new VoucherNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/api/vouchers/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Voucher not found: UNKNOWN"));
    }

    // ---- POST /api/vouchers ----

    @Test
    @DisplayName("POST should create voucher and return 201")
    void shouldCreateVoucherAndReturn201() throws Exception {
        when(voucherService.createVoucher(any(Voucher.class))).thenReturn(savedVoucher());

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    @DisplayName("POST should pass request data to service as not redeemed")
    void shouldPassRequestDataToService() throws Exception {
        when(voucherService.createVoucher(any(Voucher.class))).thenReturn(savedVoucher());

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
                                """))
                .andExpect(status().isCreated());

        ArgumentCaptor<Voucher> captor = ArgumentCaptor.forClass(Voucher.class);
        verify(voucherService).createVoucher(captor.capture());
        Voucher passed = captor.getValue();

        assertEquals("ABC123", passed.getCode());
        assertEquals(0, new BigDecimal("50.00").compareTo(passed.getAmount()));
        assertEquals(LocalDate.of(2026, 12, 31), passed.getExpirationDate());
        assertFalse(passed.isRedeemed());
    }

    @Test
    @DisplayName("POST should return 400 when body is malformed JSON")
    void shouldReturn400WhenBodyIsMalformedJson() throws Exception {
        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"ABC123\", "))
                .andExpect(status().isBadRequest());

        verify(voucherService, never()).createVoucher(any());
    }

    @Test
    @DisplayName("POST should return 400 when date format is invalid")
    void shouldReturn400WhenDateFormatInvalid() throws Exception {
        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "ABC123", "amount": 50.00, "expirationDate": "31.12.2026"}
                                """))
                .andExpect(status().isBadRequest());

        verify(voucherService, never()).createVoucher(any());
    }

    @Test
    @DisplayName("POST should return 415 when content type is missing")
    void shouldReturn415WhenContentTypeMissing() throws Exception {
        mockMvc.perform(post("/api/vouchers")
                        .content("""
                                {"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
                                """))
                .andExpect(status().isUnsupportedMediaType());

        verify(voucherService, never()).createVoucher(any());
    }

    @Test
    @DisplayName("POST should ignore redeemed field in request")
    void shouldIgnoreRedeemedFieldInRequest() throws Exception {
        when(voucherService.createVoucher(any(Voucher.class))).thenReturn(savedVoucher());

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31", "redeemed": true}
                                """))
                .andExpect(status().isCreated());

        ArgumentCaptor<Voucher> captor = ArgumentCaptor.forClass(Voucher.class);
        verify(voucherService).createVoucher(captor.capture());
        assertFalse(captor.getValue().isRedeemed());
    }

    @Test
    @DisplayName("POST currently accepts missing expiration date (no validation yet)")
    void shouldHandleMissingOptionalFields() throws Exception {
        Voucher withoutExpiration = new Voucher("ABC123", new BigDecimal("50.00"), null, false);
        withoutExpiration.setId(2L);
        when(voucherService.createVoucher(any(Voucher.class))).thenReturn(withoutExpiration);

        mockMvc.perform(post("/api/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code": "ABC123", "amount": 50.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.expirationDate").doesNotExist());
    }

    // ---- POST /api/vouchers/{code}/redeem ----

    @Test
    @DisplayName("POST redeem should return 200 and redeemed voucher")
    void shouldRedeemVoucherAndReturn200() throws Exception {
        Voucher redeemed = savedVoucher();
        redeemed.redeem();
        when(voucherService.redeemVoucher("ABC123")).thenReturn(redeemed);

        mockMvc.perform(post("/api/vouchers/ABC123/redeem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.redeemed").value(true));

        verify(voucherService).redeemVoucher("ABC123");
    }

    @Test
    @DisplayName("POST redeem should return 404 when voucher not found")
    void shouldReturn404WhenRedeemingUnknownVoucher() throws Exception {
        when(voucherService.redeemVoucher("UNKNOWN")).thenThrow(new VoucherNotFoundException("UNKNOWN"));

        mockMvc.perform(post("/api/vouchers/UNKNOWN/redeem"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Voucher not found: UNKNOWN"));
    }

    @Test
    @DisplayName("POST redeem should return 409 when voucher already redeemed")
    void shouldReturn409WhenVoucherAlreadyRedeemed() throws Exception {
        when(voucherService.redeemVoucher("ABC123"))
                .thenThrow(new IllegalStateException("Voucher already redeemed"));

        mockMvc.perform(post("/api/vouchers/ABC123/redeem"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Voucher already redeemed"));
    }

    @Test
    @DisplayName("POST redeem should return 409 when voucher expired")
    void shouldReturn409WhenVoucherExpired() throws Exception {
        when(voucherService.redeemVoucher("ABC123"))
                .thenThrow(new IllegalStateException("Voucher expired"));

        mockMvc.perform(post("/api/vouchers/ABC123/redeem"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Voucher expired"));
    }
}
