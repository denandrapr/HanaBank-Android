# HanaBank-Android-Test
Android application (take-home test) that displays a list of Pokémon cards with search, and pagination. Built using Kotlin + XML + MVVM, with Hilt, Coroutines/Flow, and Retrofit/OkHttp.

## Features And UI
![App Screenshot](https://github.com/user-attachments/assets/2e3e6eee-8738-4619-9bbf-4855350227e7)
![App Screenshot](https://github.com/user-attachments/assets/33d42f27-9712-4640-8dc9-525abceba526)





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
