package be.wilson.projetjanvier2018;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class AddItemActivity extends AppCompatActivity {

    //Déclaration des variables
    private Button addItem, cityChoice, back;
    private Intent homeIntent, cityChoiceIntent;
    private EditText nameET, descET, priceET, cityET;
    private RadioGroup stateRG, infosRG;
    private TextView error;
    private StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItem = (Button) findViewById(R.id.valid_btn);
        cityChoice = (Button) findViewById(R.id.cityBtn);
        back = (Button) findViewById(R.id.add_item_back_btn);

        addItem.setOnClickListener(validLis);
        cityChoice.setOnClickListener(cityChoiceLis);
        back.setOnClickListener(backLis);

        nameET = (EditText) findViewById(R.id.nameTxt);
        descET = (EditText) findViewById(R.id.descTxt);
        priceET = (EditText) findViewById(R.id.prixTxt);
        stateRG = (RadioGroup) findViewById(R.id.stateRadioGrp);
        cityET = (EditText) findViewById(R.id.cityTxt);
        infosRG = (RadioGroup) findViewById(R.id.infoRadioGrp);

        error = (TextView) findViewById(R.id.error);

        homeIntent = new Intent(AddItemActivity.this, HomeActivity.class);
        cityChoiceIntent = new Intent(AddItemActivity.this, LoadingScreenActivity.class);

        //Si on tourne l'écran
        if(savedInstanceState != null){
            nameET.setText(savedInstanceState.getString("name"));
            descET.setText(savedInstanceState.getString("desc"));
            priceET.setText(savedInstanceState.getString("price"));
            if(savedInstanceState.getString("state") == "1")
                stateRG.check(R.id.radio_state_new);
            else
                stateRG.check(R.id.radio_state_used);
            cityET.setText(savedInstanceState.getString("city"));
            if(savedInstanceState.getString("infos") == "1")
                infosRG.check(R.id.radio_info_by_hand);
            else
                stateRG.check(R.id.radio_info_must_send);
        }
    }

    //Validation de l'article
    View.OnClickListener validLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nameET.getText().equals("") ||
               descET.getText().equals("") ||
               priceET.getText().equals("") ||
               stateRG.getCheckedRadioButtonId() == -1 ||
               cityET.getText().equals("") ||
               infosRG.getCheckedRadioButtonId() == -1){
                error.setText(R.string.empty_error);
            }
            else{
                result = new StringBuilder();

                addParam("name", nameET.getText().toString(), false);
                addParam("descr", descET.getText().toString(), false);
                addParam("price", priceET.getText().toString(), false);

                if(stateRG.getCheckedRadioButtonId() == R.id.radio_state_new)
                    addParam("state", "1", false);
                else
                    addParam("state", "2", false);

                addParam("city", cityET.getText().toString(), false);

                if(infosRG.getCheckedRadioButtonId() == R.id.radio_info_by_hand)
                    addParam("infos", "1", true);
                else
                    addParam("infos", "2", true);

                new AddItemAsyncTask().execute(result.toString());

                startActivity(homeIntent);
            }
        }
    };

    //Fonction de construction des données de l'URL
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

    //Choix d'une ville
    View.OnClickListener cityChoiceLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(cityChoiceIntent, 2);
        }
    };

    //Détection du résultat du choix de villes
    protected void onActivityResult(int num_requete, int code_retour, Intent data){
        if(num_requete == 2){
            if(code_retour == Activity.RESULT_OK){
                cityET.setText(data.getStringExtra("city"));
            }
        }
    }

    //Retour vers l'accueil
    View.OnClickListener backLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(homeIntent);
        }
    };

    //Sauvegarde des données lors de la rotation
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putString("name", nameET.getText().toString());
        saveInstanceState.putString("desc", descET.getText().toString());
        saveInstanceState.putString("price", priceET.getText().toString());
        if(stateRG.getCheckedRadioButtonId() == R.id.radio_state_new)
            saveInstanceState.putString("state", "1");
        else
            saveInstanceState.putString("state", "2");
        saveInstanceState.putString("city", cityET.getText().toString());
        if(infosRG.getCheckedRadioButtonId() == R.id.radio_info_by_hand)
            saveInstanceState.putString("infos", "1");
        else
            saveInstanceState.putString("infos", "2");
    }
}
