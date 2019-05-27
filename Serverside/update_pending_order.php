<?php
/*
 * Following code will list all the products
 */

// array for JSON response
$response= null;

//check for required fields
if (isset($_POST["OrderID"])){

    $ID = $_POST["OrderID"];


    // connecting to db
    $db = new PDO('sqlite:Test.db');


    $sql =<<<EOF
      UPDATE Orders
	  SET 	Status = "readyForPickup"
	  WHERE OrderID = :ID
EOF;
    $stmt = $db->prepare($sql);
    $stmt->bindParam(':ID',$ID);

    $ret = $stmt->execute();
    if (!$ret){
        $response = $db->lastErrorMsg();
    }else{
        $response = "Order successfully updated";
    }
}else{

    $response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>