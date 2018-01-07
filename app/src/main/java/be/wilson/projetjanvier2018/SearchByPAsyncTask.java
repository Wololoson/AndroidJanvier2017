package be.wilson.projetjanvier2018;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchByPAsyncTask extends AsyncTask<String, String, ArrayList<String>> {

    //Déclaration des variables
    private SearchByPriceActivity act;
    private Intent intent;

    //Constructeur qui récupère l'activité d'origine
    public SearchByPAsyncTask(SearchByPriceActivity act){
        this.act = act;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String data = params[0];
        ArrayList<String> itemsString = null;

        try {
            //Connexion
            URL url = new URL("http://lanww.ddns.net/searchByP.php?"+data);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","text/plain");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setDoInput(true);

            //Récupération des données
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            String result = "";
            String line = "";
            while((line = br.readLine()) != null){
                result += line;
            }
            br.close();
            is.close();
            urlConnection.disconnect();

            JSONArray itemsArr = new JSONArray(result);
            itemsString = new ArrayList<>();

            //Construction de l'ArrayList des données
            for (int i = 0; i < itemsArr.length(); i++) {
                itemsString.add(itemsArr.getJSONObject(i).getString("nom"));
                itemsString.add(itemsArr.getJSONObject(i).getString("prix"));
                itemsString.add(itemsArr.getJSONObject(i).getString("ville"));
                itemsString.add(itemsArr.getJSONObject(i).getString("etat"));
                itemsString.add(itemsArr.getJSONObject(i).getString("desc"));
                itemsString.add(itemsArr.getJSONObject(i).getString("info"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemsString;
    }

    @Override
    protected void onPostExecute(ArrayList<String> items) {
        super.onPostExecute(items);

        //Envoi des données dans l'activité de résultats
        intent = new Intent(act, ResultsActivity.class);
        intent.putExtra("items", items);
        act.startActivityForResult(intent, 3);
    }
}
