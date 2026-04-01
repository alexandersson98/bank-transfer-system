# Bank Transfer System (Java, Spring Boot, PostgreSQL)

Backend-system för att hantera banköverföringar mellan konton med fokus på transaktionssäkerhet, tydlig affärslogik och testbarhet.

---

## Funktionalitet

* Överför pengar mellan konton
* Validerar indata (belopp, konton)
* Säkerställer att tillräckligt saldo finns
* Loggar transaktioner som **SUCCESS** eller **FAILED**
* Rollback vid tekniska fel

---

## Arkitektur

* **Repository** – databasåtkomst (Account, TransactionLog)
* **Service** – affärslogik (Transfer, Logging)
* Tydlig separation av ansvar enligt lagerindelad struktur

---

## Transaktionshantering

* Huvudtransaktion hanterar saldoändringar
* Vid fel rullas ändringar tillbaka (rollback)
* Misslyckade överföringar loggas i separat transaktion
* Valideringsfel loggas inte (ingen faktisk transaktion påbörjas)

---

## Databas

* PostgreSQL
* Databaser:

  * `bank_db` (applikation)
  * `bank_db_test` (tester)
* Tabeller:

  * `account`
  * `transaction_log`
* Schema skapas via SQL-script

---

## Tester

* Integrationstester körs mot separat testdatabas
* Testdata skapas dynamiskt inför varje test
* Verifierar:

  * saldoändringar
  * korrekt loggning i `transaction_log`

Kör tester:

```bash
mvn test
```

---

## Teknik

* Java
* Spring Boot
* PostgreSQL
* Maven
* JUnit

---

## Om projektet

Detta projekt utvecklades som en del av en utbildning inom systemutveckling, med fokus på att bygga realistisk backend-logik och testa transaktionella flöden.
