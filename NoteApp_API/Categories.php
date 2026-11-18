<?php

class Category {

    private $conn;
    private $table = "categories";

    public function __construct($db) {
        $this->conn = $db;
    }

    // READ ALL CATEGORIES
    public function read() {

        $query = "SELECT * FROM " . $this->table .
                 " ORDER BY id DESC";

        $stmt = $this->conn->prepare($query);
        $stmt->execute();

        return $stmt;
    }

    // READ ONE CATEGORY
    public function readOne($id) {

        $query = "SELECT * FROM " . $this->table .
                 " WHERE id = :id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":id", $id);
        $stmt->execute();

        return $stmt;
    }

    // CREATE CATEGORY
    public function create($name, $description) {

        $query = "INSERT INTO " . $this->table .
                 " (name, description)
                   VALUES (:name, :description)";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":name", $name);
        $stmt->bindParam(":description", $description);

        return $stmt->execute();
    }

    // UPDATE CATEGORY
    public function update($id, $name, $description) {

        $query = "UPDATE " . $this->table .
                 " SET name = :name,
                       description = :description
                   WHERE id = :id";

        $stmt = $this->conn->prepare($query);

        $stmt->bindParam(":id", $id);
        $stmt->bindParam(":name", $name);
        $stmt->bindParam(":description", $description);

        return $stmt->execute();
    }

    // DELETE CATEGORY
    public function delete($id) {

        $query = "DELETE FROM " . $this->table .
                 " WHERE id = :id";

        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(":id", $id);

        return $stmt->execute();
    }
}