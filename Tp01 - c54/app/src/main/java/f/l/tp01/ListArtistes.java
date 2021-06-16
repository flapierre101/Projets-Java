package f.l.tp01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class ListArtistes extends AppCompatActivity {
    ListView listArtistes;
    Ecouteur ec;
    ArrayAdapter<String> aa;
    EnsembleChansons instanceEC;
    Vector <String> artistesV = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_artistes);

        instanceEC = EnsembleChansons.getInstance();
        listArtistes = findViewById(R.id.listArtistes);
        ec = new Ecouteur();
        instanceEC.lesArtistes(this);

        for (int i = 0; i<instanceEC.getPlayList().size(); i++){
            artistesV.add(instanceEC.getPlayList().get(i).getArtiste());
        }
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, artistesV);

        listArtistes.setAdapter(aa);
        listArtistes.setOnItemClickListener(ec);
    }

    private class Ecouteur implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tempo = (TextView) view;
            chansonArtiste(String.valueOf(tempo.getText())); // ajoute param avec nom de l'artiste
        }
    }

    public void chansonArtiste(String artiste) {
        Intent i = new Intent(this, ListChansons.class);
        i.putExtra("artiste", artiste);
        startActivity(i);
    }
}