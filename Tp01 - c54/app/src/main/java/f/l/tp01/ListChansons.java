package f.l.tp01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class ListChansons extends AppCompatActivity {
    ListView listChanson;
    Ecouteur ec;
    EnsembleChansons instanceEC;
    Vector<Hashtable> chansonV = new Vector<>();
    TextView nombreChansons;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chansons);

        instanceEC = EnsembleChansons.getInstance();
        listChanson = findViewById(R.id.listeChansons);
        ec = new Ecouteur();
        nombreChansons = findViewById(R.id.nomDeLaListe);

        if (getIntent().getStringExtra("artiste") != null) //la liste de chanson est par artiste
            instanceEC.lesChansons(this, getIntent().getStringExtra("artiste"));
        else
            instanceEC.lesChansons(this, null);
        nombreChansons.setText("Nombre de chansons: " + instanceEC.getPlayList().size());

        // ajout des chansons dans hashtable puis dans vector
        for (int i = 0; i<instanceEC.getPlayList().size(); i++){
            Hashtable <String, Object> tempo = new Hashtable<>();
            tempo.put("Pochette", instanceEC.getPlayList().get(i).getPochette());
            tempo.put("Titre", instanceEC.getPlayList().get(i).getChansonNom());
            tempo.put("Artiste", instanceEC.getPlayList().get(i).getArtiste());
            chansonV.add(tempo);
        }

        // setup du simpleAdapter
        sa = new SimpleAdapter(this, (List) chansonV, R.layout.xmlsimpleadapter, new String[]{"Pochette", "Titre", "Artiste"}, new int[]{R.id.imageViewPochette, R.id.textViewChanson, R.id.textViewArtiste});
        sa.setViewBinder(new SimpleAdapter.ViewBinder(){

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
                    ImageView iv = (ImageView) view;
                    Bitmap bm = (Bitmap) data;
                    iv.setImageBitmap(bm);
                    return true;
                }
                return false;

            }

        });

        listChanson.setAdapter(sa);
        listChanson.setOnItemClickListener(ec);
    }

    private class Ecouteur implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            goToPlayer(i);
        }
    }

    //methode avec intent et son extra pour aller au player
    public void goToPlayer(int aJouer){
        Intent i = new Intent(this, Player.class);
        i.putExtra("chanson", aJouer);
        startActivity(i);
    }
}