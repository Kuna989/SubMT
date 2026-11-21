# SubMT

Aplikacja mobilna na Androida do zarządzania subskrypcjami (Netflix, Spotify i inne).

Autorzy: Jakub Łasiński, Bartłomiej Kieżun

## Funkcje

- Dodawanie subskrypcji z kwotą i cyklem płatności
- Przypomnienia przed płatnością
- Suma kosztów miesięcznych i statystyki roczne
- Kalendarz nadchodzących płatności
- Eksport do pliku CSV

## Technologie

| Obszar | Rozwiązanie |
|---|---|
| Język | Kotlin |
| UI | Jetpack Compose, Material Design 3 |
| Architektura | MVVM + warstwy data / domain / ui |
| Wstrzykiwanie zależności | Hilt (KSP) |
| Asynchroniczność | Kotlin Coroutines, Flow |
| Build | Gradle (Kotlin DSL) + version catalog (`gradle/libs.versions.toml`) |

Konfiguracja: `minSdk 26` (Android 8.0), `targetSdk 37`, `compileSdk 37.2`, Java 17.

## Uruchomienie

Wymagania: aktualne stabilne **Android Studio** (z obsługą Android Gradle Plugin 9.4) oraz JDK 17 lub nowszy (wbudowany w Android Studio).

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/Kuna989/SubMT.git
   ```
2. Otwórz katalog `SubMT` w Android Studio (*File → Open*) i poczekaj na synchronizację Gradle. Jeśli Studio poprosi o doinstalowanie SDK Platform 37.2, zaakceptuj.
3. Uruchom konfigurację `app` na emulatorze lub telefonie (Android 8.0+).

Z linii poleceń:

```bash
./gradlew assembleDebug
```

```bash
./gradlew test
```

```bash
./gradlew connectedAndroidTest
```

APK debug: `app/build/outputs/apk/debug/app-debug.apk`.

## Struktura projektu

```
app/src/main/java/pl/submt/
├── SubMtApplication.kt   # klasa Application (@HiltAndroidApp)
├── MainActivity.kt       # jedyna aktywność, hostuje Compose
├── di/                   # moduły Hilt (dispatchery, baza, sieć)
├── data/                 # Room, Retrofit, DataStore, implementacje repozytoriów
├── domain/               # modele, interfejsy repozytoriów, use case'y
└── ui/                   # ekrany Compose + ViewModele, motyw (ui/theme)
```

## Dokumentacja

- [Wymagania projektowe](docs/WYMAGANIA.md)
- [Podział zadań i harmonogram](docs/PODZIAL_ZADAN.md)
