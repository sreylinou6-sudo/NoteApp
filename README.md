# Note App

A full-stack notes application with a native Android client and a PHP REST API backed by MySQL.

The Android app provides note and category management, live local filtering, a dedicated search screen, and create/edit/delete flows. Data is persisted in MySQL through JSON API requests rather than stored only on the device.

## Features

- Create, view, edit, and delete notes
- Create, edit, and delete categories
- Live filter notes by title or content
- Search notes from the dedicated search screen
- View note details and creation date
- Select a category while creating or editing a note
- Empty-state and network error feedback
- Confirmation dialog before deletion

## Project Structure

```text
NoteApp/
|-- NoteApp_Android/       # Android application
|   |-- app/src/main/java/com/example/note_app/
|   |-- app/src/main/res/
|   `-- build.gradle.kts
`-- NoteApp_API/           # PHP REST API
    |-- api/note/          # Note endpoints
    |-- api/categories/    # Category endpoints
    |-- database.php       # PDO database connection
    |-- note.php           # Note data operations
    |-- Categories.php     # Category data operations
    `-- note_db.sql        # Database and table definitions
```

## Technology

### Android

- Java
- Android SDK 36
- Minimum Android SDK 24
- Material Components
- Retrofit 2.9
- Gson converter
- OkHttp logging interceptor
- RecyclerView

### API

- PHP
- PDO
- MySQL / MariaDB
- JSON request and response bodies

## Data Model

### Notes

| Column | Type | Description |
|---|---|---|
| `id` | INT | Primary key |
| `category_id` | INT, nullable | Related category ID |
| `title` | VARCHAR(255) | Required note title |
| `content` | TEXT | Optional note content |
| `created_at` | TIMESTAMP | Record creation time |
| `updated_at` | TIMESTAMP | Last update time |

### Categories

| Column | Type | Description |
|---|---|---|
| `id` | INT | Primary key |
| `name` | VARCHAR(100) | Required category name |
| `description` | TEXT | Optional category description |
| `created_at` | TIMESTAMP | Record creation time |

Each note may reference one category. Deleting a category sets linked note category IDs to `NULL`.

## Prerequisites

- Android Studio
- Android SDK 24 or newer
- JDK 11 or newer
- XAMPP, WAMP, or another PHP/MySQL server
- MySQL or MariaDB
- PHP with the PDO MySQL extension enabled

## API Setup

1. Start Apache and MySQL.
2. Copy `NoteApp_API` into the server document root:

   ```text
   XAMPP: C:\xampp\htdocs\note-app-api\
   ```

   The project folder name must match the API path used by the Android app.

3. Import `NoteApp_API/note_db.sql` using phpMyAdmin or the MySQL command line.

   The script creates a database named `note_app`, creates `notes` and `categories`, and adds the note-category foreign key.

4. Confirm the database settings in `NoteApp_API/database.php`:

   ```php
   private $host = "localhost";
   private $db_name = "note_app";
   private $username = "root";
   private $password = "";
   ```

Update these values if MySQL uses different credentials.

## Android Setup

1. Open `NoteApp_Android` in Android Studio.
2. Allow Gradle to download the configured dependencies.
3. Select an emulator or physical Android device.
4. Edit `Endpoints.java` if the API location differs:

   ```java
   public static final String BASE_URL =
       "http://10.0.2.2/note-app-api/api/";
   ```

   - Android emulator: use `10.0.2.2` for the host computer.
   - Physical device: replace it with the computer's local IP address, for example `http://192.168.1.5/note-app-api/api/`.
   - Ensure the device and server computer are on the same network.

The manifest permits cleartext HTTP traffic because the local development API does not use HTTPS.

## API Reference

Base URL: `http://10.0.2.2/note-app-api/api/`

### Notes

| Method | Endpoint | Body / Parameters | Purpose |
|---|---|---|---|
| `GET` | `note/read_note.php` | Optional `id` query parameter | Read all notes or one note |
| `POST` | `note/create_note.php` | `{"title":"...","content":"..."}` | Create a note |
| `PUT` | `note/update_note.php` | `{"id":1,"title":"...","content":"..."}` | Update a note |
| `DELETE` | `note/delete_note.php` | `{"id":1}` | Delete a note |

### Categories

| Method | Endpoint | Body / Parameters | Purpose |
|---|---|---|---|
| `GET` | `categories/read_categories.php` | Optional `id` query parameter | Read all categories or one category |
| `POST` | `categories/create_categories.php` | `{"name":"...","description":"..."}` | Create a category |
| `PUT` | `categories/update_categories.php` | `{"id":1,"name":"...","description":"..."}` | Update a category |
| `DELETE` | `categories/delete_categories.php` | `{"id":1}` | Delete a category |

## Running the App

1. Import and start the MySQL database.
2. Start Apache.
3. Verify an API request in a browser or API client:

   ```text
   http://localhost/note-app-api/api/note/read_note.php
   ```

4. Run the Android app from Android Studio.
5. Create a category, then create or edit a note.

## Building and Testing

Run from `NoteApp_Android`:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat test
.\gradlew.bat connectedAndroidTest
```

The project currently contains basic JVM and Android instrumentation test placeholders.

## Current Implementation Notes

- The Android app fetches all notes and category names, then matches category names to `category_id` values in the client.
- Category CRUD is implemented end to end.
- The note API currently creates and updates title/content only. It does not yet write the selected `category_id` to MySQL.
- Live filtering happens on the device, while the dedicated search flow also uses the repository's local title/content search implementation.
- The API is intended for local development and does not include authentication or authorization.