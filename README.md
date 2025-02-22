# File Renamer

☕ Java 17 . 🛠️ Maven . 🎨 JavaFX

![Git de démonstration](./demo.gif) 

## Contexte

Je débute en Java et ce projet me sert de support pour explorer le langage et ses bonnes pratiques. L'objectif est d'apprendre en mettant en pratique avec une approche structurée et modulaire.

## Présentation

**File Renamer** est une application permettant de renommer des fichiers selon différentes stratégies :  

🔹 **Par date** (date de prise de vue, modification...)  
🔹 **Format personnalisé** (préfixe, suffixe, remplacement...)  - à venir  
🔹 **Conversion de casse** (majuscules, minuscules, camelCase...)  - à venir

L'objectif n'est pas seulement de proposer un outil fonctionnel, mais aussi d'adopter une **architecture propre et évolutive**.

---

### Architecture & conception

🔹 **MVCI avec JavaFX** : Organisation **Model - View - Controller - Interactor** pour la séparation des responsabilités.  
🔹 **Pattern Strategy** : Encapsulation des différentes méthodes de renommage pour une évolutivité optimale.  
🔹 **Factory Pattern** : Instanciation dynamique des stratégies adaptées en fonction du besoin.  
🔹 **Injection de dépendances** : Utilisation d'un Injector maison pour faciliter la gestion des composants.  
🔹 **Observable & Binding** : Mise à jour dynamique de l'UI via JavaFX et liaison des données.

---

## Installation

```sh
git@github.com:Wagle-Studio/file_renamer.git
cd file_renamer
mvn clean install
mvn clean package
mvn exec:java -Dexec.mainClass="com.filemanager.App"
```

```sh
mvn clean javafx:run -e # Launch file renamer.
```