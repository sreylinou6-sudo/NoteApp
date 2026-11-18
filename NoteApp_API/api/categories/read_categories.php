<?php

header("Content-Type: application/json");

require_once __DIR__ . '/../../database.php';
require_once __DIR__ . '/../../Categories.php';

$database = new Database();
$db = $database->connect();

$category = new Category($db);

if (isset($_GET['id'])) {

    $stmt = $category->readOne($_GET['id']);
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result) {
        echo json_encode($result);
    } else {
        http_response_code(404);
        echo json_encode(["message" => "Category not found"]);
    }

} else {

    $stmt = $category->read();

    echo json_encode(
        $stmt->fetchAll(PDO::FETCH_ASSOC)
    );
}