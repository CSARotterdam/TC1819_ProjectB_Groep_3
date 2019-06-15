<?php

$response = array();

if(isset($_POST["ProductID"]) & isset($_POST["ProductAmountBroken"])) {
    $db = new PDO('sqlite:Test.db');
    $productAmountBroken = $_POST["ProductAmountBroken"];
    $productID = $_POST["ProductID"];

    $sql = <<<EOF
		UPDATE Products
		SET ProductAmountBroken = :ProductAmountBroken 
		WHERE ProductID = :ProductID
EOF;

    $stmt = $db->prepare($sql);
    $stmt->bindParam(':ProductAmountBroken', $productAmountBroken);
    $stmt->bindParam(':ProductID', $productID);

    $ret = $stmt->execute();
    if (!$ret) {
        $response["Update"] = $db->lastErrorMsg();
        $response["success"] = 0;

    } else {
        $response["success"] = 1;
        $response["Update"] = "Amountbroken successfully updated";
    }

}
else {
    $response["success"] = 0;
    $response["update"] = "One or more fields missing...";
}

echo json_encode($response);
$db = null;