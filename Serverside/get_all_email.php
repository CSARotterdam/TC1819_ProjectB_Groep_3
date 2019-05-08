<?php
/*
 * Following code will list all the E-mail adresses.
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
   
// read db
$sql =<<<EOF
      SELECT * from Users;
EOF;

// initialize response
$response = array();
$response["Success"] = 0;
$response["Email"] = array();


//construct response
$ret = $db->query($sql);
   while($row = $ret->fetchArray(SQLITE3_ASSOC) ) {
	    $Email = $row["Email"];
		array_push($response["Email"],$Email);
   }
   //encode response to json
   $response["Success"] = 1;
   echo json_encode($response);
   $db->close();
?>

