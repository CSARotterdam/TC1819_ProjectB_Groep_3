<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["ProductID"])&& isset($_POST["ProductManufacturer"]) && isset($_POST["ProductCategory"]) &&
isset($_POST["ProductName"])&& isset($_POST["ProductStock"])){
	
	$ID = $_POST["ProductID"];
	$Manufacturer = $_POST["ProductManufacturer"];
	$Category = $_POST["ProductCategory"];
	$Name = $_POST["ProductName"];
	$Stock = $_POST["ProductStock"];
	$AmountBroken = 0;
	
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
 
	// connecting to db
	$db = new DB_CONNECT();
	
	$sql =<<<EOF
      INSERT INTO Products (ProductID,ProductManufacturer,ProductCategory,ProductName,ProductStock,ProductAmountBroken)
	  VALUES ('$ID','$Manufacturer','$Category','$Name','$Stock','$AmountBroken');
EOF;
	 $ret = $db->exec($sql);
	 if (!$ret){
			$response = "Something went horribly wrong" + $db->lastErrorMsg();
	 }else{
			$response = "Product successfully created";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
?>

