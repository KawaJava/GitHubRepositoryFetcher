# GitHub Repositories API

## 🇬🇧 [English version](#english-version)

## Opis aplikacji

Aplikacja udostępnia REST API, które dla podanego użytkownika GitHub zwraca listę jego publicznych repozytoriów, które **nie są forkami**.

Dla każdego repozytorium zwracane są następujące dane:
- **Repository Name** — nazwa repozytorium
- **Owner Login** — login właściciela
- **Branches** — lista gałęzi, gdzie dla każdej gałęzi podane są:
  - nazwa gałęzi,
  - SHA ostatniego commita.

---

## Technologie

- Java 21
- Spring Boot 3.5 (bez WebFlux)
- TestRestTemplate (integracyjne testy REST)
- JUnit 5

---

## Jak uruchomić

1. Sklonuj repozytorium.
2. Uruchom aplikację np. w IDE lub z linii poleceń:

```bash
./mvnw spring-boot:run
```
Aplikacja będzie dostępna na losowym porcie (konfigurowanym dynamicznie).

## Jak używać

Do testowania endpointów preferowaną aplikacją jest **Postman**, ale można również używać `curl` lub dowolnego innego narzędzia do wykonywania zapytań HTTP.

## Endpoint
GET /{username}
## Przykład
GET /kawajava
zwraca wszystkie repozytoria użytkownika rtyley, które nie są forkami, wraz z ich gałęziami.

## Test integracyjny
W projekcie znajduje się test integracyjny GitHubUserControllerHappyPathIT, który:

- uruchamia aplikację na losowym porcie,
- wywołuje endpoint z użytkownikiem octocat,
- weryfikuje, że w odpowiedzi znajdują się tylko repozytoria bez forków,
- sprawdza, czy każde repo zawiera niepuste pola oraz gałęzie z poprawnym SHA ostatnich commitów.
- test integracyjny nie używa mocków, korzysta z rzeczywistego API GitHub.

# English-version

## Application Description

This application exposes a REST API that returns a list of a given GitHub user's public repositories that **are not forks**.

For each repository, the following data is returned:
- **Repository Name** — the name of the repository  
- **Owner Login** — the login of the repository owner  
- **Branches** — a list of branches, where each branch includes:
  - the branch name  
  - the SHA of the latest commit  

---

## Technologies

- Java 21  
- Spring Boot 3.5 (without WebFlux)  
- TestRestTemplate (for integration REST tests)  
- JUnit 5  

---

## How to Run

1. Clone the repository.  
2. Run the application in your IDE or from the command line:

```bash
./mvnw spring-boot:run
```

The application will start on a random port (configured dynamically).

## How to Use

The preferred application for testing the endpoints is **Postman**, but you can also use `curl` or any other HTTP client.

## Endpoint
GET /{username}
## Example
GET /kawajava
Returns all repositories of the user kawajava that are not forks, including their branches.
## Integration Test
- The project includes an integration test GitHubUserControllerHappyPathIT, which:
- starts the application on a random port
- calls the endpoint with the user octocat
- verifies that only non-fork repositories are returned
- checks that each repository contains non-empty fields and branches with a valid latest commit SHA
- does not use mocks — it interacts with the real GitHub API
