# StudyBuddy
 
**Android App for Matching Students with Study Partners**
 
StudyBuddy is a native Android application built in **Kotlin** and **Java** that matches students with compatible study partners. The app connects to a REST API backend using **Retrofit**, demonstrating client-server architecture, API integration, and modern Android development practices.
 
---
 
## Core Functionality
 
- User matching that pairs students with compatible study partners
- REST API integration using **Retrofit** for network requests and JSON parsing
- Native Android frontend built in **Kotlin**
- Dedicated backend module serving the matching API
---
 
## Architecture
 
The project is organized as separate modules with independent responsibilities:
 
```
StudyBuddy/
├── backend/        # API server handling user data and matching logic
├── frontend/app/   # Native Android application (Kotlin)
└── gradle/         # Gradle wrapper and build configuration
```
 
This structure highlights:
 
- Separation of concerns between client and server
- REST-based communication between frontend and backend
- Reproducible builds via the Gradle wrapper (Kotlin DSL)
---
 
## Technology Stack
 
- **Kotlin** – primary Android application language
- **Java** – supporting application code
- **Retrofit** – type-safe HTTP client for REST API integration
- **Gradle (Kotlin DSL)** – build system and dependency management
- **Android SDK** – native mobile platform
---
 
## Getting Started
 
1. Clone the repository:
```
   git clone https://github.com/KimMaynard/StudyBuddy.git
```
2. Open the project in Android Studio.
3. Sync Gradle and run the backend module.
4. Build and run the app on an emulator or physical device.
 
## Skills Demonstrated
 
- Native Android development in Kotlin
- REST API design and integration
- Client-server architecture
- Object-oriented programming
- Version control with Git (68 commits)
---
 
## Potential Extensions
 
- Push notifications for new match alerts
- In-app messaging between matched study partners
- Course and schedule-based matching filters
- Cloud deployment of the backend (AWS)
---
 
## Project Context
 
Developed as a university software engineering project at Oakland University (100% final grade). This project builds on earlier serverless work ([learnAWSkids](https://github.com/bennytalks/learnAWSkids)) and represents a step toward full-stack mobile development.
