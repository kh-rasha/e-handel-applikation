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
## Implementerad funktionalitet – Order, Betalning & Lager 

Denna del av projektet ansvarar för **orderflöde, betalningslogik, lagerhantering samt tillhörande CLI-kommandon**. Fokus har legat på att implementera affärsregler, transaktioner och ett tydligt flöde från kundvagn till slutförd order.

### Orderflöde (Checkout)

Orderläggning sker via ett sammanhängande checkout-flöde som:

1. Skapar en order baserat på kundvagnens innehåll
2. Kontrollerar att produkter är aktiva
3. Validerar att tillräckligt lager finns
4. Reserverar lager vid orderläggning
5. Beräknar totalpris (pris × kvantitet per rad)
6. Simulerar betalning (90 % chans att godkännas)
7. Uppdaterar orderstatus baserat på betalningsresultat
8. Återställer lager vid misslyckad betalning

All orderläggning hanteras i ett transaktionellt service-lager för att säkerställa dataintegritet.

### Betalning

Betalningar modelleras separat och kopplas till ordern:

* Stöd för betalningsmetoder: `CARD`, `INVOICE`
* Betalningsstatus: `PENDING`, `APPROVED`, `DECLINED`
* Vid godkänd betalning sätts orderstatus till `PAID`
* Vid nekad betalning sätts orderstatus till `CANCELLED` och lager återställs

### Lagerhantering

Lager hanteras via en separat `Inventory`-entitet:

* Lager kontrolleras innan order kan skapas
* Lager reserveras (minskas) vid orderläggning
* Lager återställs automatiskt vid:
    * Misslyckad betalning
    * Manuell orderannullering

### Orderadministration

Funktionalitet för att hantera befintliga ordrar:

* Lista alla ordrar eller filtrera på status
* Visa detaljer för en specifik order (inkl. orderrader och betalningar)
* Avbryta order i status `NEW` med automatisk lageråterställning

### CLI-kommandon (Order & Checkout)

Följande konsolkommandon är implementerade:

* `checkout` – skapar order, reserverar lager och simulerar betalning
* `order list` – listar ordrar (valfri statusfiltrering)
* `order show` – visar detaljer för en specifik order
* `order cancel` – annullerar en ny order och återställer lager

Checkout-flödet använder kundvagns-sessionen och kräver att kund valts samt att kundvagnen inte är tom.

### Designval

* Affärslogik är placerad i service-lagret
* Transaktioner används för att säkerställa konsekventa order- och lageruppdateringar
* CLI-lagret är tunt och ansvarar endast för inmatning och presentation
* Felhantering sker via tydliga undantag och användarvänliga felmeddelanden

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

## Indexering
Indexering hanteras automatiskt via primärnycklar och unika constraints, vilket är tillräckligt för projektets omfattning.

---
## Arbetsfördelning

Arbetet i projektet har delats upp mellan sex gruppmedlemmar för att säkerställa tydligt ansvar och möjliggöra parallellt arbete.

# Medlem 1 – Projektgrund & Databasedesign

Skapande av Spring Boot-projekt och grundläggande projektstruktur

Konfiguration av PostgreSQL och JPA/Hibernate

Säker hantering av databasuppgifter via environment variables

Design och implementation av hela domänmodellen (JPA entities)

Implementation av relationer mellan entiteter

Skapande av enums för order- och betalningsstatus

Implementation av samtliga repositories

Framtagning av ER-diagram och teknisk dokumentation (README)

# Medlem 2 – Service-lager (grundstruktur)

Implementation av service-interfaces och grundläggande service-logik

Koppling mellan repositories och CLI

Hantering av grundläggande CRUD-funktionalitet

# Medlem 3 – Produkt- och kategorihantering

Funktionalitet för produkter och kategorier

CLI-kommandon för att lägga till, uppdatera, lista och söka produkter

Hantering av koppling mellan produkter och kategorier

# Medlem 4 – Kund och kundvagn

Kundhantering via CLI (skapa, lista, söka kund)

Enkel kundvagn per kund

CLI-kommandon för kund och kundvagn

Medlem 5 – Order-, betalnings- och lagerflöde

Implementation av orderflöde från kundvagn till order

Betalningssimulering och uppdatering av orderstatus

Lagerhantering och lagerreservation vid köp

CLI-kommandon för checkout och orderhantering

# Medlem 6 – Tester, data och scenarier

Repository- och service-tester

Negativa testfall och felhantering

CSV-import av produkter, kategorier och kunder

Skapande av testscenarier (small, medium, large)

Genom denna arbetsfördelning har projektet fått en stabil teknisk grund och möjliggjort parallellt arbete med funktionalitet, affärslogik och tester.


## Sammanfattning

Projektet har en stabil teknisk grund med korrekt domänmodell, databasintegration och arkitektur. Detta möjliggör vidare utveckling enligt uppgiftens krav och ger en tydlig struktur för grupparbete.
