import { getElement } from "./dom";

export function readCode(): string {
  return getElement<HTMLInputElement>("code").value.trim();
}

export function onLookup(handler: () => void): void {
  getElement<HTMLFormElement>("lookup-form").addEventListener("submit", (event) => {
    event.preventDefault();
    handler();
  });
}

export function onRedeem(handler: () => void): void {
  getElement<HTMLButtonElement>("redeem-button").addEventListener("click", handler);
}
