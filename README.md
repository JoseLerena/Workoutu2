# Workoutu2 🧮⚡
koutu2** is an engaging and interactive Android math training game built with **Kotlin** and **Jetpack Compose**. Test your arithmetic skills against the clock with a unique retro/hacker neon aesthetic!

---

## 🎮 Features

**Wor
- **Arithmetic Training**: Generates 10 rounds of random arithmetic operations (Addition `+`, Subtraction `-`, Multiplication `*`, Division `/`).
- **Smart Operations**: Operands are kept under 12. For divisions, zero-division is strictly prevented and results are guaranteed to be exact integers (no decimals).
- **Retro / Hacker Neon Aesthetic**:
  - Pitch black background (`#000000`).
  - High-contrast neon green typography and button borders.
  - Dynamic neon color shift when pressing keypad buttons.
  - Red styled delete button (`◁`).
- **Custom Numeric Keypad**: Built from scratch using Compose grids for a precise, arcade-like feel.
- **Session Summary**: After completing 10 rounds, view your score (correct/incorrect answers) and total elapsed time formatted in `mm:ss`.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 foundations.
- **Architecture**: MVVM (Model-View-ViewModel) utilizing `ViewModel` and Kotlin `StateFlow` for robust reactive state management.
- **Minimum SDK**: Android 13 (API Level 33).
- **Build System**: Kotlin DSL (`build.gradle.kts`).

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Ladybug or newer recommended).
- Android SDK 33 or higher.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Workoutu2.git
   ```
2. Open Android Studio and select **Open**.
3. Choose the cloned `Workoutu2` folder.
4. Let Gradle sync dependencies, then run the app on an emulator or physical device (API 33+).

---

## 📂 Project Structure

```text
app/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── app/
│                   └── workoutu2/
│                       ├── game/          # Game screen, ViewModel, and state
│                       └── ui/            # Theme, typography, and styling
└── build.gradle.kts
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check issues page.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
