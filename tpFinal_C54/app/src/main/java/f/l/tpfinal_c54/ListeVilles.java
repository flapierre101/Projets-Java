package f.l.tpfinal_c54;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class ListeVilles extends AppCompatActivity {
    static ArrayList <UneVille> packVilles;
    RecyclerView listeDesVilles;
    Hashtable<String, Bitmap> villesImg;
    ArrayList <String> alNomVilles, alNomRegion;
    EnsembleImages instImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_villes);

        //vars
        instImg = EnsembleImages.getInstance();
        packVilles = new ArrayList<>();
        villesImg = new Hashtable<String, Bitmap>();
        alNomRegion = new ArrayList<>();
        alNomVilles = new ArrayList<>();
        listeDesVilles = findViewById(R.id.listeDesVilles);

        // check si c'est la première fois que l'app roule, si oui alors init des valeurs de base.
        try {
            packVilles = recupVille();
        }
        catch (Exception e){
            if (savedInstanceState != null && savedInstanceState.getSerializable("srVille") != null) {
                packVilles = (ArrayList<UneVille>) savedInstanceState.getSerializable("srVille");
            }
            else{
                packVilles.add(new UneVille(
                        "Hawkesbury",
                        "Ontario",
                        "hawkesbury.jpg",
                        "La ville d’Hawkesbury située sur le bord de la rivière des Outaouais " +
                                "est une des plus francophones hors du Québec.",
                        "http://www.hawkesbury.ca"));

                packVilles.add(new UneVille(
                        "Lachute",
                        "Laurentides",
                        "lachute.jpg",
                        "La ville de Lachute est située au contrefort des Laurentides à " +
                                "environ 1 heure de route de Montréal.",
                        "http://www.ville.lachute.qc.ca"));

                packVilles.add(new UneVille(
                        "Sutton",
                        "Cantons-de-l'Est",
                        "sutton.jpg",
                        "La ville de Sutton est bien sûr connue pour sa montagne de ski mais " +
                                "plusieurs autres attractions s’y trouvent.",
                        "http://sutton.ca"));

                packVilles.add(new UneVille(
                        "Mont-Tremblant",
                        "Laurentides",
                        "mont_tremblant.jpg",
                        "La ville de Mont-Tremblant est composée de l’ancienne ville de " +
                                "St-Jovite regroupant de nombreux services et du village de " +
                                "Mont-Tremblant, paradis des sports 4 saisons.",
                        "https://villedemont-tremblant.qc.ca/"));

                packVilles.add(new UneVille(
                        "Lac-Ste-Marie",
                        "Outaouais",
                        "lac_ste_marie.jpg",
                        "La municipalité de Lac-Ste-Marie, bien qu’à l’écart des grands axes " +
                                "routiers, n’est qu’à une heure de route de Gatineau.",
                        "https://www.lac-sainte-marie.com/"));

                // sérialise les données de base
                try {
                    FileOutputStream fos = openFileOutput("fichier.ser", MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(packVilles);
                    instImg.setPackVilles(packVilles);
                    oos.close();
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }
        // Init Images
        instImg.setLesImages(this);
        villesImg = instImg.getImagesVilles();

        // Init Arraylist pour le recyclerview
        for(UneVille ville: packVilles){
            alNomVilles.add(ville.getNomVille());
            alNomRegion.add(ville.getRegionVille());
        }
        initRV ();
    }

    private void initRV (){
        RecyclerViewAdapter a = new RecyclerViewAdapter(alNomVilles, alNomRegion, villesImg, this);
        listeDesVilles.setAdapter(a);
        listeDesVilles.setLayoutManager(new LinearLayoutManager(this));
    }

    //methode pour lire les infos dans le ficher de sérialisation
    public ArrayList<UneVille> recupVille() throws Exception{
        ArrayList<UneVille> tempo = new ArrayList<>();
        FileInputStream fis = openFileInput("fichier.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        tempo = (ArrayList<UneVille>) ois.readObject();
        //initialise le arrayliste dans le modèle / EnsembleImages
        instImg.setPackVilles(tempo);
        ois.close();
        return tempo;
    }
}