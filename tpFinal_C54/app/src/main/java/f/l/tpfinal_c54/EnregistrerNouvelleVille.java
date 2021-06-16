package f.l.tpfinal_c54;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EnregistrerNouvelleVille extends AppCompatActivity {
    EditText nomVille, nomRegion, adrWeb, description;
    ImageView uploadImg;
    Button buttonSave;
    UneVille sVille;
    Ecouteur ec;
    EnsembleImages instImg;
    ArrayList <UneVille> packVilles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrer_nouvelle_ville);

        instImg = EnsembleImages.getInstance();
        nomVille = findViewById(R.id.editNomVille);
        nomRegion = findViewById(R.id.editTextNomRegion);
        adrWeb = findViewById(R.id.editTextAdrWeb);
        description = findViewById(R.id.description);
        uploadImg = findViewById(R.id.uploadImage);
        buttonSave = findViewById(R.id.buttonSave);
        ec = new Ecouteur();
        buttonSave.setOnClickListener(ec);
        uploadImg.setOnClickListener(ec);
        packVilles = instImg.getPackVilles();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 444) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                Bitmap imgBitmap = (Bitmap) extra.get("data");
                uploadImg.setImageBitmap(imgBitmap);
            }
        }
    }

    private void prendrePhoto() {
        // de base ceci permet de prendre la photo, mais en esseyant de l'enregistrer sous un nom
        // spécifique cela cessait de fonctionner. J'ai laissé le code pour référence futur

        Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//        File photoFile = null;
//        try {
//            photoFile = createImageFile();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        if (photoFile != null) {
//            Uri photoURI = FileProvider.getUriForFile(this,
//                    "com.example.android.fileprovider",
//                    photoFile);
//            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//        }
            startActivityForResult(i, 444);
    }

    public void sauvegarderVille (ArrayList<UneVille> villes){
        // sérialisation
        try {
            FileOutputStream fos = openFileOutput("fichier.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(villes);
            oos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Sérialisation (suite)
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("etat", sVille);
    }

    private class Ecouteur implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (view.getId() == buttonSave.getId()){
                // différentes façon de de construire un objet "UneVille". Voir constructeur classe
                // "UneVille"
                if (nomVille.getText().toString().isEmpty() || nomRegion.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Information manquante \n Veuillez remplir au moins le nom et la région" , Toast.LENGTH_SHORT).show();
                }
                else if (adrWeb.equals("") && description.equals("")) {
                    sVille = new UneVille(nomVille.getText().toString(), nomRegion.getText().toString(), nomVille.getText().toString() + ".jpg");
                }
                else if (description.equals("")){
                    sVille = new UneVille(nomVille.getText().toString(), nomRegion.getText().toString(), nomVille.getText().toString() + ".jpg", adrWeb.getText().toString());
                }
                else{
                    sVille = new UneVille(nomVille.getText().toString(), nomRegion.getText().toString(), nomVille.getText().toString() + ".jpg", adrWeb.getText().toString(), description.getText().toString());
                }
                if (sVille != null) {
                    packVilles.add(sVille);
                    sauvegarderVille(packVilles);
                }
            }
            else if (view.getId() == uploadImg.getId()){
                // intent pour appareil photo
                prendrePhoto();

            }
        }
    }

    // voir commentaire plus haut sur "prendrePhoto()". Essentiellement, cette méthode permet de construire et surtout nommé le fichier
    // dans lequel le bitmap sera sauvegarder
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        File image = null;
//        if (!nomVille.getText().toString().isEmpty()){
//            String imageFileName = nomVille.getText().toString();
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//            );
//        }
//        return image;
//    }
}