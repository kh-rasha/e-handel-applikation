# E‑handelapplikation – Inlämning

## Projektbeskrivning

Detta projekt är en **konsolbaserad e‑handelapplikation** utvecklad i Java med **Spring Boot**, **Spring Data JPA** och **PostgreSQL**. Syftet med projektet är att tillämpa objektorienterad design, databashantering och lagerindelad arkitektur i ett grupparbete.

Applikationen är uppbyggd för att stödja ett komplett flöde för produkter, kunder, ordrar, betalningar och lager.

---

## Tekniker och ramverk

* Java 17/21
* Spring Boot
* Spring Data JPA (Hibernate)
* PostgreSQL
* Maven
* Git / GitHub

---

## Arkitektur

Projektet följer en tydlig lagerindelning:

* **Domain layer** – JPA‑entities och enums
* **Repository layer** – Databåtkomst via Spring Data JPA
* **Service layer** – Affärslogik (implementeras vidare av gruppen)
* **CLI layer** – Konsolgränssnitt (implementeras vidare av gruppen)

Denna struktur möjliggör parallellt arbete och god separering av ansvar.

---

## Implementerad funktionalitet (grundarbete)

Följande delar är fullt implementerade och fungerar som projektets tekniska grund:

### Databas och konfiguration

* PostgreSQL som databashanterare
* JPA/Hibernate‑konfiguration
* Automatisk schemagenerering (`ddl-auto=update`)
* Säker hantering av databasuppgifter via **environment variables** (inga lösenord i källkod)

### Domänmodell (JPA Entities)

Samtliga entities är implementerade med korrekta relationer, constructors och kontrollerad åtkomst via getters/setters:

* **Product**
* **Category**
* **Customer**
* **Inventory**
* **Order**
* **OrderItem**
* **Payment**

Relationer:

* Product ↔ Category (Many‑to‑Many)
* Customer → Order (One‑to‑Many)
* Order → OrderItem (One‑to‑Many)
* Order → Payment (One‑to‑Many)
* Product → Inventory (One‑to‑One)

### Enums

* `OrderStatus` (NEW, PAID, CANCELLED)
* `PaymentStatus` (PENDING, APPROVED, DECLINED)
* `PaymentMethod` (CARD, INVOICE)

### Repository‑lager

Alla repositories är implementerade med Spring Data JPA:

* ProductRepository
* CategoryRepository
* CustomerRepository
* InventoryRepository
* OrderRepository
* OrderItemRepository
* PaymentRepository

### Designprinciper

* No‑args constructors för JPA
* Getters för dataläsning
* Setters endast för fält som är avsedda att ändras
* Affärslogik kapslad där det är lämpligt (t.ex. beräkning av `lineTotal` i `OrderItem`)

---

## Köra projektet

### Förutsättningar

* Java installerat
* PostgreSQL körs lokalt
* Databas skapad:

```sql
CREATE DATABASE ecommerce;
```

### Environment Variables

För att köra applikationen krävs följande variabler:

```
PG_USERNAME=postgres 
PG_PASSWORD=<PG_PASSWORD>
```

### Start

Applikationen startas genom att köra huvudklassen:

```
EHandelApplikationApplication
```

Vid start skapas databastabeller automatiskt.



---

## Sammanfattning

Projektet har en stabil teknisk grund med korrekt domänmodell, databasintegration och arkitektur. Detta möjliggör vidare utveckling enligt uppgiftens krav och ger en tydlig struktur för grupparbete.
