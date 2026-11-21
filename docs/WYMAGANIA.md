# Wymagania projektowe – SubMT

Dokument referencyjny z wymaganiami prowadzącego. Każda funkcjonalność i każdy PR powinny być sprawdzane względem tej listy.

## 1. Cel projektu

Sprawdzenie umiejętności projektowania, implementowania, testowania i dokumentowania zaawansowanych aplikacji mobilnych na platformę Android.

## 2. Ogólne wymagania techniczne

| Obszar | Wymaganie | Decyzja w SubMT |
|---|---|---|
| Język programowania | Java lub Kotlin | Kotlin |
| Środowisko | Android Studio, aktualne stabilne Android SDK | Android Studio (stable), compileSdk/targetSdk – najnowsze stabilne |
| Architektura | MVVM lub Clean Architecture | MVVM + warstwy Clean (data / domain / ui) |
| Interfejs | Jetpack Compose lub XML z Material Design 3 | Jetpack Compose + Material 3 |
| Baza danych | Dowolny DBMS (np. Firebase, SQLite) | Room (SQLite) |
| Komunikacja sieciowa | REST API, Retrofit lub równoważne | Retrofit + API kursów walut NBP |
| Programowanie asynchroniczne | Kotlin Coroutines, Flow | Coroutines + Flow / StateFlow |
| Zarządzanie stanem | Dowolny | ViewModel + `StateFlow<UiState>` |
| Nawigacja | Dowolna | Navigation Compose |
| Przechowywanie ustawień | Dowolne | DataStore Preferences |
| Obsługa błędów | Walidacja danych, obsługa wyjątków, komunikaty dla użytkownika | Walidatory formularzy, `Result`/sealed errors, Snackbar |
| Testowanie | Testy jednostkowe i wybrane testy interfejsu | JUnit, MockK, Turbine, Compose UI Test |
| Bezpieczeństwo | Bezpieczne przechowywanie danych, kontrola dostępu, walidacja | Blokada biometryczna/PIN, `allowBackup=false`, walidacja wejścia |
| Wersjonowanie | Git, repozytorium z historią zmian | GitHub, gałęzie feature + Pull Requesty |
| Dokumentacja | Instrukcja uruchomienia, opis architektury, dokumentacja funkcjonalna | `README.md` + katalog `docs/` |

> Prowadzący może określić, które wymagania są obowiązkowe, a które stanowią element rozszerzający.

## 3. Wymagania funkcjonalne (minimum)

- [ ] **Zarządzanie danymi (CRUD)** – dodawanie, odczytywanie, edycja i usuwanie danych.
- [ ] **Walidacja formularzy** – sprawdzanie poprawności danych wejściowych.
- [ ] **Wieloekranowy interfejs** – minimum 4 logicznie odrębne ekrany.
- [ ] **Nawigacja** – przechodzenie pomiędzy ekranami z zachowaniem właściwego stanu.
- [ ] **Trwałość danych** – zapis i odczyt danych po ponownym uruchomieniu aplikacji.
- [ ] **Obsługa błędów** – informowanie o problemach z siecią, bazą danych i nieprawidłowych danych.

### Funkcje SubMT (z prezentacji tematu)

- [ ] Dodawanie subskrypcji z kwotą i cyklem płatności
- [ ] Przypomnienia przed płatnością
- [ ] Suma kosztów miesięcznych i statystyki roczne
- [ ] Kalendarz nadchodzących płatności
- [ ] Eksport do pliku CSV

## 4. Wymagania niefunkcjonalne

- [ ] Responsywność interfejsu i poprawne działanie na różnych rozmiarach ekranów.
- [ ] Zgodność z zasadami Material Design 3.
- [ ] Czytelny i modularny kod.
- [ ] Ograniczenie niepotrzebnych operacji w głównym wątku.
- [ ] Odporność na zmianę konfiguracji urządzenia (np. obrót ekranu).
- [ ] Podstawowe mechanizmy ochrony danych.
- [ ] Zgodność z zasadami dostępności (opisy elementów graficznych, odpowiedni kontrast).
- [ ] Możliwość rozbudowy aplikacji o kolejne funkcjonalności.

## 5. Kryteria oceny (100 pkt)

| Kryterium | Punkty |
|---|---|
| Realizacja wymagań funkcjonalnych | 25 |
| Architektura i jakość kodu | 15 |
| Interfejs użytkownika i UX | 10 |
| Baza danych i zarządzanie danymi | 10 |
| Integracja z API lub funkcjami urządzenia | 10 |
| Obsługa błędów i bezpieczeństwo | 10 |
| Testy jednostkowe i funkcjonalne | 10 |
| Dokumentacja techniczna | 5 |
| Prezentacja i obrona projektu | 5 |
| **Łącznie** | **100** |

### Skala ocen

| Punkty | Ocena |
|---|---|
| 0–49 | 2,0 – niedostateczny |
| 50–59 | 3,0 – dostateczny |
| 60–69 | 3,5 – dostateczny plus |
| 70–79 | 4,0 – dobry |
| 80–89 | 4,5 – dobry plus |
| 90–100 | 5,0 – bardzo dobry |

**Warunek zaliczenia:** samodzielne zaprezentowanie działania aplikacji oraz wykazanie się znajomością zastosowanych rozwiązań – każdy z nas musi rozumieć cały projekt, nie tylko swoją część.

## 6. Wymagana dokumentacja projektu

- [ ] Opis celu i zakresu aplikacji.
- [ ] Specyfikacja wymagań funkcjonalnych i niefunkcjonalnych.
- [ ] Diagram architektury aplikacji.
- [ ] Diagram przypadków użycia oraz diagram bazy danych.
- [ ] Zrzuty ekranu najważniejszych widoków.
- [ ] Opis wykorzystanych bibliotek i technologii.
- [ ] Instrukcja instalacji i uruchomienia.
- [ ] Opis przeprowadzonych testów i ich wyników.
- [ ] Link do repozytorium Git.
- [ ] Plik APK (lub inna uzgodniona forma prezentacji działającej aplikacji).
