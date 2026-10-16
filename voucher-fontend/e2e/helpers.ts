const API_BASE_URL = process.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

export interface TestVoucher {
  code: string;
  amount: number;
  expirationDate: string;
}

/** Eindeutiger Code pro Testlauf. */
export function uniqueCode(prefix: string): string {
  return `${prefix}-${Date.now()}-${Math.floor(Math.random() * 1000)}`;
}

/** Ablaufdatum in der Zukunft (yyyy-MM-dd). */
export function futureDate(daysFromNow = 30): string {
  const date = new Date();
  date.setDate(date.getDate() + daysFromNow);
  return date.toISOString().slice(0, 10);
}

/** Legt einen Gutschein direkt über die Backend-API an. */
export async function createVoucher(voucher: TestVoucher): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/vouchers`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(voucher),
  });
  if (!response.ok) {
    throw new Error(`Testdaten konnten nicht angelegt werden: HTTP ${response.status}`);
  }
}
