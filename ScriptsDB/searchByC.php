
<?php
	header('Content-type: application/json');

	try
	{
		$bdd = new PDO('mysql:host=192.168.0.46;port=3306;dbname=androidjan18;charset=utf8', 'adminww', '@dmin4621');
	}
	catch(Exception $e)
	{
		die('Erreur : '.$e->getMessage());
	}

	$city = $_GET['city'];
	$items = array();

	$sql = "SELECT * FROM article WHERE ville LIKE '%".$city."%'";

	foreach($bdd->query($sql) as $row){
		$temp = [
			'nom'=>$row['nom'],
			'prix'=>$row['prix'],
			'ville'=>$row['ville'],
			'etat'=>$row['etat'],
			'desc'=>$row['descr'],
			'info'=>$row['info']
		];
		array_push($items,$temp);
	}

	echo json_encode($items);
?>
