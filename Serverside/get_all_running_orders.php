<?php
/*
 * Following code will list all the orders
 */

// array for JSON response
$response = array();


// connecting to db
$db = new PDO('sqlite:Test.db');
$db -> setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);

// read db
$sql =<<<EOF
      SELECT OrderID,Email,ProductAmount FROM Orders WHERE Status = "InProgress";
EOF;

$sql2 =<<<EOF
		  SELECT COALESCE(MAX(OrderID),0) AS 'Max value' FROM Orders;
EOF;

//construct response for amount of orders
$AmountOfOrders = 0;
$ret = $db->query($sql2);
foreach($ret as $row){
    $AmountOfOrders = $row['Max value'];
}

// initialize response
$response["Success"] = 0;
$response["AmountOfOrders"] = $AmountOfOrders;
$response["Orders"] = array();


//construct response
$ret = $db->query($sql);
foreach($ret as $row) {
    $arr = array();
    $arr["OrderID"] = $row['OrderID'];
    $arr["Email"] = $row["Email"];
    $arr["ProductAmount"] = $row["ProductAmount"];
    array_push($response["Orders"],$arr);
}
//encode response to json
$response["Success"] = 1;
echo json_encode($response);
$db=null;
?>

