<?php
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response;
   
//check for required fields
if (isset($_POST["ProductID"])&& isset($_POST["ProductManufacturer"]) && isset($_POST["ProductCategory"]) &&
isset($_POST["ProductName"])&& isset($_POST["ProductStock"])){

	$Image = "";
	if (isset($_POST["ProductImage"])){
		$Image = $_POST["ProductImage"];
	}
	$ID = $_POST["ProductID"];
	$Manufacturer = $_POST["ProductManufacturer"];
	$Category = $_POST["ProductCategory"];
	$Name = $_POST["ProductName"];
	$Stock = $_POST["ProductStock"];
	$AmountBroken = 0;
	$AmountInProgress = 0;
	

	// connecting to db
	$db = new PDO('sqlite:Test.db');
	
	$sql =<<<EOF
      INSERT INTO Products (ProductID,ProductManufacturer,ProductCategory,ProductName,ProductStock,ProductAmountBroken,ProductAmountInProgress,ProductImage)
	  VALUES (:ProductID,:ProductManufacturer,:ProductCategory,:ProductName,:ProductStock,:ProductAmountBroken,:ProductAmountInProgress,:ProductImage);
EOF;
	$stmt = $db->prepare($sql);
	$stmt->bindParam(':ProductID',$ID);
	$stmt->bindParam(':ProductManufacturer',$Manufacturer);
	$stmt->bindParam(':ProductCategory',$Category);
	$stmt->bindParam(':ProductName',$Name);
	$stmt->bindParam(':ProductStock',$Stock);
	$stmt->bindParam(':ProductAmountBroken',$AmountBroken);
	$stmt->bindParam(':ProductAmountInProgress',$AmountInProgress);
	$stmt->bindParam(":ProductImage",$Image);
	
	 $ret = $stmt->execute();
	 if (!$ret){
			$response = "Something went horribly wrong" + $db->lastErrorMsg();
	 }else{
			$response = "Product successfully created";
	 }
}else{

	$response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>

