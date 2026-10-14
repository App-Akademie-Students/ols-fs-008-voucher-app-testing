package com.example.voucher;

import com.example.voucher.model.Voucher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;

@SpringBootTest
class VoucherApplicationTests {

	@Test
	void contextLoads() {
        Voucher v = new Voucher(1L, "TESTCODE", null, null, false);
	}


}
