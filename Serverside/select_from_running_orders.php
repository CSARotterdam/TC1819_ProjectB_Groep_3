<?php
/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();
$response["Success"] = 0;
if (isset($_POST["OrderID"])){
    $OrderID = $_POST["OrderID"];

    // connecting to db
    $db = new PDO('sqlite:Test.db');

    // read db
    $sql =<<<EOF
		  SELECT * from Orders WHERE OrderID = :OrderID AND Status = "InProgress"
EOF;
    $sql2 =<<<EOF
		  SELECT ProductName from Products WHERE ProductID = :ProductID
EOF;

    $stmt = $db->prepare($sql);
    $stmt->execute([':OrderID'=>$OrderID]);
    $response["Orders"] = array();

    //construct response
    while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
        $arr = array();
        $arr["OrderID"] = $row['OrderID'];
        $arr["ProductID"] = $row['ProductID'];
        $arr["ProductAmount"] = $row['ProductAmount'];
        $stmtInner = $db->prepare($sql2);
        $stmtInner->execute([':ProductID'=>$arr["ProductID"]]);
        while($rowInner = $stmtInner->fetch(\PDO::FETCH_ASSOC) ) {
            $arr["ProductName"] = $rowInner["ProductName"];
        }
        array_push($response["Orders"],$arr);
    }
    $response["Success"] = 1;

}else{

}
//encode response to json
echo json_encode($response);
$db=null;
?>