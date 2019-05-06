<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["Email"])&& isset($_POST["Password"]) && isset($_POST["Permission"])){
	
	$Email = $_POST["Email"];
	$Pass = password_hash($_POST["Password"],PASSWORD_DEFAULT);
	$Perm = $_POST["Permission"];
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
 
	// connecting to db
	$db = new DB_CONNECT();
	
	$sql =<<<EOF
      INSERT INTO Users (Email,Password,Permission)
	  VALUES ('$Email','$Pass','$Perm');
EOF;
	 $ret = $db->exec($sql);
	 if (!$ret){
			$response = "Something went horribly wrong" + $db->lastErrorMsg();
	 }else{
			$response = "User successfully registered";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
?>

