# Android Chat Exercise

## Overview
This Android application implements a basic chat interface using **Clean Architecture** principles to promote code maintainability, testability, and reusability.

## Key Features
- **Clean Architecture**: The application is structured into distinct layers: **Data**, **Domain**, and **Presentation**, adhering to Clean Architecture principles.
- **Dependency Injection**: **Koin** is used for dependency injection, enhancing testability and code organization.
- **Coroutine-based Asynchronous Operations**: **Coroutines** are employed for asynchronous tasks, improving responsiveness and code readability.
- **UI Implementation**: The UI layer utilizes **Jetpack Compose** for building the chat interface, providing a modern and declarative approach.
- **Message Display**: The chat displays messages in a list, differentiating between sent and received messages.
- **Message Input**: Users can input and send messages, which are added to the chat list.
- **Message Formatting**: Messages are formatted with appropriate styles and layouts as requested in technical documentation.
- **Changing Users**: It is possible to add new users and change active one to chat with different people and check how chat looks like for different side of conversation.

## Architecture Breakdown

### Data Layer
- Handles data access and persistence.
- Provides data models and repositories.
- Allow to map entities between different layers

### Domain Layer
- Contains business logic and repositories.
- Does not depend on any other modules.

### Presentation Layer
- Manages the UI and user interactions.
- Displays data from the Domain layer.

## Additional Considerations
- **Testing**: Unit tests are included to ensure code quality and reliability.
- **Best Practices**: Adherence to Android development best practices, including code style, performance optimization, and accessibility guidelines.

## Future Improvements
- **UI Enhancements**: Additional features like message reactions, attachments.

By following Clean Architecture principles and leveraging modern Android development tools, this project provides a solid foundation for building more complex chat applications.
