import type { Voucher } from "../model/voucher";

export function getElement<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) {
    throw new Error(`Element #${id} nicht gefunden`);
  }
  return element as T;
}

export function showError(message: string): void {
  const error = getElement<HTMLParagraphElement>("error");
  error.textContent = message;
  error.hidden = false;
}

export function clearError(): void {
  const error = getElement<HTMLParagraphElement>("error");
  error.textContent = "";
  error.hidden = true;
}

function appendRow(list: HTMLElement, term: string, description: string): void {
  const dt = document.createElement("dt");
  dt.textContent = term;
  const dd = document.createElement("dd");
  dd.textContent = description;
  list.append(dt, dd);
}

export function showVoucher(voucher: Voucher): void {
  const list = getElement<HTMLElement>("voucher");
  list.replaceChildren();
  appendRow(list, "Code", voucher.code);
  appendRow(list, "Betrag", voucher.amount.toFixed(2));
  appendRow(list, "Ablaufdatum", voucher.expirationDate ?? "kein Ablaufdatum");
  appendRow(list, "Status", voucher.redeemed ? "Eingelöst" : "Nicht eingelöst");
  getElement("voucher-section").hidden = false;
}

export function hideVoucher(): void {
  getElement("voucher-section").hidden = true;
}
