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
	
	
 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	$db->setAttribute(PDO::ATTR_ERRMODE, 
                            PDO::ERRMODE_EXCEPTION);
	
	$sql =<<<EOF
      INSERT INTO Users (Email,Password,Permission)
	  VALUES (:Email,:Pass,:Perm);
EOF;
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':Email',$Email);
	$stmt->bindParam(':Pass',$Pass);
	$stmt->bindParam(':Perm',$Perm);
	
	 $ret = $stmt->execute();
	 if (!$ret){
			$response = "Something went horribly wrong" + $db->lastErrorMsg();
	 }else{
			$response = "User successfully registered";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>

