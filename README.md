# RAF News - Fakultetski Portal za Vesti

RAF News je interaktivni portal za čitanje i upravljanje vestima. Ova aplikacija je osmišljena kako bi pružila korisnicima mogućnost da pregledaju najnovije vesti, interaguju sa sadržajem, i upravljaju njihovim korisničkim iskustvom.

## Funkcionalnosti

### Pregled Vesti
- Prikaz najnovijih vesti sa mogućnošću sortiranja po datumu.
- Filtriranje vesti po kategoriji kako bi se lakše pronašli relevantni sadržaji.
- Pretraga vesti po tagovima radi preciznijeg pronalaženja informacija.
- Predlog povezanih vesti

### Popularnost Vesti
- Lista vesti sa najviše reakcija (lajkova i dislajkova).
- Prikaz vesti po popularnosti, mereno brojem pregleda, uz ograničenje jednog pregleda po IP adresi.

### Interakcija sa Vestima
- Mogućnost lajkovanja i dislajkovanja vesti, sa ograničenjem jednog glasa po IP adresi.
- Komentarisanje vesti kako bi korisnici mogli izraziti svoje mišljenje i komentare.

### Korisnički Nalozi
- **Admin nalozi:** Obezbeđuju potpunu kontrolu nad aplikacijom. Mogu dodavati i brisati korisnike, upravljati njihovim privilegijama i sadržajem.
- **Content Creator nalozi:** Mogu dodavati nove kategorije i vesti, pružajući raznolikost sadržaja.

## Tehnički Detalji

### Front-end
- Vue.js: Front-end komponente su razvijene koristeći Vue.js framework.
- Axios: Korišćenje Axios biblioteke za HTTP komunikaciju sa back-end API-jem.

### Back-end
- JAX-RS: Back-end API je implementiran koristeći JAX-RS tehnologiju za kreiranje RESTful servisa.
- Baza podataka: MySQL baza podataka korišćena za skladištenje informacija o korisnicima, vestima, komentarima, pregledima i reakcijama.

### Autentikacija i Autorizacija
- JWT: Autentikacija korisnika i autorizacija privilegija realizovane putem JWT standarda.

## Instalacija i Pokretanje

1. Klonirajte ovaj repozitorijum na svoj lokalni uređaj.
2. **Back-end:** Uđite u `RAFNews` direktorijum i otvorite ga kao projekt u vašem Java razvojnom okruženju.
   - Pokrenite aplikaciju na Apache Tomcat serveru.
3. **Front-end:** Uđite u `RAFNewsFrontend` direktorijum i izvršite komandu `npm install` kako biste instalirali zavisnosti.
   - Pokrenite Vue.js aplikaciju komandom `npm run serve`.

## Napomena

RAF News je edukativni projekat osmišljen kako bi demonstrirao primenu tehnologija Vue.js i JAX-RS u razvoju web aplikacija. Ova aplikacija nije namenjena stvarnom distribuiranju vesti, već služi kao vežba u praktičnoj primeni naučenih veština.
