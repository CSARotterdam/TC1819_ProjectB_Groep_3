<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["ProductID"])){
	
	$ID = $_POST["ProductID"];
	
 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	$db -> setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
	
	$sql =<<<EOF
      DELETE from Products WHERE ProductID = :id;
EOF;
	$stmt = $db->prepare($sql);
	$stmt->bindValue(':id',$ID);
	
	$ret = $stmt->execute();
	if (!$ret){
			$response = $db->lastErrorMsg();
	}else{
			$response = "Product successfully deleted";
			$db = null;
	}
}else{
	$response = "Missing at least 1 required field";
}
echo ($response);
db=null;
?>