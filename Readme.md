# Nutzung unseres project_management_services

Wir, das sind Caro, Oke und Mirco, freuen uns, dass Sie sich für die Nutzung unseres 
Services entschieden haben!  
In dem folgenden Text erklären wir Ihnen, welche Schnittstellen unser Service bietet und wie
Sie diesen am besten benutzen können.  
Sollten Sie lieber einen Blick auf die durch OpenAPI generierte beschreibung werfen wollen,
nutzen Sie den Endpunkt ``http://localhost:8089/swagger``.

## Vor dem Starten
1. Bitte stellen Sie sicher, dass Docker auf Ihrem PC installiert ist und gestartet wurde.  
2. Vor dem Starten der Anwendung sollten Sie außerdem sicherstellen, dass ein Postgres Container
   gestartet wurde (siehe [Postgres](#Postgres)).

## Nutzung der Anwendung:
Bitte berücksichtigen Sie, dass Sie für die Nutzung unserer Endpunkte immer mit der folgenden Basis
beginnen müssen: ``http://localhost:8089/v1/api``  
In unserer Anwendung haben wir zwischen normalen Mitarbeitern (``user``) und Projektleitern (``project_ownern``)
unterschieden. Im Orderner ``requests`` finden Sie zwei Dateien, mit deren Hilfe Sie für jeweils eine der Rollen
einen Token generieren können.

### Endpunkte:
Wir haben unsere Endpunkte in zwei Kategorien unterteilt, während alle Endpunkte, die ein Projekt als Hauptakteur
haben, unter dem Anhängsel ``/project`` erreichbar sind, ist die Abfrage aller Projekte eines Mitarbeiters unter 
dem Anhängsel ``/employee`` möglich. Zusammengesetzt ergeben sich also die beiden Endpunkt-Grundgerüste:
- ``http://localhost:8089/v1/api/controller``
- ``http://localhost:8089/v1/api/employee``  

Bei POST- und PUT-Methoden sind zudem noch Request Bodys zu übergeben. In beiden Fällen muss hierbei ein Projekt in 
JSON-Format übergeben werden. Im folgenden Beispiel sind alle Pflichtfelder mit einem Sternchen markiert:

```
{
*   "description": "Test description", 
*   "customerId": 1,
    "comment": "Test comment",
*   "projectLeader": 10,
*   "startDate": "2025-08-30T23:59:59.9",
*   "plannedEndDate": "2025-12-01T00:00:00.0",
    "endDate": "",
    "addEmployeeDTOs": [{"id":10, "skillWithinProject": "Java"},{"id": 20, "skillWithinProject": "Angular"}],
    "qualifications": ["Java","Angular"]
}
```
An diese Grundgerüste werden jetzt die folgenden Endpunkte angehängt:

#### Projekt-Endpunkte (``/project``)
- Anlegen eines Projektes (POST)
- Löschen eines Projektes (DELETE): ``/delete/{id}``
- Auslesen aller Daten eines Projektes (GET): ``/read/{id}``
- Aktualisieren eines Projektes (PUT): ``/update/{id}``
- Auslesen aller Projekte (GET): ``/read``
- Auslesen aller Mitarbeitenden eines Projektes (GET): ``/readEmployees/{id}``
- Löschen eines Mitarbietenden aus einem Projekt (DELETE): ``/delete/{projectId}/{employeeId}``

#### Mitarbeiter-Endpunkt (``/employee``)
- Alle Projekte eines Mitarbeiters (GET): ``/{id}``

## Integrations Tests
Im Packet ``test/java/de.szut.lf8_project/integrationTests`` finden Sie zwei Packete mit Testklassen,
die jeden der implementierten Endpunkte testen. Für die erfolgreiche Ausführung dieser ist es notwendig,
dass Docker gestartet wurde.


# Postgres
### Terminal öffnen
für alles gilt, im Terminal im Ordner docker/local sein
```bash
cd docker/local
```
### Postgres starten
```bash
docker compose up
```
Achtung: Der Docker-Container läuft dauerhaft! Wenn er nicht mehr benötigt wird, sollten Sie ihn stoppen.

### Postgres stoppen
```bash
docker compose down
```

### Postgres Datenbank wipen, z.B. bei Problemen
```bash
docker compose down
docker volume rm local_lf8_starter_postgres_data
docker compose up
```