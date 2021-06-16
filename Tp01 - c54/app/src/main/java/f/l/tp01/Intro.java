package f.l.tp01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Intro extends AppCompatActivity {
    Button artistes, chansons, rPlayer;
    Ecouteur ec;
    EnsembleChansons instanceEC;
    SauverEtat sEtat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        instanceEC = EnsembleChansons.getInstance();
        artistes = findViewById(R.id.buttonArtistes);
        chansons = findViewById(R.id.buttonChansons);
        rPlayer = findViewById(R.id.buttonPlayer);
        ec = new Ecouteur();

        artistes.setOnClickListener(ec);
        chansons.setOnClickListener(ec);
        rPlayer.setOnClickListener(ec);

        // Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
            if ( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 555);

            //Serialisation
        try {
            sEtat = recuObjet();
        }
        catch (Exception e){
            if (savedInstanceState != null && savedInstanceState.getSerializable("etat") != null) {
                sEtat = (SauverEtat) savedInstanceState.getSerializable("etat");

            }
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String[]permissions, int [] grantResults)
    {
        if ( requestCode == 555)
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission accordée ! ", Toast.LENGTH_LONG).show();
    }

    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == artistes.getId()){
                listeArtistes();
            }
            else if (view.getId() == chansons.getId()){
                listeChansons();
            }
            else if (view.getId() == rPlayer.getId()){
                //chercher dans le fichier sérialisé
                if (sEtat != null)
                    returnToPlayer(Integer.parseInt(sEtat.getChId()), Integer.parseInt(sEtat.getSvProgress()));
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Aucune liste de lecture en mémoire", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    public void listeArtistes() {
        Intent i = new Intent(this, ListArtistes.class);
        startActivity(i);
    }
    public void listeChansons() {
        Intent i = new Intent(this, ListChansons.class);
        startActivity(i);
    }

    public void returnToPlayer(int aJouer, int progres){
        Intent i = new Intent(this, Player.class);
        //info pour recuperer les info de la derniere ouverture (serialisation
        i.putExtra("dernChanson", aJouer);
        i.putExtra("progres", progres);
        startActivity(i);
    }
    //methode pour lire les infos dans le ficher de s/rialisation
    public SauverEtat recuObjet() throws Exception{
        SauverEtat tempo;
        FileInputStream fis = openFileInput("fichier.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        tempo = (SauverEtat) ois.readObject();
        ois.close();
        return tempo;
    }
}