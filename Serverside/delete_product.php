<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["ProductID"])){
	
	$ID = $_POST["ProductID"];
	
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
 
	// connecting to db
	$db = new DB_CONNECT();
	
	$sql =<<<EOF
      DELETE from Products WHERE ProductID = '$ID'
	
EOF;
	 $ret = $db->exec($sql);
	 if (!$ret){
			$response = $db->lastErrorMsg();
	 }else{
			$response = "Product successfully deleted";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
?>