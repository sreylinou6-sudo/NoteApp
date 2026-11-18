<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../Categories.php';

$database = new Database();
$db = $database->connect();

$category = new Category($db);

$data = json_decode(file_get_contents("php://input"));

if (!isset($data->name) || trim($data->name) === "") {

    http_response_code(400);

    echo json_encode([
        "message" => "Category name is required"
    ]);

    exit;
}

$description = $data->description ?? "";

if ($category->create($data->name, $description)) {

    http_response_code(201);

    echo json_encode([
        "message" => "Category created successfully"
    ]);

} else {

    http_response_code(500);

    echo json_encode([
        "message" => "Failed to create category"
    ]);
}