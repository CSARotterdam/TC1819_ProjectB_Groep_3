<?php

if(isset($_POST["email"])){
	$Email = $_POST["email"];
}

$response = array();
$response["Success"] = 0;
$response["Orders"] = array();

$db = new PDO('sqlite:Test.db');



$sql =<<<EOF
		  SELECT * from Orders WHERE Email = :Email
EOF;
    $stmt = $db->prepare($sql);
    $stmt->execute([':Email' => $Email]);

    $order = array();

    while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
            $order["Email"] = $row['Email'];
            $order["OrderID"] = $row['OrderID'];
            $order["ProductID"] = $row['ProductID'];
						$order["DateOfReady"] = $row['DateOfReady'];
						$order["DateOfReturn"] = $row["DateOfReturn"];
						$order["ReadyBroadcasted"] = $row["ReadyBroadcasted"];
            array_push($response["Orders"],$order);
    }

$response["Success"] = 1;


echo json_encode($response);
$db = null;
?>
