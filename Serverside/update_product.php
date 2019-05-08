<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["ProductID"]) && isset($_POST["ProductManufacturer"]) && isset($_POST["ProductCategory"]) &&
isset($_POST["ProductName"])&& isset($_POST["ProductStock"]) && isset($_POST["ProductAmountBroken"])){
	
	$ID = $_POST["ProductID"];
	$Manufacturer = $_POST["ProductManufacturer"];
	$Category = $_POST["ProductCategory"];
	$Name = $_POST["ProductName"];
	$Stock = $_POST["ProductStock"];
	$AmountBroken = $_POST["ProductAmountBroken"];
 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	
	
	$sql =<<<EOF
      UPDATE Products 
	  SET 	ProductManufacturer = :Manufacturer,
			ProductCategory = :Category,
			ProductName = :Name,
			ProductStock = :Stock,
			ProductAmountBroken = :AmountBroken
	  WHERE ProductID = :ID
EOF;
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':Manufacturer',$Manufacturer);
	$stmt->bindParam(':Category',$Category);
	$stmt->bindParam(':Name',$Name);
	$stmt->bindParam(':Stock',$Stock);
	$stmt->bindParam(':AmountBroken',$AmountBroken);
	$stmt->bindParam(':ID',$ID);
	
	 $ret = $stmt->execute();
	 if (!$ret){
			$response = $db->lastErrorMsg();
	 }else{
			$response = "Product successfully updated";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>