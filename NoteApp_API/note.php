<?php

class Note {

    private $conn;
    private $table = "notes";

    public function __construct($db) {
        $this->conn = $db;
    }

    // READ ALL NOTES
    public function read() {

        $query = "SELECT * FROM " . $this->table .
                 " ORDER BY id DESC";

        $stmt = $this->conn->prepare($query);
        $stmt->execute();

        return $stmt;
    }

    // READ ONE NOTE
    public function readOne($id) {

        $query = "SELECT * FROM " . $this->table .
                 " WHERE id = :id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":id", $id);
        $stmt->execute();

        return $stmt;
    }

    // CREATE NOTE
    public function create($title, $content) {

        $query = "INSERT INTO " . $this->table .
                 " (title, content) VALUES (:title, :content)";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":title", $title);
        $stmt->bindParam(":content", $content);

        return $stmt->execute();
    }

    // UPDATE NOTE
    public function update($id, $title, $content) {

        $query = "UPDATE " . $this->table .
                 " SET title = :title, content = :content
                   WHERE id = :id";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":id", $id);
        $stmt->bindParam(":title", $title);
        $stmt->bindParam(":content", $content);

        return $stmt->execute();
    }

    // DELETE NOTE
    public function delete($id) {

        $query = "DELETE FROM " . $this->table .
                 " WHERE id = :id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":id", $id);

        return $stmt->execute();
    }
}