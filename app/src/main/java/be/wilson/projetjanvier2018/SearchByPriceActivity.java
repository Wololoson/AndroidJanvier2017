package be.wilson.projetjanvier2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchByPriceActivity extends AppCompatActivity {

    //Déclaration des variables
    Intent confirmIntent, backIntent;
    Button confirmBtn, backBtn;
    EditText minET, maxET;

    SearchByPriceActivity act;
    private StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_price);

        confirmIntent = new Intent(SearchByPriceActivity.this, ResultsActivity.class);
        backIntent = new Intent(SearchByPriceActivity.this, HomeActivity.class);

        backBtn = (Button) findViewById(R.id.srch_by_p_back);
        confirmBtn = (Button) findViewById(R.id.search_p_valid_btn);

        minET = (EditText) findViewById(R.id.min_priceTxt);
        maxET = (EditText) findViewById(R.id.max_priceTxt);

        act = this;

        backBtn.setOnClickListener(backLis);
        confirmBtn.setOnClickListener(confirmLis);

        //Si on tourne l'écran
        if(savedInstanceState != null){
            minET.setText(savedInstanceState.getString("min"));
            maxET.setText(savedInstanceState.getString("max"));
        }
    }

    //Retour à l'accueil
    View.OnClickListener backLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(backIntent);
        }
    };

    //Validation de la recherche
    View.OnClickListener confirmLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(minET.getText().toString().equals("") ||
                    maxET.getText().toString().equals("")) {
                Toast.makeText(act, "Veuillez remplir les deux champs !", Toast.LENGTH_LONG).show();
            }
            else{
                result = new StringBuilder();

                addParam("minP", minET.getText().toString(), false);
                addParam("maxP", maxET.getText().toString(), true);

                new SearchByPAsyncTask(act).execute(result.toString());
            }
        }
    };

    //Construction de la chaine de paramètres
    public void addParam(String key, String value, boolean last){
        try {
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));
            if(!last)
                result.append("&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Sauvegarde pour la rotation
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putString("min", minET.getText().toString());
        saveInstanceState.putString("max", maxET.getText().toString());
    }
}
