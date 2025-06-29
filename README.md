# Galaxy Trucker

### Team

* [Maggipinto Luigi](https://github.com/gigimaggi03)
* [Mandelli Federico](https://github.com/fedmand)
* [Marrone Danilo](https://github.com/Danilo2307)
* [Occhipinti Alberto](https://github.com/AlbertoOcchipinti)

---

### Functionalities

| Functionality                | Done |
| ---------------------------- | ---- |
| Simplified rules             | ✅    |
| Full rules                   | ✅    |
| Socket                       | ✅    |
| RMI                          | ✅    |
| TUI                          | ✅    |
| GUI                          | ✅    |
| Test flight                  | ✅    |
| Multiple games               | ✅    |
| Persistence                  | ❌    |
| Resilience to disconnections | ❌    |

---

### Documentation

All project documentation can be found in the `/deliverables` directory of this repository.

#### UML

* In `/deliverables/final/uml`, you will find the **UML Class Diagram** for the project as well as the **UML Sequence Diagrams** for network-related scenarios.
* The class diagram contains all model classes and the most relevant parts of the controller.
* Sequence diagrams describe key usage situations such as joining a game, drawing a component, or docking a component.

#### JavaDoc

The most important classes and methods are documented with JavaDoc.
The generated JavaDoc can be found in `/images/javadoc`.

#### Used Tools

| Library/Plugin | Description                          |
| -------------- | ------------------------------------ |
| Maven          | Project management, Java build tool  |
| JavaFX         | Graphics library for GUI development |
| JUnit          | Java unit testing framework          |

---

### How to Run

* **Make sure to download the JAR files with git LFS or directly from GitHub.**
* Open a terminal in the `/deliverables/jar/` folder.
* To run the server, execute:

  ```bash
  java -jar MainServer.jar
  ```
* To run the client, execute:

  ```bash
  java -jar MainClient.jar
  ```

---

### DISCLAIMER

**Galaxy Trucker** is a board game developed and published by Cranio Creations Srl.

All graphic content in this project associated with the official board game has been used with the approval of Cranio Creations Srl and exclusively for educational purposes.

Distribution, copying, or reproduction of these contents and images outside of this project is strictly forbidden, as is publishing this content for any other purpose.

Commercial use of the aforementioned content is also strictly prohibited.
