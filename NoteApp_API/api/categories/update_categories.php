<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../Categories.php';

$database = new Database();
$db = $database->connect();

$category = new Category($db);

$data = json_decode(file_get_contents("php://input"));

if (
    !isset($data->id) ||
    !isset($data->name) ||
    trim($data->name) === ""
) {
    http_response_code(400);

    echo json_encode([
        "message" => "ID and name are required"
    ]);

    exit;
}

if ($category->readOne($data->id)->rowCount() === 0) {

    http_response_code(404);

    echo json_encode([
        "message" => "Category not found"
    ]);

    exit;
}

$description = $data->description ?? "";

if ($category->update($data->id, $data->name, $description)) {

    echo json_encode([
        "message" => "Category updated successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Category update failed"
    ]);
}