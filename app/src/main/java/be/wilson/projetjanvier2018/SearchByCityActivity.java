package be.wilson.projetjanvier2018;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchByCityActivity extends AppCompatActivity {

    private Intent homeIntent, cityChoiceIntent;
    private EditText cityET;
    private Button backBtn, cityChoiceBtn, confirmBtn;

    private SearchByCityActivity act;
    private StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_city);

        cityET = (EditText) findViewById(R.id.cityTxt);
        backBtn = (Button) findViewById(R.id.srch_by_c_back);
        cityChoiceBtn = (Button) findViewById(R.id.cityBtn);
        confirmBtn = (Button) findViewById(R.id.search_c_valid_btn);

        backBtn.setOnClickListener(backLis);
        cityChoiceBtn.setOnClickListener(cityChoiceLis);
        confirmBtn.setOnClickListener(confirmLis);

        act = this;

        homeIntent = new Intent(SearchByCityActivity.this, HomeActivity.class);
        cityChoiceIntent = new Intent(SearchByCityActivity.this, LoadingScreenActivity.class);

        if(savedInstanceState != null){
            cityET.setText(savedInstanceState.getString("city"));
        }
    }

    View.OnClickListener confirmLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(cityET.getText().equals("") ) {
                Toast.makeText(act, "Veuillez remplir le champ !", Toast.LENGTH_LONG);
            }
            else{
                result = new StringBuilder();

                addParam("city", cityET.getText().toString(), true);

                new SearchByCAsyncTask(act).execute(result.toString());
            }
        }
    };

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

    View.OnClickListener cityChoiceLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(cityChoiceIntent, 4);
        }
    };

    protected void onActivityResult(int num_requete, int code_retour, Intent data){
        if(num_requete == 4){
            if(code_retour == Activity.RESULT_OK){
                cityET.setText(data.getStringExtra("city"));
            }
        }
    }

    View.OnClickListener backLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(homeIntent);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putString("city", cityET.getText().toString());
    }
}
