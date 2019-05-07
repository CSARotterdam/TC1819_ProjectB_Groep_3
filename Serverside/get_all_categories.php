<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();

 
// connecting to db
$db = new PDO('sqlite:Test.db');
$db -> setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
   
// read db
$sql =<<<EOF
      SELECT DISTINCT ProductCategory from Products;
EOF;

// initialize response
$response["Success"] = 0;
$response["Categories"] = array();


//construct response
$ret = $db->query($sql);
   foreach($ret as $row) {
		$Category = $row['ProductCategory'];
		array_push($response["Categories"],$Category);
   }
   //encode response to json
   $response["Success"] = 1;
   echo json_encode($response);
   $db->close();
?>

