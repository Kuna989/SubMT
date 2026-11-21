# Podział zadań – SubMT

Zespół: **Jakub Łasiński** (J) oraz **Bartłomiej Kieżun** (B).

Zasada podziału: każdy odpowiada za odrębne moduły (pakiety), żeby ograniczyć konflikty przy merge'ach, a jednocześnie każdy ma w swojej części UI, logikę, testy i dokumentację – tak, aby obaj mogli obronić całość projektu.

## Planowane ekrany (min. 4 wymagane)

1. **Lista subskrypcji** (ekran główny, suma miesięczna) – J
2. **Dodawanie / edycja subskrypcji** (formularz z walidacją) – J
3. **Szczegóły subskrypcji** – J
4. **Statystyki** (koszty miesięczne, roczne, kategorie) – B
5. **Kalendarz płatności** – B
6. **Ustawienia** (waluta bazowa, przypomnienia, motyw, blokada) – B
7. **Ekran blokady** (biometria / PIN) – B

## Model danych (wstępnie)

`Subscription`: id, name, amount, currency, billingCycle (WEEKLY / MONTHLY / QUARTERLY / YEARLY), firstPaymentDate, nextPaymentDate, category, color, notes, reminderDaysBefore, isActive.

---

## Jakub Łasiński – fundament aplikacji, dane i CRUD

| # | Zadanie | Kryterium oceny |
|---|---|---|
| J1 | Szkielet projektu: Gradle (version catalog), struktura pakietów `data/domain/ui`, Hilt, `.gitignore`, motyw Material 3 (kolory, typografia, dark mode, dynamic color) | Architektura |
| J2 | Nawigacja (Navigation Compose, NavHost, bottom bar / navigation rail dla dużych ekranów) | UI/UX, responsywność |
| J3 | Baza danych Room: encja `Subscription`, DAO (Flow), migracje, `TypeConverter`y | Baza danych |
| J4 | Warstwa domain: modele, `SubscriptionRepository`, use case'y (dodaj, edytuj, usuń, pobierz, wylicz następną płatność) | Architektura |
| J5 | Ekran listy subskrypcji + suma miesięczna, wyszukiwanie/sortowanie, swipe-to-delete z cofnięciem (Snackbar „Cofnij”) | CRUD, UI |
| J6 | Formularz dodawania/edycji z walidacją (nazwa, kwota > 0, data, cykl), komunikaty błędów pod polami | Walidacja |
| J7 | Ekran szczegółów subskrypcji | CRUD |
| J8 | Eksport do CSV (Storage Access Framework, zapis na `Dispatchers.IO`) | Funkcje urządzenia |
| J9 | Testy jednostkowe: walidatory, use case'y, wyliczanie dat płatności, ViewModel listy/formularza; testy DAO (Room in-memory) | Testy |
| J10 | Dokumentacja: diagram architektury, diagram bazy danych (ERD), opis bibliotek, instrukcja uruchomienia (README) | Dokumentacja |

## Bartłomiej Kieżun – integracje, statystyki, przypomnienia i bezpieczeństwo

| # | Zadanie | Kryterium oceny |
|---|---|---|
| B1 | Integracja z REST API: Retrofit + kursy walut NBP (`api.nbp.pl`), przeliczanie subskrypcji w USD/EUR na walutę bazową, cache kursów w Room | Integracja z API |
| B2 | Obsługa błędów sieci: brak internetu, timeout, błędy HTTP → komunikaty dla użytkownika, fallback do ostatniego kursu z cache | Obsługa błędów |
| B3 | Ekran statystyk: suma miesięczna/roczna, podział na kategorie, prosty wykres (Canvas w Compose) | Funkcjonalność, UI |
| B4 | Ekran kalendarza nadchodzących płatności (widok miesiąca + lista płatności wybranego dnia) | Funkcjonalność |
| B5 | Przypomnienia: WorkManager (zadanie okresowe), powiadomienia, kanał powiadomień, obsługa uprawnienia `POST_NOTIFICATIONS` | Funkcje urządzenia |
| B6 | Ekran ustawień + DataStore (waluta bazowa, liczba dni przed płatnością, motyw, włączenie blokady) | Ustawienia |
| B7 | Bezpieczeństwo: blokada aplikacji biometrią (BiometricPrompt), `allowBackup=false`, `FLAG_SECURE` (opcjonalnie), R8/ProGuard w release | Bezpieczeństwo |
| B8 | Testy: jednostkowe dla przeliczania walut i statystyk (MockK, Turbine), testy UI Compose (formularz – walidacja, nawigacja, lista) | Testy |
| B9 | Dostępność: `contentDescription`, kontrast, rozmiary celów dotykowych, sprawdzenie Accessibility Scannerem | Wymagania niefunkcjonalne |
| B10 | Build release: podpisany APK, CI (GitHub Actions: build + testy) – opcjonalnie | Dokumentacja/APK |

## Zadania wspólne

- Opis celu i zakresu, specyfikacja wymagań, diagram przypadków użycia.
- Zrzuty ekranu najważniejszych widoków (telefon + tablet / tryb poziomy).
- Opis przeprowadzonych testów i ich wyników (raport z `./gradlew test` i `connectedAndroidTest`).
- Code review nawzajem – każdy PR zatwierdza druga osoba.
- Przygotowanie prezentacji i obrony – każdy musi umieć wyjaśnić także część drugiej osoby.

## Harmonogram (propozycja)

| Etap | Jakub | Bartek |
|---|---|---|
| Tydzień 1 | J1, J2, J3 (szkielet + baza) | B1 (API NBP, niezależny moduł), B6 (DataStore) |
| Tydzień 2 | J4, J5, J6 | B3, B4 (na danych z repozytorium Jakuba) |
| Tydzień 3 | J7, J8, J9 | B2, B5, B7 |
| Tydzień 4 | J10, poprawki | B8, B9, B10 |
| Tydzień 5 | Wspólnie: dokumentacja, screenshoty, APK, próba prezentacji | |

**Zależność krytyczna:** Bartek zaczyna statystyki/kalendarz po zmergowaniu J1–J4 (szkielet, Room, repozytorium). Do tego czasu pracuje nad modułami niezależnymi (API NBP, DataStore).

## Zasady pracy z Gitem

- Gałąź główna `main` – zawsze kompilująca się wersja.
- Praca na gałęziach `feature/<nazwa>` (np. `feature/room-database`, `feature/nbp-api`), merge przez Pull Request.
- Małe, opisowe commity (np. `Add subscription form validation`).
- Każdy commituje ze swojego konta – historia zmian pokazuje wkład obu osób.
