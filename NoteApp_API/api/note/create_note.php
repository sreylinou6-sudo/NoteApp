<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../note.php';

$database = new Database();
$db = $database->connect();

$note = new Note($db);

$data = json_decode(file_get_contents("php://input"));

if (!isset($data->title) || trim($data->title) === "") {

    http_response_code(400);

    echo json_encode([
        "message" => "Title is required"
    ]);

    exit;
}

$content = $data->content ?? "";

if ($note->create($data->title, $content)) {

    http_response_code(201);

    echo json_encode([
        "message" => "Note created successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Failed to create note"
    ]);
}