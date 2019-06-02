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
      SELECT * from Products;
EOF;

// initialize response
$response = array();
$response["Success"] = 0;
$response["Products"] = array();


//construct response
$ret = $db->query($sql);
   while($row = $ret->fetchArray(SQLITE3_ASSOC) ) {
	    $product = array();
		$product["ProductID"] = $row['ProductID'];
		$product["ProductManufacturer"] = $row['ProductManufacturer'];
		$product["ProductCategory"] = $row['ProductCategory'];
		$product["ProductName"] = $row['ProductName'];
		$product["ProductStock"] = $row['ProductStock']; 
		$product["ProductAmountBroken"] = $row['ProductAmountBroken'];
		$product["ProductAmountInProgress"] = $row['ProductAmountInProgress'];
		array_push($response["Products"],$product);
   }
   //encode response to json
   $response["Success"] = 1;
   echo json_encode($response);
   $db->close();
?>

