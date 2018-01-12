package be.wilson.projetjanvier2018;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    //Déclaration des variables
    private TableLayout itemList;
    private Button nextBtn, prevBtn, sortBtn, backBtn;

    private Intent homeIntent, infosIntent;
    private Bundle itemBundle;
    private ArrayList<String> itemAL;
    private String[][] items;
    private int screenSize;
    private int orientation;
    private int firstArt;
    private boolean ASC;
    private int interval;
    private String data;
    private boolean resuming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        nextBtn = (Button) findViewById(R.id.next_btn);
        prevBtn = (Button) findViewById(R.id.prev_btn);
        sortBtn = (Button) findViewById(R.id.sort_btn);
        backBtn = (Button) findViewById(R.id.resulsts_back_btn);

        nextBtn.setOnClickListener(nextList);
        prevBtn.setOnClickListener(prevList);
        sortBtn.setOnClickListener(SortList);
        backBtn.setOnClickListener(backList);

        homeIntent = new Intent(ResultsActivity.this, HomeActivity.class);
        infosIntent = new Intent(ResultsActivity.this, ItemInfosActivity.class);

        screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        itemList = (TableLayout) findViewById(R.id.list_items);

        orientation = getResources().getConfiguration().orientation;

        itemBundle = getIntent().getExtras();

        //Si on tourne l'écran
        if(savedInstanceState != null){
            ASC = savedInstanceState.getBoolean("ASC");
            itemAL = savedInstanceState.getStringArrayList("itemAL");
            interval = savedInstanceState.getInt("interval");

            items = new String[itemAL.size() / 6][6];
        }
        else {
            ASC = true;
            itemAL = itemBundle.getStringArrayList("items");

            if(itemAL == null){
                Toast.makeText(this, "Problème de connexion au serveur !", Toast.LENGTH_LONG).show();
                startActivity(homeIntent);
            }

            items = new String[itemAL.size() / 6][6];
            if (items.length < 5)
                interval = items.length;
            else
                interval = 5;
        }

        data = itemBundle.getString("data");
        resuming = false;

        //Passagde des donnéees de l'ArrayList vers un tableau à deux dimensions
        int j = 0;
        for (int i = 0; i < itemAL.size(); i = i + 6) {
            items[j][0] = itemAL.get(i);
            items[j][1] = itemAL.get(i + 1);
            items[j][2] = itemAL.get(i + 2);
            items[j][3] = itemAL.get(i + 3);
            items[j][4] = itemAL.get(i + 4);
            items[j][5] = itemAL.get(i + 5);
            j++;
        }

        //Préparation du sstème de pages
        if (interval == 0) {
            TableRow tr = new TableRow(this);
            TextView tv = new TextView(this);
            tv.setText(R.string.emptyItemList);
            tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setPadding(10, 10, 10, 10);
            tr.addView(tv);
            itemList.addView(tr);
        } else {
            sortASC();
            firstArt = 0;
            fill(interval);
            enableBtns();
        }
    }

    //Activation/désactivation des boutons
    public void enableBtns(){
        if(firstArt < 5){
            prevBtn.setEnabled(false);
            if(items.length > 5)
                nextBtn.setEnabled(true);
            else
                nextBtn.setEnabled(false);
        }
        else if(firstArt >= items.length-5){
            nextBtn.setEnabled(false);
            if(items.length > 5)
                prevBtn.setEnabled(true);
            else
                prevBtn.setEnabled(false);
        }
        else{
            if(items.length > 10) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
            }
            else {
                nextBtn.setEnabled(false);
                prevBtn.setEnabled(false);
            }
        }
    }

    //Tri ascendant
    public void sortASC(){
        int max_i;
        for(int i = items.length; i > 1; i--){
            max_i = max(i);
            swap(i-1, max_i);
        }
    }

    //Swap servant aux tris
    void swap(int x, int y)
    {
        String[] tmp = new String[6];

        tmp[0] = items[x][0];
        tmp[1] = items[x][1];
        tmp[2] = items[x][2];
        tmp[3] = items[x][3];
        tmp[4] = items[x][4];
        tmp[5] = items[x][5];

        items[x][0] = items[y][0];
        items[x][1] = items[y][1];
        items[x][2] = items[y][2];
        items[x][3] = items[y][3];
        items[x][4] = items[y][4];
        items[x][5] = items[y][5];

        items[y][0] = tmp[0];
        items[y][1] = tmp[1];
        items[y][2] = tmp[2];
        items[y][3] = tmp[3];
        items[y][4] = tmp[4];
        items[y][5] = tmp[5];
    }

    //Fonction retournant un maximum servant aux tris
    public int max(int length)
    {
        int i=0, indice_max=0;

        while(i < length)
        {
            if(Double.parseDouble(items[i][1]) > Double.parseDouble(items[indice_max][1]))
                indice_max = i;
            i++;
        }

        return indice_max;
    }

    //Inversion du tri
    public void reverse(){
        int max_i;
        for(int i = 0; i < items.length/2; i++){
            swap(i, items.length-(i+1));
        }
    }

    //Remplissage du tableau des articles en fonction de l'écran
    public void fill(int end){
        itemList.removeAllViews();
        TableRow tr;
        TextView nom;
        TextView prix;
        TextView ville = null;
        TextView etat = null;

        tr = new TableRow(this);

        nom = new TextView(this);
        nom.setText(R.string.nameCol);
        nom.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        nom.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            nom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        else
            nom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        nom.setPadding(10,10,50,10);
        tr.addView(nom);

        prix = new TextView(this);
        prix.setText(R.string.priceCol);
        prix.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        prix.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            prix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        else
        prix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        prix.setPadding(10,10,50,10);
        tr.addView(prix);

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                ville = new TextView(this);
                ville.setText(R.string.cityCol);
                ville.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ville.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                ville.setPadding(10,10,50,10);
                tr.addView(ville);
            }
        }

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            ville = new TextView(this);
            ville.setText(R.string.cityCol);
            ville.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            ville.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            else
                ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            ville.setPadding(10,10,50,10);
            tr.addView(ville);

            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                etat = new TextView(this);
                etat.setText(R.string.stateCol);
                etat.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                etat.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                etat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                etat.setPadding(10,10,50,10);
                tr.addView(etat);
            }
        }

        itemList.addView(tr);

        for(int i = firstArt; i < firstArt+end; i++){
            tr = new TableRow(this);
            tr.setClickable(true);
            tr.setOnClickListener(infosLis);
            tr.setTag(i);

            nom = new TextView(this);
            nom.setText(items[i][0]);
            nom.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            nom.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                nom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            else
                nom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            nom.setPadding(10,10,50,10);
            tr.addView(nom);

            prix = new TextView(this);
            prix.setText(items[i][1]);
            prix.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            prix.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                prix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            else
                prix.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            prix.setPadding(10,10,50,10);
            tr.addView(prix);

            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    ville = new TextView(this);
                    ville.setText(items[i][2]);
                    ville.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    ville.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    ville.setPadding(10,10,50,10);
                    tr.addView(ville);
                }
            }

            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                ville = new TextView(this);
                ville.setText(items[i][2]);
                ville.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ville.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                    ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                else
                    ville.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                ville.setPadding(10,10,50,10);
                tr.addView(ville);

                if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    etat = new TextView(this);
                    if(items[i][3].equals("1"))
                        etat.setText(R.string.radio_state_new);
                    else
                        etat.setText(R.string.radio_state_used);
                    etat.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    etat.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    etat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    etat.setPadding(10,10,50,10);
                    tr.addView(etat);
                }
            }
            itemList.addView(tr);
        }
    }

    //Listener de tri
    View.OnClickListener SortList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reverse();
            if(ASC) {
                TextView tmp = (TextView) view;
                tmp.setText(R.string.desc_sort);
                ASC = false;
            }
            else{
                TextView tmp = (TextView) view;
                tmp.setText(R.string.asc_sort);
                ASC = true;
            }
            fill(interval);
            enableBtns();
        }
    };

    //Listener du bouton suivant
    View.OnClickListener nextList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            interval = -1;
            firstArt += 5;
            if(firstArt <= items.length && firstArt >= items.length-5)
                interval = items.length - firstArt;
            if(interval == -1) {
                interval = 5;
                firstArt -= 5;
            }
            fill(interval);
            enableBtns();
        }
    };

    //Listener du bouton précédent
    View.OnClickListener prevList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            firstArt -= 5;
            interval = 5;
            fill(interval);
            enableBtns();
        }
    };

    //Listener des infos de l'article
    View.OnClickListener infosLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String[] item = {items[(int)view.getTag()][0],
                             items[(int)view.getTag()][1],
                             items[(int)view.getTag()][2],
                             items[(int)view.getTag()][3],
                             items[(int)view.getTag()][4],
                             items[(int)view.getTag()][5]};
            infosIntent.putExtra("item", item);
            startActivityForResult(infosIntent, 5);
        }
    };

    //Listener de retour à l'accueil
    View.OnClickListener backList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(homeIntent);
        }
    };

    //Sauvegarde lors de la rotation
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putBoolean("ASC", ASC);
        saveInstanceState.putStringArrayList("itemAL", itemAL);
        saveInstanceState.putInt("interval", interval);
    }

    //Gestion de la mise en veille
    @Override
    protected void onPause() {
        super.onPause();
        resuming = true;
    }

    //Gestion de la mise en veille
    @Override
    protected void onResume() {
        super.onResume();
        if(resuming) {
            if(data.charAt(0) == 'c')
                new SearchByCAsyncTask(this).execute(data);
            else
                new SearchByPAsyncTask(this).execute(data);
            resuming = false;
        }
    }
}
