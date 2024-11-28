<?php

function check_int($str): bool {
    if (!is_numeric($str)) {
        return false;
    }

    return intval($str) == $str;
}

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(400); //Bad request
    echo 'Wrong method, use "GET"';
    return false;
}

if (!isset($_GET['x']) || !isset($_GET['y']) || !isset($_GET['r'])) {
    echo 'Request must contain "x", "y", "r" paramaters';
    http_response_code(400); //Bad request
    return false;
}

if (!check_int($_GET['x'])) {
    echo 'Parameter "x" must be an integer';
    http_response_code(400); //Bad request
    return false;
}

if (!is_numeric($_GET['y'])) {
    echo 'Parameter "y" must a number';
    http_response_code(400); //Bad request
    return false;
}

if (!check_int($_GET['r'])) {
    echo 'Parameter "r" must be an integer';
    http_response_code(400); //Bad request
    return false;
}

if ($_GET['x'] < -3 && $_GET['x'] > 5) {
    echo 'Parameter "x" must be between [-3, 5]';
    http_response_code(400); //Bad request
    return false;
}

if ($_GET['y'] < -5 && $_GET['y'] > 3) {
    echo 'Parameter "y" must be between [-5, 3]';
    http_response_code(400); //Bad request
    return false;
}

if ($_GET['r'] < -3 && $_GET['r'] > 5) {
    echo 'Parameter "r" must be between [1, 5]';
    http_response_code(400); //Bad request
    return false;
}

return true;

?>