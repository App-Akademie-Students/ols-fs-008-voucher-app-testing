# Verwendete Prompts

Fortlaufende Dokumentation der AI-Prompts für das Frontend.

## 1. Projekt abgleichen

> lese die dateien und gleiche das projekt ab

- **Kontext:** Frontend enthielt nur `CLAUDE.md` und `README.md`; `docs/` war leer.
- **Ergebnis:** Abgleich mit dem Backend, keine Änderungen. Abweichungen: fehlende Dateien, CORS-Port 5173 nicht erlaubt, Ordnername `voucher-fontend`, React-Erwähnung in der Backend-README, `ddl-auto`-Angabe.

## 2. A, B, C umsetzen

> A, B, C

- **Kontext:** Auswahl aus den Vorschlägen: README anpassen (A), `docs/requirements.md` und `docs/prompts.md` anlegen (B), Backend-CORS erweitern (C).
- **Ergebnis:** README an den tatsächlichen Stand angepasst; `docs/requirements.md` (FE-01 bis FE-03, aus Backend-US-01 bis US-03 abgeleitet) und diese Datei angelegt; `@CrossOrigin` im Backend um `http://localhost:5173` erweitert. Keine Tests ausgeführt.

## 3. IDs für Akzeptanzkriterien

> Akzeptanzkriterien sollen auch eine ID bekommen.

- **Kontext:** Die Kriterien in `docs/requirements.md` waren unnummerierte Aufzählungen.
- **Ergebnis:** Jedes Akzeptanzkriterium trägt eine ID nach dem Schema `FE-<Story>-AC<n>` (FE-01-AC1 bis FE-03-AC3), damit Tests und Tickets darauf verweisen können.

## 4. Frontend-Grundgerüst (E, dann D)

> erst e, dann d

- **Kontext:** E: Umbenennen von `voucher-fontend` scheiterte (Permission denied, Ordner geöffnet), Backend-README bereinigt. D: Frontend-Grundgerüst.
- **Ergebnis:** Vite/TypeScript-Projekt (`package.json`, `tsconfig.json`, `vite.config.ts`, `.env.example`, `.gitignore`), `index.html`, `admin.html`, `src/api`, `src/model`, `src/ui`, `src/main.ts`, `src/admin.ts`, `src/style.css`. FE-01 bis FE-03 umgesetzt. `npm run build` erfolgreich. `@types/node` als Dev-Abhängigkeit für `vite.config.ts`. Keine Tests geschrieben, nicht gegen das laufende Backend geprüft.

## 5. Playwright-E2E-Test (Happy Path)

> Playwright-E2E-Test , nur einen Happy-Path-Test

- **Kontext:** Ausdrückliche Testaufgabe, ein einziger Test.
- **Ergebnis:** `@playwright/test` installiert, `playwright.config.ts`, `e2e/helpers.ts` (Testdaten per Backend-API, eindeutiger Code), `e2e/voucher.spec.ts` (Gutschein abrufen und einlösen, deckt FE-01-AC1/AC2 und FE-02-AC1/AC2), Skript `test:e2e`. Lauf gegen laufendes Backend: 1 Test grün.

## 6. Tests aufteilen und zwei weitere ergänzen

> 1 und 2, vorher die Tests Gutschein finden, Gutschein einlösen

- **Kontext:** Ausdrückliche Testaufgabe. Der kombinierte Test wurde in „Gutschein finden“ und „Gutschein einlösen“ getrennt, dazu zwei vorgeschlagene Tests.
- **Ergebnis:** `e2e/voucher.spec.ts` enthält vier Tests: finden (FE-01-AC1/AC2), einlösen (FE-02-AC1/AC2), unbekannter Code (FE-01-AC3), doppeltes Einlösen mit `409`-Meldung (FE-02-AC3).

## 7. Dokumente anpassen

> pass alle Dokumente an wenn nötig.

- **Kontext:** Nach dem Aufteilen und Ergänzen der E2E-Tests.
- **Ergebnis:** `README.md`: Status-Hinweis (Tests vorhanden) und Testtabelle mit Abdeckung. `docs/requirements.md`: Status von FE-01 und FE-02 um die E2E-Abdeckung ergänzt. `CLAUDE.md` unverändert.
