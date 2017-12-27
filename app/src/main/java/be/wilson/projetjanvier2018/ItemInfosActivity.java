package be.wilson.projetjanvier2018;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemInfosActivity extends AppCompatActivity {

    private TextView name, desc, price, state, city, infos;
    private Button backBtn;

    private Bundle passedBndl;
    private String[] item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_infos);

        if(savedInstanceState != null) {
            passedBndl = null;
            item = savedInstanceState.getStringArray("item");
        }
        else{
            passedBndl = getIntent().getExtras();
            item = passedBndl.getStringArray("item");
        }

        name = (TextView) findViewById(R.id.nameTxtR);
        desc = (TextView) findViewById(R.id.descTxtR);
        price = (TextView) findViewById(R.id.prixTxtR);
        state = (TextView) findViewById(R.id.stateTxtR);
        city = (TextView) findViewById(R.id.cityTxtR);
        infos = (TextView) findViewById(R.id.infoTxtR);

        backBtn = (Button) findViewById(R.id.item_infos_back);

        backBtn.setOnClickListener(backLis);

        fill();
    }

    public void fill(){
        name.setText(item[0]);
        desc.setText(item[4]);
        price.setText(item[1]);
        if(item[3].equals("1"))
            state.setText("New");
        else
            state.setText("Used");
        city.setText(item[2]);
        if(item[5].equals("1"))
            infos.setText("Must be picked up by hands");
        else
            infos.setText("Can be sent");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putStringArray("item", item);
    }

    View.OnClickListener backLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
