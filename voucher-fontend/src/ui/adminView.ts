import { getElement } from "./dom";

export interface CreateFormValues {
  code: string;
  amount: string;
  expirationDate: string;
}

export function readForm(): CreateFormValues {
  return {
    code: getElement<HTMLInputElement>("code").value.trim(),
    amount: getElement<HTMLInputElement>("amount").value,
    expirationDate: getElement<HTMLInputElement>("expiration-date").value,
  };
}

export function onCreate(handler: () => void): void {
  getElement<HTMLFormElement>("create-form").addEventListener("submit", (event) => {
    event.preventDefault();
    handler();
  });
}
