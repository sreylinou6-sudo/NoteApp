<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../note.php';

$database = new Database();
$db = $database->connect();

$note = new Note($db);

if (isset($_GET['id'])) {

    $stmt = $note->readOne($_GET['id']);

    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result) {
        echo json_encode($result);
    } else {
        http_response_code(404);
        echo json_encode(["message" => "Note not found"]);
    }

} else {

    $stmt = $note->read();

    $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($result);
}