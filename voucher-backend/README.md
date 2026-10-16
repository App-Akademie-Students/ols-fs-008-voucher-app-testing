# Voucher Backend – Gutscheine verwalten und einlösen

REST-API zum Erstellen, Abrufen und Einlösen von Gutscheinen. Das Backend ist Teil der
**Voucher App**, einer kleinen Full-Stack-Beispielanwendung, an der Spring Boot, ein Vite-/TypeScript-Frontend und
verschiedene Testarten praktisch umgesetzt werden. Im Mittelpunkt steht nicht die Komplexität,
sondern ein klarer, gut testbarer Ablauf.

> **Status:** Projekt-Grundgerüst, Domänenmodell, Service sowie Erstellen, Abrufen und Einlösen
> per REST stehen. Eingabevalidierung und weitere Tests sind in Arbeit (siehe [Roadmap](#roadmap)).

## Projektstruktur

Backend und Frontend liegen als getrennte Projekte nebeneinander in einem übergeordneten Verzeichnis:

```
ols-fs-008-voucher-app-testing/
├── voucher-backend/     <- dieses Projekt (Spring Boot)
└── voucher-frontend/    <- Vite-/TypeScript-Frontend (parallel zum Backend)
```

## Tech-Stack

| Bereich       | Technologie                                   |
|---------------|-----------------------------------------------|
| Sprache       | Java 21                                       |
| Framework     | Spring Boot 4.1.1 (Spring Web MVC)            |
| Persistenz    | Spring Data JPA / Hibernate                   |
| Datenbank     | H2 (In-Memory, inkl. H2-Console)              |
| Validierung   | Bean Validation (`spring-boot-starter-validation`) |
| API-Doku      | springdoc-openapi (Swagger UI)                |
| Hilfsmittel   | Lombok, Spring Boot DevTools                  |
| Build         | Maven (Maven Wrapper enthalten)               |
| Tests         | JUnit 5, Spring Boot Test (`webmvc-test`, `data-jpa-test`, `validation-test`) |

## Domänenmodell

Ein `Voucher` (Paket `com.example.voucher.model`) besitzt:

| Feld             | Typ          | Beschreibung                                |
|------------------|--------------|---------------------------------------------|
| `id`             | `Long`       | Technischer Primärschlüssel (generiert)     |
| `code`           | `String`     | Gutscheincode, über den abgerufen wird      |
| `amount`         | `BigDecimal` | Gutscheinwert                               |
| `expirationDate` | `LocalDate`  | Ablaufdatum                                 |
| `redeemed`       | `boolean`    | Status: bereits eingelöst?                  |

## Fachliche Regeln

- Gutscheine können **erstellt** werden.
- Gutscheine können über ihren **Code abgerufen** werden.
- **Gültige** Gutscheine können **eingelöst** werden.
- **Abgelaufene** oder **bereits eingelöste** Gutscheine werden abgelehnt.

## REST-API

Implementiert im `VoucherController` (Basispfad `/api/vouchers`):

| Methode | Pfad                           | Beschreibung                          | Status          |
|---------|--------------------------------|---------------------------------------|-----------------|
| `POST`  | `/api/vouchers`                | Gutschein erstellen                   | `201 Created`   |
| `GET`   | `/api/vouchers/{code}`         | Gutschein per Code abrufen            | `200 OK`        |
| `POST`  | `/api/vouchers/{code}/redeem`  | Gutschein einlösen                    | `200 OK`        |

Beispiel zum Erstellen:

```json
POST /api/vouchers
{"code": "ABC123", "amount": 50.00, "expirationDate": "2026-12-31"}
```

Die Antwort (`VoucherResponse`) enthält `id`, `code`, `amount`, `expirationDate` und `redeemed`.

Beispiel zum Einlösen (ohne Request-Body, liefert den Gutschein mit `redeemed: true`):

```
POST /api/vouchers/ABC123/redeem
```

### Fehlerbehandlung

Fehler werden zentral im `GlobalExceptionHandler` als `ProblemDetail` (RFC 9457) beantwortet:

| Fall                   | HTTP-Status     |
|------------------------|-----------------|
| Unbekannter Code       | `404 Not Found` |
| Bereits eingelöst      | `409 Conflict`  |
| Abgelaufen             | `409 Conflict`  |

Der Service wirft in den beiden `409`-Fällen eine `IllegalStateException`, die der
`GlobalExceptionHandler` auf `409 Conflict` abbildet. Ungültige Eingabe (Validierung) ist noch
nicht über eigene Statuscodes abgebildet.

## Voraussetzungen

- JDK 21
- Keine lokale Maven-Installation nötig (`mvnw` / `mvnw.cmd` liegen bei)

## Starten

```bash
# Linux / macOS / Git Bash
./mvnw spring-boot:run

# Windows (PowerShell / cmd)
.\mvnw.cmd spring-boot:run
```

Das Backend läuft anschließend unter `http://localhost:8080`.

### API-Dokumentation (Swagger)

Die REST-API ist über Swagger UI dokumentiert und kann dort direkt ausprobiert werden:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI-Spezifikation (JSON): `http://localhost:8080/v3/api-docs`

### H2-Console

Die In-Memory-Datenbank kann im Browser eingesehen werden:

- URL: `http://localhost:8080/h2-console`
- JDBC-URL: `jdbc:h2:mem:voucherdb`
- Benutzer: `sa`, Passwort: leer

Die Daten gehen beim Beenden der Anwendung verloren.

## Tests ausführen

```bash
./mvnw test
```

## Teststrategie

Backend, Frontend und komplette User Journeys werden automatisiert getestet – auf
unterschiedlichen Testebenen. Für das Backend sind vorgesehen:

| Ebene              | Gegenstand                                        | Mittel                         |
|--------------------|---------------------------------------------------|--------------------------------|
| Unit-Tests         | Geschäftslogik (Gültigkeit, Einlösen)             | JUnit 5                        |
| Repository-Tests   | Persistenz und Abfragen per Code                  | `@DataJpaTest`, H2             |
| Web-/API-Tests     | Controller, Statuscodes, Validierung              | `@WebMvcTest` / MockMvc        |
| Integrationstests  | Gesamter Ablauf von API bis Datenbank             | `@SpringBootTest`              |

Die End-to-End-Tests über komplette User Journeys entstehen zusammen mit dem Frontend.

## Konfiguration

Die Einstellungen liegen in `src/main/resources/application.properties`:

- H2 In-Memory-Datenbank `voucherdb`
- `spring.jpa.hibernate.ddl-auto=create-drop`
- SQL-Ausgabe aktiviert (`spring.jpa.show-sql=true`)
- H2-Console unter `/h2-console` aktiviert

## Roadmap

- [x] Projekt-Setup (Spring Boot, JPA, H2, Validation)
- [x] Entity `Voucher`
- [x] Repository (Suche per Code)
- [x] Service mit Einlöse-Logik (Prüfung auf eingelöst/abgelaufen)
- [x] REST-Controller: Erstellen und Abrufen, `404` bei unbekanntem Code
- [x] REST-Endpunkt zum Einlösen (`POST /api/vouchers/{code}/redeem`)
- [x] Fehlerbehandlung für abgelaufen/bereits eingelöst (`IllegalStateException` → `409 Conflict`)
- [ ] Eingabevalidierung beim Erstellen
- [x] Automatisierte Tests: Unit (Service), Web (Controller), Integration (Service)
- [ ] Repository-Tests (`@DataJpaTest`) und API-Integrationstests von Controller bis Datenbank
- [x] Swagger UI (springdoc-openapi)
- [x] CORS-Konfiguration für das Frontend (`@CrossOrigin` für `http://localhost:3000` und den Vite-Dev-Server `http://localhost:5173` am `VoucherController`)
- [ ] Frontend (`voucher-frontend`) und E2E-Tests
