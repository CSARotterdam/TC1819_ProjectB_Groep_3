<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
   
// read db
$sql =<<<EOF
      SELECT DISTINCT ProductCategory from Products;
EOF;

// initialize response
$response["Success"] = 0;
$response["Categories"] = array();


//construct response
$ret = $db->query($sql);
   while($row = $ret->fetchArray(SQLITE3_ASSOC) ) {
		$Category = $row['ProductCategory'];
		array_push($response["Categories"],$Category);
   }
   //encode response to json
   $response["Success"] = 1;
   echo json_encode($response);
   $db->close();
?>

