# Java Project - Farm Management

## Description

Ce projet est une application Java console pour la gestion simple d'une ferme.
Il utilise JDBC pour communiquer avec une base de donnees SQL Server.

L'application permet actuellement de gerer les elements suivants :

- authentification d'un utilisateur
- creation d'un nouvel utilisateur
- creation d'une ferme
- affichage des fermes
- recherche d'une ferme par id
- modification d'une ferme
- suppression d'une ferme
- ajout d'un poulet dans une ferme existante
- ajout d'une vache dans une ferme existante

Le projet est organise avec une architecture en couches afin de separer les responsabilites.

## Architecture

```text
src/
  controller/   Couche controleur, encore en preparation
  dao/          Acces aux donnees SQL Server
  database/     Configuration de la connexion JDBC
  dto/          Objets de transfert de donnees
  model/        Modeles metier
  service/      Logique metier
  util/         Classes utilitaires
  Main.java     Menu console principal
  Main2.java    Point d'entree secondaire pour tests ponctuels
```

## Packages principaux

### model

Contient les classes metier :

- `User`
- `Farm`
- `Animal`
- `Vache`
- `Poulet`
- `Admin`
- `Ouvrier`
- `Stock`
- `Task`
- `Cultiver`

### dao

Contient les classes responsables des requetes SQL :

- `UserDAO`
- `FarmDAO`
- `AnimalDAO`
- `StockDAO`
- `TaskDAO`

### service

Contient la logique applicative :

- `AuthService`
- `UserService`
- `FarmService`
- `AnimalService`
- `ProductionService`
- `TaskAssignmentService`

### dto

Contient les objets utilises pour transporter des donnees sans exposer directement les modeles :

- `LoginRequestDTO`
- `UserResponseDTO`

## Base de donnees

Le projet utilise SQL Server avec le driver JDBC Microsoft :

```text
libs/mssql-jdbc-13.4.0.jre11.jar
```

La connexion est centralisee dans :

```text
src/database/ConnectionDb.java
```

Par defaut, la configuration locale est :

```text
URL: jdbc:sqlserver://localhost:1433;databaseName=java_project;Encrypt=True;TrustServerCertificate=True;
User: sa
Password: sa
```

Il est possible de remplacer ces valeurs avec des variables d'environnement :

```text
JAVA_PROJECT_DB_URL
JAVA_PROJECT_DB_USER
JAVA_PROJECT_DB_PASSWORD
```

## Menu console

Le fichier `Main.java` lance un menu interactif :

```text
1. Login
2. Add user
3. Add farm
4. Show farms
5. Add poulet
6. Add vache
7. Find farm by id
8. Update farm
9. Delete farm
0. Exit
```

Le menu valide les entrees principales :

- champs texte obligatoires
- nombres valides
- valeurs non negatives pour l'age et la production
- verification que la ferme existe avant d'ajouter un animal

## Etat actuel

Le projet compile avec OpenJDK 25.

Les fonctionnalites principales deja implementees sont :

- connexion SQL Server
- login utilisateur
- ajout utilisateur
- ajout ferme
- affichage des fermes
- recherche d'une ferme par id
- modification d'une ferme
- suppression d'une ferme
- ajout vache
- ajout poulet

## Points a ameliorer

Les prochaines ameliorations recommandees sont :

- hasher les mots de passe au lieu de les stocker en clair
- remplacer les `printStackTrace()` par une gestion d'erreurs plus propre
- completer les classes `controller`
- ajouter des methodes de recherche, modification et suppression pour les animaux
- ajouter des tests
- migrer vers Maven ou Gradle pour gerer les dependances plus proprement

## Execution

Le projet peut etre lance depuis IntelliJ IDEA avec le JDK configure.

Depuis un terminal, il faut compiler avec le driver SQL Server dans le classpath, puis lancer `Main`.
