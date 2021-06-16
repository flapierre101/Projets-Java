package f.l.tpfinal_c54;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class ConsultationVilles extends AppCompatActivity {

    CardView carte1, carte2;
    MotionLayout consultVille;
    ImageView photoVille;
    TextView nomVille, nomRegion, siteWeb, nomVille2, descVille;
    EnsembleImages instImg;
    Ecouteur ec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_villes);

        ec = new Ecouteur();
        instImg = EnsembleImages.getInstance();
        carte1 = findViewById(R.id.card_view);
        carte2 = findViewById(R.id.card_view2);
        consultVille = findViewById(R.id.consultVille);
        photoVille = findViewById(R.id.photoDeLaVille);
        nomVille = findViewById(R.id.nomDeLaVille);
        nomVille2 = findViewById(R.id.nomDeLaVille2);
        nomRegion = findViewById(R.id.nomDeLaRegion);
        siteWeb = findViewById(R.id.siteWebDeLaVille);
        descVille = findViewById(R.id.shortDesc);

        if (instImg.getImagesVilles().size() == 0){
            instImg.setLesImages(this);
        }
        // la clé dans le intent
        String key = String.valueOf(getIntent().getStringExtra("nom").toLowerCase());
        // l'index de la ville dans le arraylist
        int index = getIntent().getIntExtra("index", 0);
        // set les widgets
        photoVille.setImageBitmap(instImg.getImagesVilles().get(key));
        nomVille.setText(instImg.getPackVilles().get(index).getNomVille());
        nomVille2.setText(instImg.getPackVilles().get(index).getNomVille());
        nomRegion.setText(instImg.getPackVilles().get(index).getRegionVille());
        siteWeb.setText(instImg.getPackVilles().get(index).getSiteWeb());
        descVille.setText(instImg.getPackVilles().get(index).getShortDesc());

        // set listener pour motion
        carte1.setOnTouchListener(ec);
        carte2.setOnTouchListener(ec);
        // set listener pour site web
        siteWeb.setOnClickListener(ec);
    }

    private void openUrl() {
        // intent web
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(siteWeb.getText().toString()));
        startActivity(i);
    }

    private class Ecouteur implements View.OnTouchListener, View.OnClickListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // gestion motion
            if (view == carte1){
                consultVille.transitionToStart();
            }
            if (view == carte2){
                consultVille.transitionToEnd();
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (view == siteWeb){
                openUrl();
            }
        }
    }
}