<?php
 
/**
 * A class file to connect to database
 */
class DB_CONNECT extends SQLite3 {
 
    // constructor
    function __construct() {
        // connecting to database
		require_once __DIR__ . '/db_config.php';
        $this->open(DB_DATABASE);
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }
}
?>