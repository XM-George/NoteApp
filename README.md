# 📝 NoteApp
A Java Swing note-taking application designed around a printable **A4 landscape page**, divided into two independent **A5 portrait sections**.

---

## 📑 Table of Contents
- [Features](#features)
- [Keyboard Shortcuts](#keyboard-shortcuts)
- [Page Layout](#page-layout)
- [File Format](#file-format)
- [Printing](#printing)
- [Tech Stack](#tech-stack)
- [Future Improvements](#future-improvements)
- [Installation](#installation)
- [Version History](#version-history)
- [Contributing](#contributing)
- [License](#license)

---

## ✨ Features <a name="features"></a>
- A4 landscape workspace
- Two independent A5 sections: left and right
- 1 to 5 text areas per A5 section
- Text overflow protection to keep content inside printable boundaries
- Click-to-position caret using automatic spaces and line breaks
- New, Open, Save, and Save As
- Custom `.noteapp` file format
- Unsaved-change detection and confirmation
- Print support
- Print Preview
- Undo and Redo
- Text search across all text areas
- Native operating-system file dialogs
- Keyboard shortcuts
- Simple and user-friendly interface

---

## ⌨️ Keyboard Shortcuts <a name="keyboard-shortcuts"></a>

| Action | Shortcut |
|---|---|
| New document | `Ctrl + N` |
| Open document | `Ctrl + O` |
| Save | `Ctrl + S` |
| Save As | `Ctrl + Shift + S` |
| Print | `Ctrl + P` |
| Print Preview | `Ctrl + Shift + P` |
| Undo | `Ctrl + Z` |
| Redo | `Ctrl + Y` |
| Find | `Ctrl + F` |

## 📄 Page Layout <a name="page-layout"></a>

The application displays one A4 sheet in landscape orientation:

```text
┌────────────────────────── A4 landscape ──────────────────────────┐
│                              │                                    │
│          Left A5             │             Right A5               │
│                              │                                    │
│      1–5 text areas          │          1–5 text areas            │
│                              │                                    │
└──────────────────────────────┴────────────────────────────────────┘
```

Each A5 section can be configured independently from the **Layout** menu.

Examples:

- Left: 1 text area, Right: 1 text area
- Left: 2 text areas, Right: 4 text areas
- Left: 5 text areas, Right: 3 text areas

## 📁 File Format <a name="file-format"></a>

Documents are stored locally using the `.noteapp` extension.

Each file stores:

- file-format version
- number of left-side text areas
- number of right-side text areas
- text content for every area

Text content is UTF-8 encoded and stored using Base64 so that Greek characters, line breaks, tabs, and special characters can be preserved safely.

## 🖨️ Printing <a name="printing"></a>

The application prints only the A4 page component—not the menus, window frame, scroll container, or workspace background.

Printing uses:

- A4 paper
- landscape orientation
- automatic scaling
- centered page output

PDF output is also possible by selecting a PDF printer from the operating system print dialog, such as **Microsoft Print to PDF** on Windows.

---

## 🛠️ Tech Stack <a name="tech-stack"></a>
- **Language:** Java
- **GUI:** Java Swing
- **Tools:** IntelliJ IDEA, Git, GitHub

---

## 🔧 Future Improvements <a name="future-improvements"></a>
- Adjustable font size and font family
- Bold, italic, and underline formatting
- Zoom controls
- Recent files
- Autosave and recovery
- Reusable page templates
- Improved keyboard navigation

---

## 💾 Installation <a name="installation"></a>

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

src/main/Main.java

```

and run the Main class directly.

#### Using the terminal:

```bash

cd src
javac main/Main.java
java main.Main

```

---

## 🏷️ Version History <a name="version-history"></a>

### v2.0

- Redesigned the editor around an A4 landscape page
- Added independent left and right A5 sections
- Added configurable 1–5 text areas per section
- Added text-boundary protection
- Added click-to-position editing
- Added native Save As and Open dialogs
- Added custom document persistence
- Added unsaved-change detection
- Added Print Preview
- Added Undo and Redo
- Added text search

### v1.0

The original version of NoteApp, featuring the initial note editor, local file handling, and printing functionality.

The original version remains available through the repository's `v1` branch and `v1.0` tag.


---

## 🤝 Contributing <a name="contributing"></a>
Contributions are welcome. Feel free to fork the repository and submit a pull request.

---

## 📜 License <a name="license"></a>
This project is licensed under the MIT License. See the LICENSE file for details.

