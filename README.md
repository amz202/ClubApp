# ClubApp

A modern Android application designed to streamline the management of university clubs, events, and memberships. Built with the latest Android technologies, the app emphasizes user experience, real-time interaction, and clean architecture.

---

## Features

- Google Sign-In with Firebase Authentication  
- Club management: browse, join, and explore university clubs  
- Event system: view, participate in, and track club events  
- Real-time group chat for each club using WebSockets  
- Push notifications via Firebase Cloud Messaging  
- Persistent login via Jetpack DataStore  
- Role-based access control for administrative actions  
- UI state consistency on session change  

---

## Tech Stack

- **Language**: Kotlin  
- **UI Framework**: Jetpack Compose  
- **Architecture**: MVVM with StateFlows  
- **Networking**: Retrofit (HTTP) + Ktor WebSocket  
- **Authentication & Session**: Firebase Authentication + Jetpack DataStore  
- **Real-time Chat**: WebSocket messaging with MongoDB backend  
- **Image Loading**: Coil  
- **Serialization**: kotlinx.serialization  

---

## Project Structure Highlights

- Clean separation of concerns across UI, domain, and data layers  
- Uses repository pattern for data access  
- Firebase ID tokens are attached to requests for secure communication  
- Efficient caching of clubs and events  
- Built-in support for contacting club admins and event heads via gmail intent  
- Real-time updates with message observers tied to UI lifecycle  

---

## Version 1.1

> **ClubApp v1.1** adds real-time communication, improves session consistency, and enhances user experience with backend-driven interactions.

### Highlights

- Real-time group chat support using WebSocket for each club  
- Automatic group creation on club creation (1:1 mapping with backend)  
- Message history loaded from MongoDB; includes sender and timestamp  
- Users can delete their own messages from the chat screen  
- Resolved session cache issues on logout and re-login  
- Profile picture pulled from Google account and shown on the home screen  

---

## UI Previews

### Home, Clubs, and Events
![Slide 1](https://github.com/user-attachments/assets/546b3186-4096-4fa5-86a6-ce71342a2663)

### Event Creation and Details
![Slide 2](https://github.com/user-attachments/assets/1574c537-2346-446e-8c9b-cb213121deb0)

### Club Creation, Club Detail, and Club Chat
![Slide 3](https://github.com/user-attachments/assets/f0d4b59c-a36a-4c2f-aa53-079209d3bf39)

---

