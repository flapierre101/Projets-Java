package f.l.tpfinal_c54;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button openListe, ajoutVille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openListe = findViewById(R.id.buttonOpenListe);
        ajoutVille = findViewById(R.id.ajoutVille);

        // Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
            if ( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);


        openListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListeVilles();
            }
        });

        ajoutVille.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToAjoutVilles();
            }
        });
    }

    private void goToListeVilles(){
        Intent i = new Intent(this, ListeVilles.class);
        startActivity(i);
    }
    private void goToAjoutVilles(){
        Intent i = new Intent(this, EnregistrerNouvelleVille.class);
        startActivity(i);
    }
}
