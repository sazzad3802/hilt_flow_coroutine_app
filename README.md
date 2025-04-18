# Hilt Flow Feed Application 📱

A modern Android feed application that displays posts with smooth scrolling, pull-to-refresh, offline support, and efficient image loading. Built with Jetpack components, Retrofit, Room, and Hilt.

| ![Feed List](images/HiltFeed.jpg) |

## Features ✨
- **Dynamic Feed**: Scrollable list of posts with card-based UI
- **Pull-to-Refresh**: Swipe down to fetch latest content
- **Offline Support**: Caches data locally using Room
- **Fast Image Loading**: Glide for efficient thumbnail handling
- **Clean Architecture**: MVVM with ViewModel/LiveData
- **Dependency Injection**: Hilt for simplified DI

## Tech Stack 🛠️
| Category          | Components                                                                |
|-------------------|---------------------------------------------------------------------------|
| Core              | Kotlin, Coroutines, Jetpack, Flow                                         |
| UI                | Material Design, RecyclerView, CardView, SwipeRefreshLayout               |
| Networking        | Retrofit 2 + GSON                                                         |
| Image Loading     | Glide                                                                     |
| Local Database    | Room (with Kotlin coroutine support)                                      |
| DI                | Hilt                                                                      |
| Testing           | JUnit, Espresso                                                           |


┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│    UI Layer    │ ← │  ViewModel     │ ← │  Repository    │
└────────────────┘   └────────────────┘   └────────────────┘
                                           ↑           ↑
                                           │           │
                                ┌───────────┘   ┌───────┴───────┐
                                │               │               │
                        ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
                        │  Remote Data   │ │  Local Cache   │ │  Other Data    │
                        │  (Retrofit)    │ │  (Room)        │ │  Sources       │
                        └────────────────┘ └────────────────┘ └────────────────┘

