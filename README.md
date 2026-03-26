# 📝 NoteApp
A simple note-taking application that allows users to create, edit, delete, and manage notes efficiently.  
The project focuses on file handling, data persistence, and clean application structure.

---

## 📑 Table of Contents
- [Features](#-features)
- [Tech Stack](#tech-stack)
- [Usage](#-usage)
- [The Process](#-the-process)
- [Future Improvements](#-future-improvements)
- [Code Highlights](#-code-highlights)
- [Installation](#-installation)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features
- Create new notes
- Edit existing notes
- Load / Save notes locally
- Delete notes
- View saved notes
- Print notes using a printer
- Simple and user-friendly interface

---

## 🛠️ Tech Stack <a name="tech-stack"></a>
- **Language:** Java
- **GUI:** Java Swing
- **Tools:** IntelliJ IDEA, Git, GitHub

---

## 🚀 Usage
A simple note management application.

- Create a note by entering text and saving it
- Load existing notes to view or edit them
- Delete notes when no longer needed
- Print your notes to have a physical copy
- Notes are saved locally

---

## 📍 The Process
This project was created to improve my understanding of Java, file handling, and application design. It was also one of my first projects built out of personal necessity, as I could not find a note-taking app that suited my needs.

I started by designing the GUI of the application. The interface was fairly simple, consisting of a frame, text areas, and a menu bar at the top. I then implemented the ability to split the page into 1–5 equally sized text areas so that multiple unrelated notes could be stored on the same page.

Next, I worked on saving and loading files. I had never done file handling in Java before, so I had to research and learn new concepts along the way. After some experimentation, I successfully implemented persistent storage.

Once the core logic was complete, I focused on improving usability by adding additional user and text formatting options. This marked the completion of the first working version of the application.

After that, I decided to implement a print feature so users could print their notes if needed. This was my first time working with printing in Java, and it introduced many challenges and bugs. However, after several hours of debugging and learning, I managed to fully implement it.

Finally, I tested the application extensively, fixed issues related to file reading/writing, and ensured that the printing functionality worked reliably. I then refined the application to improve overall usability and provide a smoother user experience.

**Key learnings:**
- File handling in Java
- Structuring a multi-class application
- Working with user input and event handling
- Printing GUI components to a physical printer in Java
- Debugging and improving application stability

---

## 🔧 Future Improvements
- Add search functionality for notes
- Improve UI/UX design
- Add categories or tags for notes
- Implement a feature to split pages into two sections (similar to a book layout) for better note organization

---

## 💡 Code Highlights
- Printing GUI components to a physical printer
- Modular structure separating logic and UI
- Clean and reliable file I/O operations

---

## 💾 Installation

Follow these steps to clone and run the project locally. Works on **Linux, macOS, and Windows** (via Git Bash or WSL).

### 1. Clone the repository
Make sure you have git installed. Open your terminal and run:

```bash

git clone https://github.com/XM-George/NoteApp.git
cd NoteApp

```

### 2. Open the project
You can open the project in:
- IntelliJ IDEA (recommended)
- Eclipse
- Any Java IDE

Make sure the `src` folder is marked as the source root if required

### 3. Run the application

#### Using an IDE:
Navigate to:

```bash

src/Main.java

```

and run the Main class directly.

#### Using the terminal:

```bash

cd src
javac Main.java
java Main

```

---

## 🤝 Contributing
Contributions are welcome. Feel free to fork the repository and submit a pull request.

---

## 📜 License
This project is licensed under the MIT License. See the LICENSE file for details.

