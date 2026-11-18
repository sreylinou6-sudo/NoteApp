<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../Categories.php';

$database = new Database();
$db = $database->connect();

$category = new Category($db);

$data = json_decode(file_get_contents("php://input"));

if (!isset($data->id)) {

    http_response_code(400);

    echo json_encode([
        "message" => "Category ID is required"
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

if ($category->delete($data->id)) {

    echo json_encode([
        "message" => "Category deleted successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Category delete failed"
    ]);
}