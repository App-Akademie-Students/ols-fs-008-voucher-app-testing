# Requirements – Voucher Frontend

## Überblick

Das Frontend ist die Oberfläche zur Voucher-Anwendung. Es kommuniziert ausschließlich über die REST-API des Spring-Boot-Backends (`/api/vouchers`). Fachliche Regeln (Eindeutigkeit, Ablauf, Einlösestatus) liegen im Backend. Maßgeblich sind die Anforderungen in `voucher-backend/docs/requirements.md`.

**Bereiche**

- **Standardbereich** (`/`, `index.html`) für Kunden: Gutschein abrufen und einlösen.
- **Admin-Bereich** (`/admin.html`) für Administratoren: Gutschein anlegen.

Beide Bereiche sind getrennt (eigene HTML-Seite, eigener Einstiegspunkt, eigene View).

**Backend-Schnittstelle**

| Methode | Pfad                          | Zweck                       | Erfolg | Fehler                                        |
|---------|-------------------------------|-----------------------------|--------|-----------------------------------------------|
| `POST`  | `/api/vouchers`               | Gutschein anlegen           | `201`  | noch nicht dokumentiert (Validierung offen)   |
| `GET`   | `/api/vouchers/{code}`        | Gutschein abrufen           | `200`  | `404` unbekannter Code                        |
| `POST`  | `/api/vouchers/{code}/redeem` | Gutschein einlösen          | `200`  | `404` unbekannt, `409` eingelöst oder abgelaufen |

Fehler kommen als `ProblemDetail` (RFC 9457); die Meldung steht im Feld `detail`.

Voucher-Felder: `id`, `code`, `amount`, `expirationDate` (`yyyy-MM-dd`), `redeemed`.

---

## User Stories

### FE-01: Gutschein per Code abrufen (Standardbereich)

**Als** Kunde
**möchte ich** einen Code eingeben und den Gutschein abrufen,
**damit** ich Betrag, Ablaufdatum und Status sehe (Backend: US-02).

**Akzeptanzkriterien**

- **FE-01-AC1:** Es gibt ein Eingabefeld „Code“ mit Label und einen Button zum Abrufen.
- **FE-01-AC2:** Bei bekanntem Code werden Code, Betrag, Ablaufdatum und Status (eingelöst / nicht eingelöst) angezeigt.
- **FE-01-AC3:** Bei unbekanntem Code erscheint eine eindeutige Fehlermeldung (`role="alert"`).

**Status:** Umgesetzt; E2E-Tests für AC1, AC2 und AC3

---

### FE-02: Gutschein einlösen (Standardbereich)

**Als** Kunde
**möchte ich** einen abgerufenen Gutschein einlösen,
**damit** ich den Betrag verwenden kann (Backend: US-03).

**Akzeptanzkriterien**

- **FE-02-AC1:** Es gibt einen Button zum Einlösen für den angezeigten Gutschein.
- **FE-02-AC2:** Nach erfolgreichem Einlösen wird der Gutschein als eingelöst angezeigt.
- **FE-02-AC3:** Bei bereits eingelöstem oder abgelaufenem Gutschein (`409`) erscheint die Backend-Meldung als Fehlermeldung (`role="alert"`).
- **FE-02-AC4:** Bei unbekanntem Code (`404`) erscheint eine Fehlermeldung (`role="alert"`).

**Status:** Umgesetzt; E2E-Tests für AC1, AC2 und AC3 (AC4 ungetestet)

---

### FE-03: Gutschein anlegen (Admin-Bereich)

**Als** Administrator
**möchte ich** einen Gutschein mit Code, Betrag und Ablaufdatum anlegen,
**damit** Kunden ihn einlösen können (Backend: US-01).

**Akzeptanzkriterien**

- **FE-03-AC1:** Es gibt Formularfelder „Code“, „Betrag“ und „Ablaufdatum“, jeweils mit Label, und einen Button zum Anlegen.
- **FE-03-AC2:** Nach erfolgreichem Anlegen wird der neue Gutschein angezeigt (`redeemed = false`).
- **FE-03-AC3:** Schlägt der Aufruf fehl, erscheint eine Fehlermeldung (`role="alert"`).

**Offen / abhängig vom Backend:** Das Backend validiert Eingaben noch nicht (Backend: US-01). Clientseitige Prüfungen (z. B. Pflichtfelder) sind nur ergänzend und ersetzen keine Backend-Regeln.

**Status:** Umgesetzt (manuell nicht gegen das Backend geprüft)

---

## Nicht umgesetzt (Backend-Stories ohne Frontend-Anforderung)

- US-04 Gültigkeit prüfen, US-05 Gutscheine auflisten, US-06 löschen oder sperren: im Backend offen, daher keine Frontend-Anforderung.

## Nicht-funktionale Anforderungen

| ID        | Anforderung                                                                                  |
|-----------|----------------------------------------------------------------------------------------------|
| FE-NFR-01 | Vite, TypeScript (strict), HTML, CSS; kein React, kein weiteres Frontend-Framework.          |
| FE-NFR-02 | HTTP-Aufrufe ausschließlich in `src/api/`, Basis-URL aus `VITE_API_BASE_URL`.                |
| FE-NFR-03 | Semantisches HTML, Formularfelder mit Label, Fehlermeldungen mit `role="alert"`.             |
| FE-NFR-04 | Oberfläche mit Playwright testbar über Rollen, Labels und sichtbare Texte (kein `data-testid`). |
