import { expect, test } from "@playwright/test";
import { createVoucher, futureDate, uniqueCode } from "./helpers";

// Deckt FE-01-AC1/AC2 (abrufen) ab.
test("Kunde findet einen Gutschein", async ({ page }) => {
  const code = uniqueCode("E2E");
  const expirationDate = futureDate();
  await createVoucher({ code, amount: 50, expirationDate });

  await page.goto("/");
  await page.getByLabel("Code").fill(code);
  await page.getByRole("button", { name: "Gutschein abrufen" }).click();

  await expect(page.getByRole("heading", { name: "Gutschein", exact: true })).toBeVisible();
  await expect(page.getByText(code)).toBeVisible();
  await expect(page.getByText("50.00")).toBeVisible();
  await expect(page.getByText(expirationDate)).toBeVisible();
  await expect(page.getByText("Nicht eingelöst")).toBeVisible();
});

// Deckt FE-02-AC1/AC2 (einlösen) ab.
test("Kunde löst einen Gutschein ein", async ({ page }) => {
  const code = uniqueCode("E2E");
  await createVoucher({ code, amount: 50, expirationDate: futureDate() });

  await page.goto("/");
  await page.getByLabel("Code").fill(code);
  await page.getByRole("button", { name: "Gutschein abrufen" }).click();
  await page.getByRole("button", { name: "Gutschein einlösen" }).click();

  await expect(page.getByText("Eingelöst", { exact: true })).toBeVisible();
  await expect(page.getByRole("alert")).toHaveCount(0);
});

// Deckt FE-01-AC3 (unbekannter Code) ab.
test("Unbekannter Code zeigt eine Fehlermeldung", async ({ page }) => {
  await page.goto("/");
  await page.getByLabel("Code").fill(uniqueCode("MISSING"));
  await page.getByRole("button", { name: "Gutschein abrufen" }).click();

  await expect(page.getByRole("alert")).toBeVisible();
  await expect(page.getByRole("button", { name: "Gutschein einlösen" })).toBeHidden();
});

// Deckt FE-02-AC3 (bereits eingelöst, 409) ab.
test("Doppeltes Einlösen zeigt die Backend-Meldung", async ({ page }) => {
  const code = uniqueCode("E2E");
  await createVoucher({ code, amount: -50, expirationDate: futureDate() });

  await page.goto("/");
  await page.getByLabel("Code").fill(code);
  await page.getByRole("button", { name: "Gutschein abrufen" }).click();
  await page.getByRole("button", { name: "Gutschein einlösen" }).click();
  await expect(page.getByText("Eingelöst", { exact: true })).toBeVisible();

  await page.getByRole("button", { name: "Gutschein einlösen" }).click();

  await expect(page.getByRole("alert")).toContainText("already redeemed");
  await expect(page.getByText("Eingelöst", { exact: true })).toBeVisible();
});
