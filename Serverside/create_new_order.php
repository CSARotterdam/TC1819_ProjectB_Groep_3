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
	$DateOfReady = $_POST["dateOfReady"];
    $DateOfReturn = $_POST["dateOfReturn"];
    $ReadyBroadcasted = "False";
    $ReturnWarningBroadcasted = "False";
    $Status = "pending";
	
 
	// connecting to db
	$db = new PDO('sqlite:Test.db');
	
	$sql =<<<EOF
		  SELECT COALESCE(MAX(OrderID),0) AS 'Max value' FROM Orders;
EOF;
	
	$ret = $db->query($sql);
		foreach($ret as $row){
			$OrderID = $row['Max value'] + 1;
		}
	
	$products = array();
	$productAmounts = array();
	$returnDates = array();
	$readyDates = array();
	$checkAmount = 'Amount';
	$checkDateOfReturn = 'Return';
	$checkDateOfReady = "Ready";




    //loop through POST parameters, skip user, then check if “amount” is in the key
	foreach($_POST as $key => $value){
		if ($key == 'email'){
			continue;
		}

		$amount = strpos($key, $checkAmount, 6);
		$return = strpos($key, $checkDateOfReturn,5);
		$ready = strpos($key, $checkDateOfReady, 5);
		if($amount){
			//If “Amount” is present in the key, add the value to amount array
			array_push($productAmounts, $value);
		}
		else if($return){
		    //if "Return" is present in the key, add the value to return array
            array_push($returnDates, $value);
        }

		else if($ready){
            array_push($readyDates, $value);
        }

		else{
			//If “Amount” not present in the key, add the value to product array
			array_push($products, $value);
		}
	}

	//Loop through both arrays, updating ProductID and ProductAmount each iteration
	for($i = 0;$i < count($products);$i++){
		$ProductID = $products[$i];
		$ProductAmount = $productAmounts[$i];
		$DateOfReturn = $returnDates[$i];
		$DateOfReady = $readyDates[$i];
		
		//Initialize SQL statement placing data into Database
		$sql = <<<EOF
		INSERT INTO Orders(OrderID,Email,ProductID,ProductAmount, DateOfReady, DateOfReturn,ReadyBroadcasted,ReturnWarningBroadcasted, Status)
		VALUES (:OrderID,:Email,:ProductID,:ProductAmount,:DateOfReady,:DateOfReturn,:ReadyBroadcasted,:ReturnWarningBroadcasted,:Status)
EOF;
		$stmt = $db->prepare($sql);
		$stmt->bindParam(':OrderID',$OrderID);
		$stmt->bindParam(':Email',$Email);
		$stmt->bindParam(':ProductID',$ProductID);
		$stmt->bindParam(':ProductAmount',$ProductAmount);
        $stmt->bindParam(':DateOfReady',$DateOfReady);
        $stmt->bindParam(':DateOfReturn',$DateOfReturn);
        $stmt->bindParam(':ReadyBroadcasted',$ReadyBroadcasted);
        $stmt->bindParam(':ReturnWarningBroadcasted',$ReturnWarningBroadcasted);
        $stmt->bindParam(':Status',$Status);
		//Execute SQL statement
		$ret = $stmt->execute();
		if (!$ret){
			$response = $db->lastErrorMsg();
		}else{
			$response = "Order succesfully placed";
		}
	}
}else{
	$response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>