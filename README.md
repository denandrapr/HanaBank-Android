# HanaBank-Android-Test
Android application (take-home test) that displays a list of Pokémon cards with search, and pagination. Built using Kotlin + XML + MVVM, with Hilt, Coroutines/Flow, and Retrofit/OkHttp.

## 📱 Features & UI
<p align="center">
  <img src="https://github.com/user-attachments/assets/210cb0fd-4ac0-40eb-b306-060232fd1ec0" width="30%" />
  <img src="https://github.com/user-attachments/assets/2e3e6eee-8738-4619-9bbf-4855350227e7" width="30%" />
  <img src="https://github.com/user-attachments/assets/51c98c79-f61a-4584-b3ef-2d6375810ba4" width="30%" />
  <img src="https://github.com/user-attachments/assets/33d42f27-9712-4640-8dc9-525abceba526" width="30%" />
  <img src="https://github.com/user-attachments/assets/ec060ef8-5d54-460f-99cc-3635d105ff7a" width="30%" />
</p>

## ✨ Key Features
- Card list with pagination (lazy load).
- Search cards (query managed via StateFlow).
- loading/empty/error state indicators.
- Simple state management using the ApiResultHandler sealed class (Loading/Success/Error).

## 🧱 Architecture & Tech Stack
- Architecture: MVVM + Repository pattern
- ViewModel ⟷ Repository ⟷ (Remote)
- UI: XML
- DI: Hilt
- Concurrency: Kotlin Coroutines & Flow
- Networking: Retrofit + OkHttp
- Image Loading: Glide
- Build: Gradle Kotlin DSL (build.gradle.kts)

## 📂 Folder Structure
```
app/
 ├── data/
 │    ├── remote/         # Retrofit API definitions & DTOs
 │    ├── repository/     # Repository implementations
 │
 ├── ui/
 │    ├── home/           # Home screen
 │    ├── detail/         # Detail screen
 │    ├── splash/         # Splash screen
 │
 ├── di/                  # Hilt modules
 ├── utils/               # Helpers & extensions
```
 ## 🚀 Setup & Run
 1.	Clone the repository
 2.	Add your API Key to local.properties
```
POKEMON_API_KEY=your_api_key_here
BASE_URL=https://api.pokemontcg.io/v2/
```
