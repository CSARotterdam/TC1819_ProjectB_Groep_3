<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
$response["Success"] = 0;
if (isset($_POST["ProductID"])){
	$pid = $_POST["ProductID"];
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
	 
	// connecting to db
	$db = new DB_CONNECT();
	   
	// read db
	$sql =<<<EOF
		  SELECT * from Products WHERE ProductID = "$pid"
EOF;

	// initialize response
	$response = array();
	


	//construct response
	$ret = $db->query($sql);
	   while($row = $ret->fetchArray(SQLITE3_ASSOC) ) {
			$response["ProductID"] = $row['ProductID'];
			$response["ProductManufacturer"] = $row['ProductManufacturer'];
			$response["ProductCategory"] = $row['ProductCategory'];
			$response["ProductName"] = $row['ProductName'];
			$response["ProductStock"] = $row['ProductStock']; 
			$response["ProductAmountBroken"] = $row['ProductAmountBroken']; 
	   }
	   $response["Success"] = 1;
	   
}else{
	
}
//encode response to json
	   echo json_encode($response);
	   $db->close();
?>