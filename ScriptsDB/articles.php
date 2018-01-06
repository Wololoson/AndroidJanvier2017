<?php 
	try
	{
		$bdd = new PDO('mysql:host=192.168.0.46;port=3306;dbname=androidjan18;charset=utf8', 'adminww', '@dmin4621');
	}
	catch(Exception $e)
	{
	    die('Erreur : '.$e->getMessage());
	}

	$nom = $_POST['name'];
	$descr = $_POST['descr'];
	$prix = $_POST['price'];
	$etat = $_POST['state'];
	$ville = $_POST['city'];
	$info = $_POST['infos'];

   	$stmt = $bdd->prepare("INSERT INTO article (nom, descr, prix, etat, ville, info) VALUES (:nom, :descr, :prix, :etat, :ville, :info)");
   	$stmt->execute(array('nom' => $nom,
   						 'descr' => $descr,
   						 'prix' => $prix,
   						 'etat' => $etat,
   					 	 'ville' => $ville,
   					 	 'info' => $info));

   	echo("Ca arrive jusque la.");

   	$stmt->close();
?>
