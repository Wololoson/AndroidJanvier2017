package be.wilson.projetjanvier2018;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LoadingScreenActivity extends AppCompatActivity {

    //Déclaration des variables
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Si on tourne l'écran
        if(savedInstanceState == null) {
                CityWizardAsyncTask cityGet = new CityWizardAsyncTask(LoadingScreenActivity.this);
                cityGet.execute();
        }
    }

    //Arrêt du chargement si la tâche asynchrône a terminé ses opérations
    protected void onActivityResult(int num_requete, int code_retour, Intent data){
        if(num_requete == 1){
            if(code_retour == RESULT_OK){
                setResult(RESULT_OK, data);
                finish();
            }
            else{
                finish();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putBoolean("notFirstTime", true);
    }
}
