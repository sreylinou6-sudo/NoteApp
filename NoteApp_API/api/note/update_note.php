<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../note.php';

$database = new Database();
$db = $database->connect();

$note = new Note($db);

$data = json_decode(file_get_contents("php://input"));

if (
    !isset($data->id) ||
    !isset($data->title) ||
    trim($data->title) === ""
) {
    http_response_code(400);

    echo json_encode([
        "message" => "ID and title are required"
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

$content = $data->content ?? "";

if ($note->update($data->id, $data->title, $content)) {

    echo json_encode([
        "message" => "Note updated successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Update failed"
    ]);
}