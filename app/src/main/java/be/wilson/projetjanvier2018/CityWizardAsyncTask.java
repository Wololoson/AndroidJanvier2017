package be.wilson.projetjanvier2018;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CityWizardAsyncTask extends AsyncTask <Void, Void, String[]> {

    //Déclaration des variables
    private LoadingScreenActivity act;
    private Intent intent;

    //Constructeur qui récupère l'activité d'origine
    public CityWizardAsyncTask(LoadingScreenActivity act){
        this.act = act;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(Void... params) {
        String[] citiesString = null;

        try {
            //Connexion
            URL url = new URL("http://lanww.ddns.net/cities.json");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Récupération des données
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String result = "";
            String line = "";
            while((line = br.readLine()) != null){
                result += line;
            }
            br.close();
            is.close();
            urlConnection.disconnect();

            JSONArray citiesArr = new JSONArray(result);
            citiesString = new String[citiesArr.length()];

            //Passage des données dans un tableau
            for (int i = 0; i < citiesArr.length(); i++) {
                citiesString[i] = citiesArr.getJSONObject(i).getString("name");
            }

        }
        catch(Exception e){
            Log.e("Async exception",e.getMessage());
            e.printStackTrace();
        }

        return citiesString;
    }

    @Override
    protected void onPostExecute(String[] cities) {
        super.onPostExecute(cities);

        //Retour des données dans l'activité
        intent = new Intent(act, CityWizardActivity.class);
        intent.putExtra("cities", cities);
        act.startActivityForResult(intent, 1);
    }
}
