package com.example.note_app;

/** ONE place to edit: your server address and PHP file names. */
public class Endpoints {

    // Emulator -> XAMPP on your PC. Must end with "/".
    // Real phone: use your PC's IP, e.g. "http://192.168.1.5/note-app-api/api/"
    public static final String BASE_URL = "http://10.0.2.2/note-app-api/api/";

    // Notes  (read_note.php is confirmed from your Postman; check the others in htdocs)
    public static final String NOTE_READ   = "note/read_note.php";
    public static final String NOTE_CREATE = "note/create_note.php";
    public static final String NOTE_UPDATE = "note/update_note.php";
    public static final String NOTE_DELETE = "note/delete_note.php";

    // Categories (folder name "categories" is from your Postman; check the file names)
    public static final String CATEGORY_READ   = "categories/read_categories.php";
    public static final String CATEGORY_CREATE = "categories/create_categories.php";
    public static final String CATEGORY_UPDATE = "categories/update_categories.php";
    public static final String CATEGORY_DELETE = "categories/delete_categories.php";
}
