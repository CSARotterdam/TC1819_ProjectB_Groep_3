<?php
/*
 * Following code will list all the E-mail adresses.
 */
 
// array for JSON response
$response = array();
 
 
// connecting to db
$db = new PDO('sqlite:Test.db');
   
// read db
$sql =<<<EOF
      SELECT DISTINCT Email from Users;
EOF;

// initialize response
$response = array();
$response["Success"] = 0;
$response["Email"] = array();


//construct response
$ret = $db->query($sql);
   foreach($ret as $row){
	    $Email = $row["Email"];
		array_push($response["Email"],$Email);
   }
   //encode response to json
   $response["Success"] = 1;
   echo json_encode($response);
   $db=null;
?>

