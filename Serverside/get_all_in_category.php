<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();

if (isset($_POST["ProductCategory"])){
		$Cat = $_POST["ProductCategory"];
 
// connecting to db
$db = new PDO('sqlite:Test.db');
   
// read db
$sql =<<<EOF
      SELECT * from Products Where ProductCategory = :Cat ;
EOF;
	$stmt = $db->prepare($sql);
	$stmt->execute([':Cat'=>$Cat]);

// initialize response
$response = array();
$response["Success"] = 0;
$response["Products"] = array();


//construct response
   while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
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
   
   $response["Success"] = 1;
}else{
	$response["Message"] = "Missing at least 1 required field";
}
	//encode response to json
   echo json_encode($response);
   $db=null;
?>

