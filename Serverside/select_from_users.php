<?php
/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();
$response["Success"] = 0;
if (isset($_POST["Email"])){
    $Email = $_POST["Email"];

    // connecting to db
    $db = new PDO('sqlite:Test.db');

    // read db
    $sql =<<<EOF
		  SELECT * from Users WHERE Email = :Email
EOF;
    $stmt = $db->prepare($sql);
    $stmt->execute([':Email' => $Email]);


    //construct response
    while($row = $stmt->fetch(\PDO::FETCH_ASSOC) ) {
        $response["Success"] = 1;
        $response["Email"] = $row['Email'];
        $response["Permission"] = $row['Permission'];
    }


}else{
    $response["Message"] = "Missing at least 1 required field";
}
//encode response to json
echo json_encode($response);
$db=null;
?>
