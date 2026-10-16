import { ApiError } from "../api/voucherApi";

export function toErrorMessage(error: unknown): string {
  if (error instanceof ApiError) {
    return error.message;
  }
  return "Unerwarteter Fehler. Bitte erneut versuchen.";
}
