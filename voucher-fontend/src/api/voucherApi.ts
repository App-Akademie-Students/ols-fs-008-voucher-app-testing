import type { CreateVoucherRequest, ProblemDetail, Voucher } from "../model/voucher";

const API_BASE_URL: string =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

export class ApiError extends Error {
  readonly status: number;

  constructor(status: number, message: string) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

async function readProblemMessage(response: Response): Promise<string> {
  try {
    const problem = (await response.json()) as ProblemDetail;
    return problem.detail ?? problem.title ?? `HTTP ${response.status}`;
  } catch {
    return `HTTP ${response.status}`;
  }
}

async function request(path: string, init?: RequestInit): Promise<Voucher> {
  let response: Response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, init);
  } catch {
    throw new ApiError(0, "Das Backend ist nicht erreichbar.");
  }
  if (!response.ok) {
    throw new ApiError(response.status, await readProblemMessage(response));
  }
  return (await response.json()) as Voucher;
}

export function getVoucher(code: string): Promise<Voucher> {
  return request(`/vouchers/${encodeURIComponent(code)}`);
}

export function redeemVoucher(code: string): Promise<Voucher> {
  return request(`/vouchers/${encodeURIComponent(code)}/redeem`, { method: "POST" });
}

export function createVoucher(voucher: CreateVoucherRequest): Promise<Voucher> {
  return request("/vouchers", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(voucher),
  });
}
