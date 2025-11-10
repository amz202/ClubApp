# ClubApp

A modern Android application designed to streamline the management of university clubs, events, and memberships. Built with the latest Android technologies, the app emphasizes user experience, real-time interaction, and clean architecture.

---

## Features

- Google Sign-In with Firebase Authentication  
- **Club management:** Browse, join, and explore university clubs  
- **Event system:** View, participate in, and track club events  
- Real-time group chat for each club using WebSockets  
- Push notifications via Firebase Cloud Messaging  
- Offline mode for clubs and events list using Room Database
- Role-based access control for administrative actions  
- Persistent login via Jetpack DataStore

---

## Tech Stack

- **Language**: Kotlin  
- **UI Framework**: Jetpack Compose  
- **Architecture**: MVVM with StateFlows  
- **Networking**: Retrofit (HTTP) + Ktor WebSocket  
- **Authentication & Session**: Firebase Authentication + Jetpack DataStore  
- **Database**: Azure PostgreSQL and MongoDB (cloud) + Room (local caching)
- **Image Loading**: Coil  
- **Serialization**: kotlinx.serialization  

---

## Project Structure Highlights

- Clean separation of concerns across UI, domain, and data layers  
- Uses repository pattern for data access  
- Firebase ID tokens are attached to requests for secure communication  
- Efficient caching of clubs and events  
- Offline-first design (currently for clubs and events lists): Loads instantly from Room cache and syncs fresh data from the server in the background.
- Built-in support for contacting club admins and event heads via gmail intent
- **Backend**: [Ktor Backend Server](https://github.com/amz202/ktor-clubapp) with Firebase Auth verification, Azure PostgreSQL, and MongoDB


---

## Version 1.2

> **ClubApp v1.2** introduces controlled club access, structured membership requests and offline-first support for clubs and events lists using Room. The update also improves UI responsiveness, error handling, and overall backend stability.  

### Highlights

- **Club access control** – Creators can open or close clubs, controlling whether new requests are allowed  
- **Join requests** – Users can request to join open clubs; creators and moderators can accept or reject them  
- **Offline Mode** – Clubs and Events lists are available even without internet connectivity, thanks to local caching via Room
- **Event–club link** – Events now show their parent club name, with quick navigation back to the club page  
- **Error handling** – Error screen redesigned with a retry button for faster recovery  
- **Responsive UI** – Member status updates (club or event) now reflect instantly, with smoother caching and state handling  
- **Backend** – Database migrated to **Supabase** for improved testing  

---

## UI Previews

### Home, Clubs, and Events
![Slide 1](https://github.com/user-attachments/assets/546b3186-4096-4fa5-86a6-ce71342a2663)

### Event Creation and Details
![Slide 2](https://github.com/user-attachments/assets/1574c537-2346-446e-8c9b-cb213121deb0)

### Club Creation, Club Detail, and Club Chat
![Slide 3](https://github.com/user-attachments/assets/3eb8aee4-5382-402d-ad38-e948d1fd9997)

---

## License

This project is licensed under the [Apache License 2.0](./LICENSE).

---


