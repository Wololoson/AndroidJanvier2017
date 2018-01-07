package be.wilson.projetjanvier2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    //Déclaration des variables
    private Button addItem, searchByP, searchByC;
    private Intent addItemIntent, searchByPIntent, searchByCIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addItem = (Button) findViewById(R.id.home_add_btn);
        searchByP = (Button) findViewById(R.id.home_srch_p_btn);
        searchByC = (Button) findViewById(R.id.home_srch_c_btn);

        addItem.setOnClickListener(choiceLis);
        searchByP.setOnClickListener(choiceLis);
        searchByC.setOnClickListener(choiceLis);

        addItemIntent = new Intent(HomeActivity.this, AddItemActivity.class);
        searchByPIntent = new Intent(HomeActivity.this, SearchByPriceActivity.class);
        searchByCIntent = new Intent(HomeActivity.this, SearchByCityActivity.class);
    }

    //Choix de la fonctionnalité
    View.OnClickListener choiceLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.home_add_btn:
                    startActivity(addItemIntent);
                    break;
                case R.id.home_srch_p_btn:
                    startActivity(searchByPIntent);
                    break;
                case R.id.home_srch_c_btn:
                    startActivity(searchByCIntent);
                    break;
            }
        }
    };
}
