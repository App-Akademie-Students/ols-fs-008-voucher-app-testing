# Voucher Frontend

Einfaches Frontend zur Verwaltung von Gutscheinen. Es kommuniziert mit dem separaten Spring-Boot-Backend (`voucher-backend`).

> **Status:** Standard- und Admin-Bereich sind umgesetzt (FE-01 bis FE-03). Playwright-E2E-Tests decken den Standardbereich ab (4 Tests); für den Admin-Bereich gibt es noch keine.

## Funktionen

- **Standardbereich** (`/`): Gutschein per Code abrufen und einlösen
- **Admin-Bereich** (`/admin.html`): Gutschein anlegen (Code, Betrag, Ablaufdatum)

Details und Akzeptanzkriterien: [docs/requirements.md](docs/requirements.md).

## Tech-Stack

| Technologie | Version | Rolle |
|---|---|---|
| TypeScript | ^5.6 | Programmiersprache, `strict`-Modus, explizite Interfaces für Backend-Daten |
| Vite | ^6 | Dev-Server und Build-Tool, Multi-Page-Setup mit `index.html` und `admin.html` |
| HTML | – | Semantisches Markup, Grundlage für die Playwright-Selektoren |
| CSS | – | Einfaches Styling in `src/style.css`, kein CSS-Framework |
| Fetch API | Browser-nativ | HTTP-Kommunikation mit dem Spring-Boot-Backend, nur in `src/api/` |
| Playwright (`@playwright/test`) | ^1.64 | E2E-Tests in `e2e/` mit Chromium, Dev-Server startet automatisch |

Bewusst nicht verwendet: React oder andere Frontend-Frameworks, zusätzliche Libraries, Klassen wo Funktionen genügen (Ausnahme: `ApiError`).

## Starten

Voraussetzung: Das Backend läuft auf `http://localhost:8080`.

```bash
npm install
npm run dev
```

Die Anwendung ist dann unter http://localhost:5173 erreichbar. Die Backend-URL lässt sich über `VITE_API_BASE_URL` ändern (siehe `.env.example`).

## Befehle

| Befehl | Zweck |
|---|---|
| `npm run dev` | Dev-Server auf http://localhost:5173 |
| `npm run build` | Typprüfung und Produktions-Build |
| `npm run test:e2e` | Playwright-Tests (Backend muss auf Port 8080 laufen) |

## Konfiguration

- `VITE_API_BASE_URL`: Backend-Basis-URL, Standard `http://localhost:8080/api` (Vorlage: `.env.example`)
- Der Dev-Server auf Port 5173 ist im Backend als CORS-Origin erlaubt (`@CrossOrigin` am `VoucherController`).
- Das Backend nutzt eine H2-In-Memory-Datenbank (`ddl-auto=create-drop`): Alle Gutscheine gehen beim Neustart verloren. E2E-Tests legen ihre Testdaten daher selbst per API an.

## Projektstruktur

```text
index.html            Standardbereich (Abrufen, Einlösen)
admin.html            Admin-Bereich (Anlegen)
vite.config.ts        Vite, Multi-Page (index.html + admin.html)
playwright.config.ts  Playwright-Konfiguration
.env.example          Vorlage für VITE_API_BASE_URL
docs/
├── requirements.md
└── prompts.md
e2e/
├── helpers.ts        Testdaten-Helfer
└── voucher.spec.ts   E2E-Tests
src/
├── api/              Backend-Aufrufe (fetch)
├── model/            Typen und Interfaces
├── ui/
│   ├── dom.ts            gemeinsame DOM-Helfer und Voucher-Anzeige
│   ├── customerView.ts   Standardbereich
│   ├── adminView.ts      Admin-Bereich
│   └── errorMessage.ts   Fehlertext aus ApiError
├── main.ts           Ablaufsteuerung Standardbereich
├── admin.ts          Ablaufsteuerung Admin-Bereich
└── style.css
```

## E2E-Tests

| Test                                           | Abgedeckt   |
|------------------------------------------------|-------------|
| Kunde findet einen Gutschein                   | FE-01-AC1/AC2 |
| Kunde löst einen Gutschein ein                 | FE-02-AC1/AC2 |
| Unbekannter Code zeigt eine Fehlermeldung      | FE-01-AC3   |
| Doppeltes Einlösen zeigt die Backend-Meldung   | FE-02-AC3   |

```bash
npx playwright install chromium   # einmalig
npm run test:e2e

Tests stepweise ausführen: npx playwright test e2e/voucher.spec.ts --debug

Komplett:npx playwright test e2e/voucher.spec.ts --headed
oder
npx playwright test --ui

```

## Dokumentation

- [docs/requirements.md](docs/requirements.md): Anforderungen
- [docs/prompts.md](docs/prompts.md): verwendete AI-Prompts
