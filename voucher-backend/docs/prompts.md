# Verwendete Prompts

Prompts aus allen bisherigen Sessions dieses Projekts, chronologisch nach Session gruppiert.
Die Prompts früherer Sessions stammen aus den gespeicherten Transkripten (reiner Wortlaut,
lange eingefügte Texte sind mit „…“ gekürzt). Die letzte Session enthält zusätzlich Kontext
und Ergebnis.

## Session 13.10.2026 (Setup)

| Zeit  | Prompt |
|-------|--------|
| 10:58 | standard konfiguration für h2 db in application.properies |
| 10:58 | ja |
| 11:10 | erzeuge eine readme die die wesentlichen aspekte berücksichtigt. dieses projekt ist erstmal nur das backend. das frontend liegt später parallel zum backend in einem übergeordneten verzeichnis. *(+ eingefügte Projektbeschreibung „Voucher App – Gutscheine verwalten und einlösen“)* |

## Session 13.10.2026 (Requirements und Tests)

| Zeit  | Prompt |
|-------|--------|
| 18:59 | erzeuge eine requirements.md mit userstories und akzeptanzkkriterien |
| 19:13 | pom.xml hat warnungen - wie bekomme ich diese weg |
| 19:17 | Ich habe die Requirements.md gelöscht. Bitte diese noch einmal erzeugen. Inzwischen gibt es auch schon ein Controller und andere Klassen |
| 19:20 | Ja, bitte die offenen Punkte umsetzen |
| 20:02 | die letzte generierung habe ich gelöscht aus didaktischen Gründen. Mache mir ein Vorschlag jetzt für die Klasse VoucherServiceImplTest |
| 20:25 | Noch 2 weitere Tests in der VoucherServiceImplTest im Bild Test nach gleichem Vorbild erzeugen |

## Session 14.10.2026 (Frontend-Mockup)

| Zeit  | Prompt |
|-------|--------|
| 07:06 | generiere mir ein bild, welches das mögliche frontend zeigt |
| 07:13 | vergleiche requirements.md mit den ursprünglichen Anforderungen: *(+ eingefügte Anforderungsliste)* |
| 07:15 | ja, füge die beiden Stories ein und verschlanke den Mockup |
| 07:23 | https://claude.ai/artifact/4FXreRrx9GbGVKGt79Vga7 führt ins leere |

## Session 15.10.2026 (Service, DTOs, Controller, Controller-Tests)

| Zeit  | Prompt |
|-------|--------|
| 09:34 | erzeuge unter docs eine requirements.md mit userstories |
| 09:39 | neu Methode redeemVoucher - VoucherService - überprüfe die requirements und passe an |
| 09:40 | schlage mir für VoucherServiceTest weitere Tests vor |
| 09:41 | implementiere Punkt 1 und 2 |
| 09:42 | Tests ausführen |
| 10:27 | erzeuge im paket dto records CreateVoucherRequest und VoucherResponse |
| 10:32 | erzeuge einen VoucherController mit create und get |
| 10:43 | kommentiere VoucherController Methoden mit Path Example |
| 10:45 | erzeuge VoucherNotFoundException und ControllerAdvice |
| 10:46 | Ja, im Service umstellen |
| 10:46 | Ja, requirements aktualisieren |
| 10:49 | Tests ausführen |
| 10:51 | schlage mir Tests für VoucherController vor |
| 10:51 | Ja, implementiere GET und POST Happy Path |
| 10:53 | implementiere die POST Fehlerfälle und Edge Cases |

## Session 15.10.2026 (parallele Session, Controller anpassen)

| Zeit  | Prompt |
|-------|--------|
| 08:30 | welche Tests in VoucherServiceTest sollte ich noch abbilden? |
| 08:38 | erzeuge aus den bisherigen Informationen eine requirements.md unter docs/ |
| 08:40 | erzeuge die requirements.md neu mit Userstories und Akzeptanzkriterien |
| 08:46 | erzeuge unter berücksichtigung der dto einen VoucherController |
| 08:52 | der voucherService hat eine redeemVoucher Methode bekommen. passe den Controller an |

## Session 15.10.2026 (aktuell: Swagger und README)

Jeweils mit Kontext und Ergebnis.

## 1. README um Swagger-Aufruf ergänzen

> passe die readme an mit dem swagger aufruf /url

- **Kontext:** Swagger/OpenAPI war im Projekt nicht eingerichtet (kein `springdoc` in `pom.xml`).
- **Ergebnis:** Rückfrage mit zwei Optionen (Swagger einrichten und README ergänzen oder nur README vorbereiten).

## 2. Entscheidung für Option A

> A

- **Kontext:** Antwort auf die Rückfrage aus Prompt 1: Swagger zuerst einrichten, dann README ergänzen.
- **Ergebnis:** Dependency `springdoc-openapi-starter-webmvc-ui` 3.0.3 in `pom.xml`. README um Abschnitt „API-Dokumentation (Swagger)“ und Tech-Stack-Zeile erweitert.

## 3. REST-API-Abschnitt aktualisieren

> ja, passe den Abschnitt REST-API an den Ist-Stand an

- **Kontext:** Zustimmung zu meinem Vorschlag, den veralteten Abschnitt „REST-API (geplant)“ zu korrigieren.
- **Ergebnis:** Endpunkte, Statuscodes, Beispiel und Fehlerbehandlung (`404` per `GlobalExceptionHandler`) dokumentiert. `redeem`-Endpunkt als offen markiert.

## 4. Roadmap aktualisieren

> ja, aktualisiere die Roadmap

- **Kontext:** Zustimmung zu meinem Vorschlag, die Roadmap an den Code anzupassen.
- **Ergebnis:** Repository, Service, Controller, Tests und Swagger abgehakt. Offene Punkte (Einlöse-Endpunkt, Fehlerbehandlung, Repository-/API-Integrationstests, CORS, Frontend) einzeln aufgeführt.

## 5. Swagger starten

> starte swagger

- **Kontext:** Die App sollte lokal laufen, um Swagger UI zu nutzen.
- **Ergebnis:** App per `mvnw spring-boot:run` gestartet. Swagger UI unter `http://localhost:8080/swagger-ui.html`, OpenAPI-JSON unter `/v3/api-docs`.

