# ClubApp

A modern Android application designed to streamline the management of university clubs, events, and memberships. Built with the latest Android technologies, the app emphasizes user experience, performance, and clean architecture.

---

## Features

- Google Authentication – Sign in securely using Firebase Authentication  
- Club Management – Browse, join, and explore university clubs  
- Event Participation – View event details, join upcoming events, and manage participation  
- Push Notifications – Receive real-time updates with Firebase Cloud Messaging  
- Persistent Login – Seamless login experience using DataStore-based session storage  
- Context-Aware Access Control – Automatically restrict access to expired events and enforce role-based actions  

---

## Tech Stack

- Language: Kotlin  
- UI Framework: Jetpack Compose  
- Architecture: MVVM (Model–View–ViewModel) with ViewModels and State Flows  
- Authentication: Firebase Authentication  
- Networking: Retrofit and OkHttp  
- Data Serialization: Kotlinx Serialization  
- Data Persistence: Jetpack DataStore  

---

## Project Structure Highlights

- Clear separation of concerns across UI, domain, and data layers  
- Firebase ID token-based session handling  
- Efficient caching for static data such as clubs and events  
- Built-in intent support for contacting club administrators via email or phone  

---

Let me know when you're ready to draft the backend README.
