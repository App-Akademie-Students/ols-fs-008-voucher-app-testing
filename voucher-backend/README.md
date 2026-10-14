# Voucher Backend – Gutscheine verwalten und einlösen

REST-API zum Erstellen, Abrufen und Einlösen von Gutscheinen. Das Backend ist Teil der
**Voucher App**, einer kleinen Full-Stack-Beispielanwendung, an der Spring Boot, React und
verschiedene Testarten praktisch umgesetzt werden. Im Mittelpunkt steht nicht die Komplexität,
sondern ein klarer, gut testbarer Ablauf.

> **Status:** Projekt-Grundgerüst und Domänenmodell stehen. REST-API, Geschäftslogik und Tests
> sind in Arbeit (siehe [Roadmap](#roadmap)).

## Projektstruktur

Backend und Frontend liegen als getrennte Projekte nebeneinander in einem übergeordneten Verzeichnis:

```
ols-fs-008-voucher-app-testing/
├── voucher-backend/     <- dieses Projekt (Spring Boot)
└── voucher-frontend/    <- React-Frontend (folgt, parallel zum Backend)
```

## Tech-Stack

| Bereich       | Technologie                                   |
|---------------|-----------------------------------------------|
| Sprache       | Java 21                                       |
| Framework     | Spring Boot 4.1.1 (Spring Web MVC)            |
| Persistenz    | Spring Data JPA / Hibernate                   |
| Datenbank     | H2 (In-Memory, inkl. H2-Console)              |
| Validierung   | Bean Validation (`spring-boot-starter-validation`) |
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

## REST-API (geplant)

Die konkreten Pfade sind ein Vorschlag und noch nicht implementiert:

| Methode | Pfad                           | Beschreibung                          |
|---------|--------------------------------|---------------------------------------|
| `POST`  | `/api/vouchers`                | Gutschein erstellen                   |
| `GET`   | `/api/vouchers/{code}`         | Gutschein per Code abrufen            |
| `POST`  | `/api/vouchers/{code}/redeem`  | Gutschein einlösen                    |

Fehlerfälle (z. B. unbekannter Code, abgelaufen, bereits eingelöst) sollen mit sinnvollen
HTTP-Statuscodes und einer klaren Fehlermeldung beantwortet werden.

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
- `spring.jpa.hibernate.ddl-auto=update`
- SQL-Ausgabe aktiviert (`spring.jpa.show-sql=true`)
- H2-Console unter `/h2-console` aktiviert

## Roadmap

- [x] Projekt-Setup (Spring Boot, JPA, H2, Validation)
- [x] Entity `Voucher`
- [ ] Repository (Suche per Code)
- [ ] Service mit Einlöse-Logik und Validierung
- [ ] REST-Controller inkl. Fehlerbehandlung
- [ ] Automatisierte Tests (Unit, Repository, Web, Integration)
- [ ] CORS-Konfiguration für das React-Frontend
- [ ] Frontend (`voucher-frontend`) und E2E-Tests
