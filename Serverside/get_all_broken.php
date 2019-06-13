<?php

$response = array();
$db = new PDO('sqlite:Test.db');

$sql =<<<EOF
		  SELECT ProductID, ProductName, ProductAmountBroken, 	ProductImage FROM Products WHERE ProductAmountBroken > 0;
EOF;

$stmt = $db->prepare($sql);
$stmt->execute();

$response["Products"] = array();
while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
    $product = array();
    $product["ProductID"] = $row['ProductID'];
    $product["ProductName"] = $row['ProductName'];
    $product["ProductAmountBroken"] = $row['ProductAmountBroken'];
    $product["ProductImage"] = $row['ProductImage'];
    array_push($response["Products"],$product);
}

if (!$stmt){
    $response["Readed"] = $db->lastErrorMsg();
    $response["Success"] = 0;
}else{
    $response["Readed"] = "Orders selected successfully";
    $response["Success"] = 1;
}

echo json_encode($response);
$db = null;
