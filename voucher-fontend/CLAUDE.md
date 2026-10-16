# CLAUDE.md

## Projektkontext

Dieses Verzeichnis enthält das Frontend der Voucher-Anwendung.

Technologien:

- Vite
- TypeScript
- HTML
- CSS
- kein React
- kein weiteres Frontend-Framework

Das Frontend kommuniziert mit einem separaten Spring-Boot-Backend.

## Wichtige Dokumente

Bevor Änderungen vorgenommen werden, zuerst folgende Dateien prüfen:

- `docs/requirements.md`
- `docs/prompts.md`
- `README.md`

`docs/prompts.md` dient als fortlaufende Dokumentation der verwendeten AI-Prompts und muss bei jeder relevanten Aufgabe aktualisiert werden.

## Projektstruktur

Tech-Stack, vollständige Struktur, Befehle und Konfiguration: siehe `README.md`.

Verantwortlichkeiten:

- `src/api/`: Kommunikation mit dem Backend
- `src/model/`: TypeScript-Typen und Interfaces
- `src/ui/`: DOM-Manipulation und Darstellung
- `src/main.ts`: Einstiegspunkt und Ablaufsteuerung des Standardbereichs
- `src/admin.ts`: Einstiegspunkt und Ablaufsteuerung des Admin-Bereichs
- `src/style.css`: Styling
- `e2e/`: Playwright-Tests und Testdaten-Helfer

Standard- und Admin-Bereich bleiben getrennt: eigene HTML-Seite, eigener Einstiegspunkt, eigene View. Gemeinsam genutzt werden nur `src/api/`, `src/model/` und `src/ui/dom.ts`.

## Architekturregeln

Halte die Anwendung bewusst einfach.

Bevorzuge kleine Funktionen gegenüber unnötigen Klassen.

Trenne insbesondere:

- Backend-Kommunikation
- Datenmodell
- UI-Darstellung
- Ablaufsteuerung

Keine API-Aufrufe direkt in UI-Funktionen.

Keine DOM-Manipulation in API-Funktionen.

Keine Businesslogik in `index.html`.

Keine unnötigen Abstraktionen oder Design Patterns einführen.

## TypeScript

- TypeScript strict verwenden.
- `any` vermeiden.
- Für Backend-Daten explizite Interfaces definieren.
- Funktionen möglichst klar typisieren.
- Fehler nicht stillschweigend ignorieren.

## HTML und Accessibility

Semantisches HTML verwenden.

Formularfelder müssen ein zugeordnetes `<label>` besitzen.

Buttons sollen verständliche sichtbare Texte haben.

Die Oberfläche soll so aufgebaut sein, dass sie später gut mit Playwright getestet werden kann.

Bevorzugte Playwright-Selektoren sind:

- Rollen
- Labels
- sichtbare Texte

`data-testid` nur verwenden, wenn semantische Selektoren nicht sinnvoll möglich sind.

## Backend-Kommunikation

HTTP-Aufrufe befinden sich ausschließlich unter `src/api/`.

`fetch()` verwenden.

Die Backend-Basis-URL zentral definieren und nicht mehrfach hardcodieren. Sie wird in `src/api/voucherApi.ts` aus `VITE_API_BASE_URL` gelesen (Details in `README.md`).

HTTP-Fehler explizit behandeln.

Das Backend nicht verändern, wenn eine Aufgabe ausschließlich das Frontend betrifft.

## Änderungen

Vor größeren Änderungen zunächst prüfen, ob sie mit `docs/requirements.md` vereinbar sind.

Bestehende funktionierende Strukturen nicht unnötig umbauen.

Nur Dateien ändern, die für die aktuelle Aufgabe notwendig sind.

Keine zusätzlichen Libraries installieren, wenn die Aufgabe ohne sie sinnvoll lösbar ist.

## Tests

E2E-Tests liegen in `e2e/` und laufen mit Playwright.

Beim Implementieren darauf achten, dass:

- Eingabefelder eindeutig identifizierbar sind
- Buttons klare Beschriftungen besitzen
- Ergebnisse sichtbar im DOM erscheinen
- Fehlermeldungen eindeutig dargestellt werden (`role="alert"`)

Regeln für Tests:

- Selektoren: `getByRole`, `getByLabel`, `getByText`; kein `data-testid`.
- Testdaten per Backend-API anlegen (`e2e/helpers.ts`), mit eindeutigem Code pro Lauf.
- Keine neuen Tests ohne ausdrückliche Aufgabe.

## Dokumentation

Keine fachlichen Anforderungen erfinden. Maßgeblich ist `docs/requirements.md`.

### Prompts

Jeder relevante Prompt wird fortlaufend nummeriert in `docs/prompts.md` ergänzt.
