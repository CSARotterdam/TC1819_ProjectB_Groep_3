<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
$response["Success"] = 0;
if (isset($_POST["ProductID"])){
	$pid = $_POST["ProductID"];
	
	
	 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	   
	// read db
	$sql =<<<EOF
		  SELECT * from Products WHERE ProductID = :pid
EOF;
	
	$stmt = $db->prepare($sql);
	$stmt->execute([':pid'=>$pid]);


	//construct response
	   while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
			$response["ProductID"] = $row['ProductID'];
			$response["ProductManufacturer"] = $row['ProductManufacturer'];
			$response["ProductCategory"] = $row['ProductCategory'];
			$response["ProductName"] = $row['ProductName'];
			$response["ProductStock"] = $row['ProductStock']; 
			$response["ProductAmountBroken"] = $row['ProductAmountBroken'];
			$response["ProductAmountInProgress"] = $row['ProductAmountInProgress'];
	   }
	   $response["Success"] = 1;
	   
}else{
	
}
//encode response to json
	   echo json_encode($response);
	   $db=null;
?>