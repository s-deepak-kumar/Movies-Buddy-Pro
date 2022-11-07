<?php
// Replace the below values
$host = 'svc-66ddf087-c788-4d73-8264-c935cb89e05f-dml.aws-mumbai-1.svc.singlestore.com';
$username = 'admin';
$password = 'Z(}I*LBqHG.Jy-b|U?dj';
$db = 'MoviesBuddyPro';

// Create connection
$conn = mysqli_connect($host, $username, $password, $db);
//define("conn", $conn);
mysqli_query($conn, "SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION'");
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
