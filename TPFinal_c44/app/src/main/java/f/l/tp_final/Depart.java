package f.l.tp_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Depart extends AppCompatActivity {
    GestionBD instance;
    TextView textHighScore;
    Button demarrer;
    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depart);
        instance = GestionBD.getInstance(this);
        textHighScore = findViewById(R.id.textHighScore);
        textHighScore.setText(instance.retourTopScore());

        demarrer = findViewById(R.id.buttonStart);
        ec = new Ecouteur();
        demarrer.setOnClickListener(ec);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.fermerBD();
    }

    private class Ecouteur implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            demarrerPartie();
        }
    }

    public void demarrerPartie() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}