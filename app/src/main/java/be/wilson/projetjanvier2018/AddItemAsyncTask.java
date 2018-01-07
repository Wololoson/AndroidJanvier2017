package be.wilson.projetjanvier2018;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AddItemAsyncTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        String data = params[0];

        try {
            //Connexion
            URL url = new URL("http://lanww.ddns.net/articles.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Envoi des données
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            //Récupération des erreurs s'il y en a
            InputStream is = urlConnection.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            String result = "";
            String line = "";
            while((line = br.readLine()) != null){
                result += line + "\n";
            }
            br.close();
            is.close();
            urlConnection.disconnect();
            Log.e("PHP RESULT", result);
        } catch (Exception e) {
            Log.e("Async exception",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
