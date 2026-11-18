<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../note.php';

$database = new Database();
$db = $database->connect();

$note = new Note($db);

$data = json_decode(file_get_contents("php://input"));

if (!isset($data->id)) {

    http_response_code(400);

    echo json_encode([
        "message" => "ID is required"
    ]);

    exit;
}

if ($note->readOne($data->id)->rowCount() === 0) {

    http_response_code(404);

    echo json_encode([
        "message" => "Note not found"
    ]);

    exit;
}

if ($note->delete($data->id)) {

    echo json_encode([
        "message" => "Note deleted successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Delete failed"
    ]);
}