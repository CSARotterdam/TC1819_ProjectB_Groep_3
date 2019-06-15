<?php

$response = array();
$response["UpdateReady"] = "";
$response["UpdateOverdue"] = "";
$response["Success"] = 0;
$Email = "";
$Date = "";
$db = new PDO('sqlite:Test.db');

if(isset($_POST["email"]) && isset($_POST["date"])){
  $Email = $_POST["email"];
  $Date = $_POST["date"];

  $sql =<<<EOF
      UPDATE Orders 
	  SET 	ReturnWarningBroadcasted = "True"
	  WHERE Email = :Email
	  AND DateOfReturn = :Date
EOF;

  $stmt = $db->prepare($sql);
  $stmt->bindParam(':Email',$Email);
  $stmt->bindParam(':Date',$Date);

  $ret = $stmt->execute();
  if (!$ret){
  		$response["Update"] = $db->lastErrorMsg();
  }else{
  		$response["Update"] = "ReadyBroadcasted successfully updated";
  }
  $response["Success"] = 1;

  $sql2 =<<<EOF
      UPDATE Orders 
	  SET 	OverdueDate = :Date
	  WHERE Email = :Email
	  
EOF;

  $stmt = $db->prepare($sql2);
  $stmt->bindParam(':Email',$Email);
  $stmt->bindParam(':Date',$Date);

  $ret = $stmt->execute();
  if (!$ret){
    $response["UpdateOverdue"] = $db->lastErrorMsg();
  }else{
    $response["UpdateOverdue"] = "ReadyBroadcasted successfully updated";
  }
  $response["Success"] = 1;


}

else{
  $response["UpdateReady"] = "One or more fields missing";
  $response["UpdateOverdue"] = "One or more fields missing";
  $response["Success"] = 0;
}
echo json_encode($response);
$db = null;
?>
