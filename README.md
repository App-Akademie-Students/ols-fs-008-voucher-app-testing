# Voucher App

Kleine Full-Stack-Beispielanwendung zum Verwalten und Einlösen von Gutscheinen. Im Mittelpunkt stehen ein klarer, gut testbarer Ablauf und Tests auf verschiedenen Ebenen (Unit, Web, Integration, E2E).

| Teil | Verzeichnis | Technologie |
|---|---|---|
| Backend (REST-API) | [voucher-backend/](voucher-backend/) | Java 21, Spring Boot 4.1.1, Spring Data JPA, H2, Maven |
| Frontend | [voucher-fontend/](voucher-fontend/) | Vite, TypeScript, HTML, CSS (kein Framework), Playwright |

> Hinweis: Das Frontend-Verzeichnis heißt `voucher-fontend` (so im Repository angelegt).

## Funktionen

- **Standardbereich** (`/`): Gutschein per Code abrufen und einlösen
- **Admin-Bereich** (`/admin.html`): Gutschein anlegen (Code, Betrag, Ablaufdatum)
- **Fachliche Regeln:** Gültige Gutscheine können eingelöst werden; abgelaufene oder bereits eingelöste werden abgelehnt.

## Schnellstart

Voraussetzungen: JDK 21, Node.js (inkl. npm). Maven muss nicht installiert sein (Maven Wrapper liegt bei).

**1. Backend starten** (Port 8080)

```bash
cd voucher-backend
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

**2. Frontend starten** (Port 5173, in einem zweiten Terminal)

```bash
cd voucher-fontend
npm install
npm run dev
```

Danach erreichbar:

| Was | URL |
|---|---|
| Standardbereich | http://localhost:5173 |
| Admin-Bereich | http://localhost:5173/admin.html |
| Backend-API | http://localhost:8080/api/vouchers |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2-Console | http://localhost:8080/h2-console (JDBC-URL `jdbc:h2:mem:voucherdb`, Benutzer `sa`, kein Passwort) |

## REST-API

Basispfad `/api/vouchers`:

| Methode | Pfad | Beschreibung | Status |
|---|---|---|---|
| `POST` | `/api/vouchers` | Gutschein erstellen | `201 Created` |
| `GET` | `/api/vouchers/{code}` | Gutschein per Code abrufen | `200 OK` |
| `POST` | `/api/vouchers/{code}/redeem` | Gutschein einlösen | `200 OK` |

Fehler (`ProblemDetail`, RFC 9457): unbekannter Code → `404`, abgelaufen oder bereits eingelöst → `409`.

Beispiel:

```json
POST /api/vouchers
{"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
```

## Tests

| Ebene | Befehl | Hinweis |
|---|---|---|
| Backend (Unit, Web, Integration) | `cd voucher-backend && ./mvnw test` | JUnit 5, MockMvc, `@SpringBootTest` |
| Frontend E2E (Playwright) | `cd voucher-fontend && npm run test:e2e` | **Backend muss auf Port 8080 laufen**; der Dev-Server startet automatisch |

Einmalig für E2E: `npx playwright install chromium`. Die E2E-Tests legen ihre Testdaten selbst per Backend-API an.

## Konfiguration

- Backend-URL im Frontend: `VITE_API_BASE_URL`, Standard `http://localhost:8080/api` (Vorlage: `voucher-fontend/.env.example`)
- CORS: Das Backend erlaubt `http://localhost:5173` (Vite) und `http://localhost:3000` (`@CrossOrigin` am `VoucherController`).
- Datenbank: H2 In-Memory mit `ddl-auto=create-drop`. **Alle Gutscheine gehen beim Neustart des Backends verloren.**

## Projektstruktur

```text
ols-fs-008-voucher-app-testing/
├── voucher-backend/     Spring-Boot-Backend (controller, service, repository, model, dto, exception)
│   ├── src/
│   └── docs/            Anforderungen und Prompts
└── voucher-fontend/     Vite-/TypeScript-Frontend
    ├── src/             api/, model/, ui/, main.ts, admin.ts
    ├── e2e/             Playwright-Tests
    └── docs/            Anforderungen und Prompts
```

## Weiterführende Dokumentation

- Backend: [voucher-backend/README.md](voucher-backend/README.md), [Anforderungen](voucher-backend/docs/requirements.md), [Prompts](voucher-backend/docs/prompts.md)
- Frontend: [voucher-fontend/README.md](voucher-fontend/README.md), [Anforderungen](voucher-fontend/docs/requirements.md), [Prompts](voucher-fontend/docs/prompts.md)


## E2E Playwright Test Example
```
import { test, expect } from '@playwright/test';

test('Startseite liefert HTTP 200', async ({ page }) => {
  const response = await page.goto('http://localhost:5173');

  expect(response?.status()).toBe(200);
});

```