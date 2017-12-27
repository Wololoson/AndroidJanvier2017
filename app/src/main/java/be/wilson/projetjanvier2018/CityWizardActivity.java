package be.wilson.projetjanvier2018;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

public class CityWizardActivity extends AppCompatActivity {

    private RadioGroup rgCities;
    private SearchView searchBtn;
    private String[] cities;
    private String selectedCity;
    private Intent intentReturn;
    private CityWizardActivity act;
    private Button submitBtn, backBtn;
    private int screenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_wizard);

        act = this;

        rgCities = (RadioGroup) findViewById(R.id.radioGrp_cities);
        searchBtn = (SearchView) findViewById(R.id.searchBar);

        searchBtn.setOnQueryTextListener(searchLis);
        int searchIconId = searchBtn.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) searchBtn.findViewById(searchIconId);
        searchIcon.setMaxWidth(40);
        searchIcon.setMaxHeight(40);

        submitBtn = (Button) findViewById(R.id.valid_btn);
        backBtn = (Button) findViewById(R.id.city_back_button);

        submitBtn.setOnClickListener(submitLis);
        backBtn.setOnClickListener(backLis);

        intentReturn = getIntent();
        cities = intentReturn.getStringArrayExtra("cities");

        if(cities == null){
            Toast.makeText(act, "Problème de connexion au serveur !", Toast.LENGTH_LONG).show();
            finish();
        }

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            LinearLayout linearLayout1 = (LinearLayout) searchBtn.getChildAt(0);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            autoComplete.setTextSize(60);
        }
    }

    View.OnClickListener backLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener submitLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = rgCities.getCheckedRadioButtonId();
            if(id != -1) {
                Intent submit = new Intent();
                RadioButton rb = (RadioButton) findViewById(id);
                selectedCity = rb.getText().toString();
                submit.putExtra("city", selectedCity);
                setResult(RESULT_OK, submit);
                finish();
            }
            else{
                Toast.makeText(act, "Veuillez choisir une ville !", Toast.LENGTH_LONG).show();
            }
        }
    };

    SearchView.OnQueryTextListener searchLis = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            boolean found = false;
            rgCities.removeAllViews();
            RadioButton rb;
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].contains(query)) {
                    rb = new RadioButton(act);
                    rb.setText(cities[i]);
                    rgCities.addView(rb);
                    found = true;
                }
            }
            if (!found)
                Toast.makeText(act, "Aucune ville n'a été trouvée", Toast.LENGTH_LONG).show();

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}
