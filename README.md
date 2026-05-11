# Java Project - Farm Management

Application Java de gestion agricole avec architecture en couches et persistance SQL Server via JDBC.

Le backend est structure pour etre utilise par une interface console aujourd'hui, puis par JavaFX ensuite.

## Architecture

```text
src/main/java/
  controller/   Facade appelee par l'UI console ou JavaFX
  services/     Validation et logique metier
  dao/          Requetes SQL Server
  database/     Configuration JDBC
  dto/          Objets de transfert
  exceptions/   Exceptions metier exploitables par l'UI
  model/        Entites metier
  util/         Session, validation, hash password
  Main.java     Menu console
```

Flux principal :

```text
UI -> Controller -> Service -> DAO -> SQL Server
```

## Dependances

Le projet utilise Maven.

- `mssql-jdbc` pour SQL Server
- `jbcrypt` pour le hash des mots de passe
- `javafx-controls` et `javafx-fxml` pour l'integration JavaFX
- `maven-compiler-plugin` avec `release 11`

## Configuration base de donnees

La connexion JDBC est centralisee dans :

```text
src/main/java/database/ConnectionDb.java
```

Valeurs par defaut :

```text
URL: jdbc:sqlserver://localhost:1433;databaseName=FermeAgricole;Encrypt=True;TrustServerCertificate=True;
User: sa
Password: sa
```

Variables d'environnement supportees :

```text
JAVA_PROJECT_DB_URL
JAVA_PROJECT_DB_USER
JAVA_PROJECT_DB_PASSWORD
```

## Build

Depuis un terminal ou Maven est disponible :

```bash
mvn clean compile
mvn clean package
```

Si Maven est lance depuis IntelliJ, verifier que `JAVA_HOME` pointe vers un JDK installe.

Exemple Windows :

```powershell
$env:JAVA_HOME='C:\Users\Microsoft\.jdks\openjdk-25.0.2'
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
mvn clean package
```

## Modules backend

### Authentification et session

- `AuthController.login(email, password)`
- `AuthController.logout()`
- `AuthController.getCurrentUser()`
- `AuthController.isAuthenticated()`
- `SessionManager.login(user)`
- `SessionManager.logout()`
- `SessionManager.getCurrentUser()`
- `SessionManager.isAuthenticated()`

Les mots de passe sont hashes avec BCrypt via `PasswordHasher`.

### Users

- create
- read
- update
- delete
- search exact par email : `searchUserByEmail`
- recherche dynamique par email : `searchUsersByEmail`
- gestion workers via role `OUVRIER`

### Farms

- create
- read
- update
- delete
- `searchFarmByName(String name)`
- `countAnimals(int farmId)`
- `countWorkers(int farmId)`
- `countTasks(int farmId)`
- `countStocks(int farmId)`
- `getFarmSummary(int farmId)`

### Animals

- add vache
- add poulet
- read all/by id/by farm
- update
- delete
- `updateHealthStatus`
- `searchAnimalByType(String type)`
- `getAnimalsByFarmId(int farmId)`

### Stocks

- create
- read all/by id/by farm
- update
- delete
- add quantity
- remove quantity
- check availability
- low stock check
- `searchStockByType(String type)`
- `getStocksByFarm(int farmId)`

### Tasks

- create
- read all/by id/by farm/by worker
- update
- delete
- assign to worker
- update status
- `searchTaskByStatus(TaskStatus status)`
- `getTasksByWorker(int workerId)`
- `getTasksByFarm(int farmId)`

### Cultiver

- create
- read all/by id/by farm
- update
- delete
- update status
- calculate harvest dates
- `getCultiversByFarm(int farmId)`

Les statuts Java sont convertis vers les valeurs acceptees par SQL Server.

## Exceptions

Les services lancent des exceptions metier heritant de `BusinessException` :

- `ValidationException`
- `AuthenticationException`
- `NotFoundException`

Une UI JavaFX peut intercepter `BusinessException` pour afficher un message utilisateur propre.

## Notes JavaFX

Les controllers exposent les methodes necessaires pour les ecrans JavaFX :

- recherche dynamique
- listes filtrees par ferme
- dashboard counters
- session utilisateur
- CRUD principaux

Le backend compile et package avec Maven avant integration JavaFX.
