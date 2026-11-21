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

## Harmonogram – 2 tygodnie (21.11 – 5.12.2026)

| Dni | Jakub | Bartek |
|---|---|---|
| 21–22.11 | ✅ J1, J2, J3 (szkielet, nawigacja, Room) | B1 (API NBP – niezależny moduł), B6 (DataStore) |
| 23–25.11 | J4 (najpierw interfejs repozytorium!), J5, J6 | B2 (błędy sieci), B5 (przypomnienia) |
| 26–28.11 | J7, J8 (CSV) | B3 (statystyki), B4 (kalendarz) – na repozytorium z J4 |
| 29.11–1.12 | J9 (testy jednostkowe) | B7 (biometria), B8 (testy UI), B9 (dostępność) |
| **2.12** | **Feature freeze** – od tej chwili tylko poprawki błędów | |
| 2–4.12 | J10 (diagramy, README) | B10 (podpisany APK) |
| 4–5.12 | Wspólnie: zrzuty ekranu, opis testów, próba prezentacji | |

**Zależność krytyczna:** Bartek zaczyna statystyki i kalendarz po zmergowaniu J3–J4 (Room + repozytorium). Dlatego Jakub w J4 najpierw wypycha sam interfejs `SubscriptionRepository` i model domenowy – Bartek może od razu pisać na nim kod i testy (z fake'owym repozytorium). Do tego czasu Bartek robi moduły niezależne (API NBP, DataStore, przypomnienia).

**Przy braku czasu wycinamy (w tej kolejności):** CI w GitHub Actions, `FLAG_SECURE`, wykres w statystykach (zostaje lista sum), wyszukiwanie na liście. Nie wycinamy niczego, co jest w kryteriach oceny (CRUD, walidacja, API, testy, dokumentacja).

## Zasady pracy z Gitem

- Gałąź główna `main` – zawsze kompilująca się wersja.
- Praca na gałęziach `feature/<nazwa>` (np. `feature/room-database`, `feature/nbp-api`), merge przez Pull Request.
- Małe, opisowe commity (np. `Add subscription form validation`).
- Każdy commituje ze swojego konta – historia zmian pokazuje wkład obu osób.
