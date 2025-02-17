# File Renamer

☕ Java 17 . 🛠️ Maven

## Contexte

Je débute en Java et ce projet me sert de support pour explorer le langage et ses bonnes pratiques, l'objectif est d'apprendre en mettant en pratique.

## Présentation

Il s'agit d'un script permettant de renommer des fichiers selon différentes stratégies (par date, format personnalisé, conversion de casse, etc.).  

L'objectif n'est pas seulement de créer un outil fonctionnel, mais aussi de structurer le code de manière évolutive et maintenable.

**☕ Java & Maven** : J'ai structuré ce projet avec Maven pour mieux gérer les dépendances et organiser mon code de manière modulaire.

**🏗️ Design Pattern** : J'ai implémenté le pattern Strategy pour encapsuler les différentes méthodes de renommage. Une Factory permet d'instancier dynamiquement la stratégie adaptée en fonction des besoins.

**🪴 Évolutivité** : Le projet est conçu pour être facilement extensible. Il est possible d'ajouter d'autres stratégies de renommage sans impacter le reste du code.

## Installation & Utilisation

### Cloner le projet

```sh
git clone git@github.com:Wagle-Studio/rename_picture_script.git
cd rename_picture_script
```

### Compiler et exécuter

```sh
mvn package && mvn exec:java -Dexec.mainClass="com.filemanager.App"
```