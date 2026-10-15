# Requirements – Voucher Backend

## Überblick

Das Voucher-Backend verwaltet Gutscheine (Vouchers). Ein Gutschein hat einen eindeutigen Code, einen Betrag, ein Ablaufdatum und einen Einlösestatus.

**Rollen**

- **Administrator** – legt Gutscheine an und verwaltet sie.
- **Kunde** – prüft und löst Gutscheine ein.

**Datenmodell (Voucher)**

| Feld             | Typ        | Beschreibung                          |
|------------------|------------|---------------------------------------|
| `id`             | Long       | Technische ID (generiert)             |
| `code`           | String     | Eindeutiger Gutscheincode (Pflicht)   |
| `amount`         | BigDecimal | Gutscheinwert                         |
| `expirationDate` | LocalDate  | Ablaufdatum                           |
| `redeemed`       | boolean    | Gutschein bereits eingelöst           |

---

## User Stories

### US-01: Gutschein anlegen

**Als** Administrator
**möchte ich** einen neuen Gutschein mit Code, Betrag und Ablaufdatum anlegen,
**damit** Kunden ihn später einlösen können.

**Akzeptanzkriterien**

- Ein Gutschein wird mit Code, Betrag und Ablaufdatum gespeichert.
- Ein neuer Gutschein ist standardmäßig nicht eingelöst (`redeemed = false`).
- Der Code ist Pflicht und muss eindeutig sein.
- Ein bereits vorhandener Code wird abgelehnt.
- Der Betrag muss größer als 0 sein.
- Das Ablaufdatum darf nicht in der Vergangenheit liegen.

**Status:** Teilweise umgesetzt (`VoucherService.createVoucher`, `POST /api/vouchers` mit `CreateVoucherRequest` / `VoucherResponse`; Validierung offen)

---

### US-02: Gutschein per Code abrufen

**Als** Kunde
**möchte ich** einen Gutschein anhand seines Codes abrufen,
**damit** ich Betrag, Ablaufdatum und Status einsehen kann.

**Akzeptanzkriterien**

- Bei existierendem Code werden alle Gutscheindaten zurückgegeben.
- Bei unbekanntem Code wird ein Fehler „Voucher not found: <code>“ geliefert.
- Der Fehler ist fachlich spezifisch (`VoucherNotFoundException`) und wird über den `GlobalExceptionHandler` als HTTP `404` (ProblemDetail) ausgeliefert.
- Abruf über `GET /api/vouchers/{code}`.

**Status:** Umgesetzt (`VoucherService.getVoucher`, `VoucherController.get`, `VoucherNotFoundException`, `GlobalExceptionHandler`)

---

### US-03: Gutschein einlösen

**Als** Kunde
**möchte ich** einen Gutschein einlösen,
**damit** ich den Betrag bei meiner Bestellung verwenden kann.

**Akzeptanzkriterien**

- Ein gültiger, nicht eingelöster Gutschein wird auf `redeemed = true` gesetzt.
- Ein bereits eingelöster Gutschein kann nicht erneut eingelöst werden (`IllegalStateException`: „Voucher already redeemed“).
- Ein abgelaufener Gutschein (Ablaufdatum vor heute) kann nicht eingelöst werden (`IllegalStateException`: „Voucher expired“).
- Am Ablaufdatum selbst kann der Gutschein noch eingelöst werden (Grenzfall).
- Ein Gutschein ohne Ablaufdatum gilt als nicht abgelaufen.
- Ein unbekannter Code liefert einen Fehler (`VoucherNotFoundException`, HTTP `404`).
- Der geänderte Zustand wird persistiert und der eingelöste Gutschein zurückgegeben.

**Status:** Service umgesetzt und per Unit-Tests abgedeckt (`VoucherService.redeemVoucher`). Offen: REST-Endpunkt, fachliche Exceptions für „bereits eingelöst“ / „abgelaufen“ (aktuell `IllegalStateException`)

---

### US-04: Gültigkeit prüfen

**Als** Kunde
**möchte ich** prüfen, ob ein Gutschein gültig ist,
**damit** ich vor der Bestellung weiß, ob ich ihn verwenden kann.

**Akzeptanzkriterien**

- Ein Gutschein ist gültig, wenn er nicht eingelöst und nicht abgelaufen ist.
- Am Ablaufdatum selbst ist der Gutschein noch gültig (Grenzfall).
- Das Ergebnis nennt bei Ungültigkeit den Grund (eingelöst / abgelaufen / unbekannt).

**Status:** Offen (die Prüflogik steckt bisher inline in `redeemVoucher`, eigene Methode noch nicht extrahiert)

---

### US-05: Gutscheine auflisten

**Als** Administrator
**möchte ich** alle Gutscheine auflisten und nach Status filtern (aktiv, eingelöst, abgelaufen),
**damit** ich den Überblick behalte.

**Akzeptanzkriterien**

- Alle Gutscheine können abgerufen werden.
- Filter nach eingelöst / nicht eingelöst und nach abgelaufen sind möglich.

**Status:** Offen

---

### US-06: Gutschein löschen oder sperren

**Als** Administrator
**möchte ich** einen Gutschein löschen oder sperren,
**damit** fehlerhaft angelegte oder missbrauchte Gutscheine nicht mehr verwendet werden.

**Akzeptanzkriterien**

- Ein gelöschter bzw. gesperrter Gutschein kann nicht mehr eingelöst werden.
- Ein unbekannter Code liefert einen Fehler.

**Status:** Offen

---

## Nicht-funktionale Anforderungen

| ID     | Anforderung                                                                                   |
|--------|-----------------------------------------------------------------------------------------------|
| NFR-01 | Gutscheincodes sind datenbankseitig eindeutig (Unique Constraint).                            |
| NFR-02 | Beträge werden als `BigDecimal` geführt, um Rundungsfehler zu vermeiden.                      |
| NFR-03 | Fehlerfälle liefern fachliche Exceptions statt generischer `RuntimeException`. Umgesetzt für „nicht gefunden“ (`VoucherNotFoundException`), offen für „eingelöst“ / „abgelaufen“. |
| NFR-04 | Service-Logik ist durch Unit-Tests abgedeckt (Happy Path, Fehlerfälle, Edge Cases).           |
