<?php

declare(strict_types=1);

require 'coordinate_space_test.php';

$is_valid = require 'validate_request.php';

if (!$is_valid) {
    return;
}

$result = array(
    'hit' => Coordinate_Space_Test::isPointHitting($_GET["x"], $_GET["y"], $_GET["r"]),
    'timestamp' => date('Y-m-d H:i:s'),
    'values' => array(
        'x' => $x,
        'y' => $y,
        'r' => $r
    )
);

http_response_code(200); 
echo json_encode($result);

?>