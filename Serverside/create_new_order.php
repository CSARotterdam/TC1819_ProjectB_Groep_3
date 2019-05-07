<?php
/*
 * Following code will list all the products
 */
 

$response = "";
   
//check for required fields
if (isset($_POST["email"])){
	$Email = $_POST["email"];
	$OrderID = 0;
	$ProductID = '';
	$ProductAmount = 0;
	// include db connect class
	require_once __DIR__ . '/db_connect.php';
 
	// connecting to db
	$db = new DB_CONNECT();
	
	$sql =<<<EOF
		  SELECT COALESCE(MAX(OrderID),0) AS 'Max value' FROM Orders;
EOF;
	
	$ret = $db->query($sql);
		while($row = $ret->fetchArray(SQLITE3_ASSOC) ) {
			$OrderID = $row['Max value'] + 1;
		}
	
	$products = array();
	$productamounts = array();
	$check = 'Amount';

	//loop through POST parameters, skip user, then check if “amount” is in the key
	foreach($_POST as $key => $value){
		if ($key == 'email'){
			continue;
		}
		$Position = strpos($key, $check, 6);
		if($Position){
			//If “Amount” is present in the key, add the value to amount array
			array_push($productamounts, $value);
		}else{
			//If “Amount” not present in the key, add the value to product array
			array_push($products, $value);
		}
	}

	//Loop through both arrays, updating ProductID and ProductAmount each iteration
	for($i = 0;$i < count($products);$i++){
		$ProductID = $products[$i];
		$ProductAmount = $productamounts[$i]; 
		
		//Initialize SQL statement placing data into Database
		$sql = <<<EOF
		INSERT INTO Orders(OrderID,Email,ProductID,ProductAmount)
		VALUES ('$OrderID','$Email','$ProductID','$ProductAmount');
EOF;

		//Execute SQL statement
		$ret = $db->exec($sql);
		if (!$ret){
			$response = $db->lastErrorMsg();
		}else{
			$response = "Product successfully created";
		}
	}
}else{
	$response = "Missing at least 1 required field";
}
echo ($response);
?>