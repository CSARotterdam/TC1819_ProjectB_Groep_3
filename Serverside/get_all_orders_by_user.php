<?php
/*
 * Following code will list all the orders
 */

// array for JSON response
$response = array();
if (isset($_POST["Email"])) {
    $Email = $_POST["Email"];

    // connecting to db
    $db = new PDO('sqlite:Test.db');
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // read db
    $sql = <<<EOF
          SELECT OrderID,ProductID,ProductAmount,DateOfReturn,Status FROM Orders WHERE Email = :Email;
EOF;

    $stmt = $db->prepare($sql);
    $stmt->execute([':Email' => $Email]);
    $response["Orders"] = array();

    //construct response
    while ($row = $stmt->fetch(\PDO::FETCH_ASSOC)) {
        $arr = array();
        $arr["OrderID"] = $row['OrderID'];
        $arr["ProductID"] = $row['ProductID'];
        $arr["ProductAmount"] = $row['ProductAmount'];
        $arr["DateOfReturn"] = $row['DateOfReturn'];
        $arr["Status"] = $row['Status'];
        array_push($response["Orders"],$arr);
    }
}
//encode response to json
$response["Success"] = 1;
echo json_encode($response);
$db=null;
?>