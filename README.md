# HanaBank-Android-Test
Android application (take-home test) that displays a list of Pokémon cards with search, and pagination. Built using Kotlin + XML + MVVM, with Hilt, Coroutines/Flow, and Retrofit/OkHttp.

## Features And UI
[Screen_recording_20250810_184906.webm](https://github.com/user-attachments/assets/51d4f768-1d01-41e8-b43f-34b3d980ecf9){width=30%}



## ✨ Key Features
- Card list with pagination (lazy load).
- Search cards (query managed via StateFlow).
- loading/empty/error state indicators.
- Simple state management using the ApiResultHandler sealed class (Loading/Success/Error).

## 🧱 Architecture & Tech Stack
- Architecture: MVVM + Repository pattern
- ViewModel ⟷ Repository ⟷ (Remote + Local)
- UI: XML
- DI: Hilt
- Concurrency: Kotlin Coroutines & Flow
- Networking: Retrofit + OkHttp
- Image Loading: Glide
- Build: Gradle Kotlin DSL (build.gradle.kts)
