<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
$response["Success"] = 0;
if (isset($_POST["Email"]) && isset($_POST["Password"])){
	$Email = $_POST["Email"];
	$Pass = $_POST["Password"];
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
	 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	   
	// read db
	$sql =<<<EOF
		  SELECT * from Users WHERE Email = :Email
EOF;
	$stmt = $db->prepare($sql);
	$stmt->execute([':Email' => $Email]);

	
	//construct response
	   while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
			if(password_verify($Pass,$row['Password'])){
				$response["Success"] = 1;
				$response["Email"] = $row['Email'];
				$response["Password"] = $row['Password'];
				$response["Permission"] = $row['Permission'];
		   }
	   }
	   
	   
}else{
	$response["Message"] = "Missing at least 1 required field";
}
//encode response to json
	   echo json_encode($response);
	   $db->close();
?>