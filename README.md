# Github User Profile

An Android application for displaying GitHub user information, built with a strong emphasis on **clean architecture**, **scalability**, and **maintainability**.

## Project Overview

This project follows **Clean Architecture** principles to ensure a clear separation of concerns across layers. The presentation layer adopts the **MVVM** pattern to create a reactive, testable, and lifecycle-aware UI. The user interface is implemented using **XML**.

## Architecture

The application is structured using **Clean Architecture**, consisting of:

### Presentation Layer
Handles UI logic using **MVVM**, ViewModel, and observable state management.

### Domain Layer
Contains business logic, use cases, and core models, independent of frameworks and data sources.

### Data Layer
Responsible for data operations, including API calls and data mapping between remote and domain models.

## Tech Stack

- **Language**: Kotlin  
- **UI**: XML  
- **Architecture**: Clean Architecture, MVVM  
- **Dependency Injection**: Koin  
- **Networking**: Retrofit  
- **Asynchronous**: Kotlin Coroutines & Flow  
- **CI/CD**: CircleCI  

## Continuous Integration

This project is integrated with **CircleCI** to ensure automated builds and maintain code quality.

[![CircleCI](https://circleci.com/gh/arifaizin/MySimpleCleanArchitecture.svg?style=svg)](https://circleci.com/gh/ramadaaditya/GithubUser)
