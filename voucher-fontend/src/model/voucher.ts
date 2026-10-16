export interface Voucher {
  id: number;
  code: string;
  amount: number;
  expirationDate: string | null;
  redeemed: boolean;
}

export interface CreateVoucherRequest {
  code: string;
  amount: number;
  expirationDate: string;
}

/** Fehlerantwort des Backends (RFC 9457 ProblemDetail). */
export interface ProblemDetail {
  status?: number;
  title?: string;
  detail?: string;
}
