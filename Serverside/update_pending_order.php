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
    $products = array();

    $sql =<<<EOF
      UPDATE Orders
	  SET 	Status = "readyForPickup"
	  WHERE OrderID = :ID
EOF;
    $sql2 =<<<EOF
      SELECT ProductID,ProductAmount from Orders WHERE OrderID = :ID
EOF;
    $sql3 =<<<EOF
      UPDATE Products
	  SET 	ProductAmountInProgress = ProductAmountInProgress + :Amount
	  WHERE ProductID = :ID
EOF;
    $stmt = $db->prepare($sql);
    $stmt->bindParam(':ID',$ID);

    $ret = $stmt->execute();
    if (!$ret){
        $response = $db->lastErrorMsg();
    }else{
        $response = "Order successfully updated";
    }

    $stmt = $db->prepare($sql2);
    $stmt->execute([':ID'=>$ID]);
    //construct array for updating inventory
    while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
        $product = array();
        $product['ProductID'] = $row['ProductID'];
        $product['ProductAmount'] = $row['ProductAmount'];
        array_push($products,$product);
    }
    //update product database
    foreach($products as $arrItem){
        $stmt = $db->prepare($sql3);
        $stmt->bindParam(':Amount',$arrItem["ProductAmount"]);
        $stmt->bindParam(':ID',$arrItem["ProductID"]);
        $stmt->execute();
    }
}else{

    $response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>