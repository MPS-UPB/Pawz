<?php
/*
require_once('Streamer.php');

$ft = new File_Streamer();
$ft->setDestination('uploads/');
$ft->receive();
*/
echo urldecode($_POST["data"]);
$img = imagecreatefromstring(urldecode(base64_decode($_POST["data"]))); 
if($img != false) 
{ 
   imagejpeg($img, 'asdf.jpg'); 
} 