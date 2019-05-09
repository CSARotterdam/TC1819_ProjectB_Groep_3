<?php
/*
 * Following code will list all the products
 */

// array for JSON response
$response;

//check for required fields
if (isset($_POST["Email"]) && isset($_POST["Permission"])){

    $Email = $_POST["Email"];
    $Permission = $_POST["Permission"];

    // connecting to db
    $db = new PDO('sqlite:Test.db');

    $sql =<<<EOF
      UPDATE Users 
	  SET 	Permission = :Permission
	  WHERE Email = :Email
EOF;
    $stmt = $db->prepare($sql);
    $stmt->bindParam(':Permission',$Permission);
    $stmt->bindParam(':Email',$Email);

    $ret = $stmt->execute();
    if (!$ret){
        $response = $db->lastErrorMsg();
    }else{
        $response = "User permissions successfully updated";
    }
}else{

    $response = "Missing at least 1 required field";
}
echo ($response);
$db=null;
?>
