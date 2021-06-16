package f.l.tp_final;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    TextView carteJeu1, carteJeu2, carteJeu3, carteJeu4, carteJeu5, carteJeu6, carteJeu7, carteJeu8;
    TextView ascUn, ascDeux, descUn, descDeux, nbCartesRestant, scoreTotal;
    ConstraintLayout constraintAscUn, constraintAscDeux, constraintDescUn, constraintDescDeux;
    LinearLayout lesCartes, lesCartesUn, lesCartesDeux;
    Ecouteur ec;
    boolean playPos = true;
    Cartes paquetCarte;
    Ascendant pileAscUn, pileAscDeux;
    Descendant pileDescUn, pileDescDeux;
    Score scorePartie;
    int cartesRestantes;
    GestionBD instance;
    Chronometer chrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ouverture de la BD
        instance = GestionBD.getInstance(this);

        // Création des objets
        paquetCarte = new Cartes();
        ec = new Ecouteur();
        pileAscUn = new Ascendant();
        pileAscDeux = new Ascendant();
        pileDescUn = new Descendant();
        pileDescDeux = new Descendant();
        scorePartie = new Score();

        // création et démarage du chrono
        chrono = findViewById(R.id.timer);
        chrono.start();

        // Init de la vue
        lesCartes = findViewById(R.id.lesCartes);
        lesCartesUn = findViewById(R.id.lesCartesUn);
        lesCartesDeux = findViewById(R.id.lesCartesDeux);
        carteJeu1 = findViewById(R.id.carteJeu1);
        carteJeu1.setText(paquetCarte.getCarteAJouer()[0]);
        carteJeu2 = findViewById(R.id.carteJeu2);
        carteJeu2.setText(paquetCarte.getCarteAJouer()[1]);
        carteJeu3 = findViewById(R.id.carteJeu3);
        carteJeu3.setText(paquetCarte.getCarteAJouer()[2]);
        carteJeu4 = findViewById(R.id.carteJeu4);
        carteJeu4.setText(paquetCarte.getCarteAJouer()[3]);
        carteJeu5 = findViewById(R.id.carteJeu5);
        carteJeu5.setText(paquetCarte.getCarteAJouer()[4]);
        carteJeu6 = findViewById(R.id.carteJeu6);
        carteJeu6.setText(paquetCarte.getCarteAJouer()[5]);
        carteJeu7 = findViewById(R.id.carteJeu7);
        carteJeu7.setText(paquetCarte.getCarteAJouer()[6]);
        carteJeu8 = findViewById(R.id.carteJeu8);
        carteJeu8.setText(paquetCarte.getCarteAJouer()[7]);

        // Init variable interne du nombre de cartes restantes
        cartesRestantes = paquetCarte.getLesCartes().size();
        // TextView du nombre de cartes restantes
        nbCartesRestant = findViewById(R.id.nbCartes);
        nbCartesRestant.setText(String.valueOf(cartesRestantes));

        scoreTotal = findViewById(R.id.scoreTotal);
        scoreTotal.setText(String.valueOf(scorePartie.getUnScore()));

        ascUn = findViewById(R.id.ascUn);
        ascDeux = findViewById(R.id.ascDeux);
        descUn = findViewById(R.id.descUn);
        descDeux = findViewById(R.id.descDeux);
        constraintAscUn = findViewById(R.id.constraintAscUn);
        constraintAscDeux = findViewById(R.id.constraintAscDeux);
        constraintDescUn = findViewById(R.id.constraintDescUn);
        constraintDescDeux = findViewById(R.id.constraintDescDeux);

        constraintAscUn.setOnDragListener(ec);
        constraintAscDeux.setOnDragListener(ec);
        constraintDescUn.setOnDragListener(ec);
        constraintDescDeux.setOnDragListener(ec);

        for (int i = 0; i < lesCartesUn.getChildCount(); i++) {
            TextView lay = (TextView) lesCartesUn.getChildAt(i);
            lay.setOnTouchListener(ec);
            TextView lay2 = (TextView) lesCartesDeux.getChildAt(i);
            lay2.setOnTouchListener(ec);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.fermerBD();
    }

    private class Ecouteur implements View.OnDragListener, View.OnTouchListener {

        Drawable select = ContextCompat.getDrawable(MainActivity.this, R.drawable.contour_select);
        Drawable normal = ContextCompat.getDrawable(MainActivity.this, R.drawable.contour);

        // Variable de contrôle pour vérification du déplacement
        boolean calcCheck = false;
        // Vector qui retient quels textviews ont été retiré du layout
        Vector<TextView> tempTextViews = new Vector<>();

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    view.setBackground(select);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    view.setBackground(normal);
                    break;

                case DragEvent.ACTION_DROP:
                    TextView carte = (TextView) dragEvent.getLocalState();
                    LinearLayout origine = (LinearLayout) carte.getParent();

                    ConstraintLayout destination = (ConstraintLayout) view;
                    TextView textDestination = (TextView) destination.getChildAt(0);

                    // variable temporaire pour calculer le score
                    int pointage;

                    switch (String.valueOf(destination.getTag())) {
                        // Dans quel layout la carte a été déposé
                        case "asc1":
                            // méthode de vérification si le déplacement est possible
                            calcCheck = pileAscUn.calcValidation(Integer.parseInt(String.valueOf(tempTextViews.lastElement().getText())));
                            if (calcCheck) {
                                textDestination.setText(String.valueOf(pileAscUn.getValeurActu()));
                                origine.removeView(carte);
                                carte.setVisibility(View.INVISIBLE);
                                // calcul du score
                                pointage = paquetCarte.scoreHelper();
                                scorePartie.calcScore(pointage);
                            }
                            break;
                        case "asc2":
                            calcCheck = pileAscDeux.calcValidation(Integer.parseInt(String.valueOf(tempTextViews.lastElement().getText())));
                            if (calcCheck) {
                                textDestination.setText(String.valueOf(pileAscDeux.getValeurActu()));
                                origine.removeView(carte);
                                carte.setVisibility(View.INVISIBLE);
                                pointage = paquetCarte.scoreHelper();
                                scorePartie.calcScore(pointage);
                            }
                            break;
                        case "desc1":
                            calcCheck = pileDescUn.calcValidation(Integer.parseInt(String.valueOf(tempTextViews.lastElement().getText())));
                            if (calcCheck) {
                                textDestination.setText(String.valueOf(pileDescUn.getValeurActu()));
                                origine.removeView(carte);
                                carte.setVisibility(View.INVISIBLE);
                                pointage = paquetCarte.scoreHelper();
                                scorePartie.calcScore(pointage);
                            }
                            break;
                        case "desc2":
                            calcCheck = pileDescDeux.calcValidation(Integer.parseInt(String.valueOf(tempTextViews.lastElement().getText())));
                            if (calcCheck) {
                                textDestination.setText(String.valueOf(pileDescDeux.getValeurActu()));
                                origine.removeView(carte);
                                carte.setVisibility(View.INVISIBLE);
                                pointage = paquetCarte.scoreHelper();
                                scorePartie.calcScore(pointage);
                            }
                            break;

                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    view.setBackground(normal);

                    // remplacement de carte dans la vue lorsqu'il en manque 2
                    if (lesCartesUn.getChildCount() + lesCartesDeux.getChildCount() <= 6) {
                        // layout du haut
                        if (lesCartesUn.getChildCount() == 2) {
                            tempTextViews.firstElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesUn.addView(tempTextViews.firstElement());
                            tempTextViews.firstElement().setVisibility(View.VISIBLE);

                            tempTextViews.lastElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesUn.addView(tempTextViews.lastElement());
                            tempTextViews.lastElement().setVisibility(View.VISIBLE);
                        }
                        // layout du bas
                        else if (lesCartesDeux.getChildCount() == 2) {
                            tempTextViews.firstElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesDeux.addView(tempTextViews.firstElement());
                            tempTextViews.firstElement().setVisibility(View.VISIBLE);

                            tempTextViews.lastElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesDeux.addView(tempTextViews.lastElement());
                            tempTextViews.lastElement().setVisibility(View.VISIBLE);
                        }
                        // les deux layouts
                        else {

                            tempTextViews.firstElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesUn.addView(tempTextViews.firstElement());
                            tempTextViews.firstElement().setVisibility(View.VISIBLE);

                            tempTextViews.lastElement().setText(paquetCarte.ajouterNouvelCarte());
                            lesCartesDeux.addView(tempTextViews.lastElement());
                            tempTextViews.lastElement().setVisibility(View.VISIBLE);

                        }
                        // vide le vecteur car le contenu n'est plus utile pour le prochain tour
                        tempTextViews.removeAll(tempTextViews);
                    }
                    // mise-à-jour du nb de cartes et du score
                    cartesRestantes = paquetCarte.getLesCartes().size();
                    nbCartesRestant.setText(String.valueOf(cartesRestantes));
                    scoreTotal.setText(String.valueOf(scorePartie.getUnScore()));

                    // Met à jour les valeurs dans l'objet cartes

                    for (int i = 0; i < lesCartesUn.getChildCount(); i++) {
                        TextView tempo = (TextView) lesCartesUn.getChildAt(i);
                        String tempoStr = String.valueOf(tempo.getText());
                        int tempoInt = Integer.parseInt(String.valueOf(tempo.getTag()));
                        paquetCarte.setUneCarteAJouer(tempoStr, tempoInt);
                    }
                    for (int i = 0; i < lesCartesDeux.getChildCount(); i++) {
                        TextView tempo = (TextView) lesCartesDeux.getChildAt(i);
                        String tempoStr = String.valueOf(tempo.getText());
                        int tempoInt = Integer.parseInt(String.valueOf(tempo.getTag()));
                        paquetCarte.setUneCarteAJouer(tempoStr, tempoInt);
                    }
                    // Méthode qui vérifie s'il reste des coups possible
                    playPos = paquetCarte.playCheck(pileAscUn.getValeurActu(), pileAscDeux.getValeurActu(), pileDescUn.getValeurActu(), pileDescDeux.getValeurActu());
                    finirPartie();
                    break;
            }

            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(null, shadowBuilder, view, 0);
            // prends le textview selectionné et le met temporairement dans le vector
            tempTextViews.add((TextView) view);

            return true;
        }

    }

    public void finirPartie() {
        if (!playPos) {
            Intent i = new Intent(this, Conclusion.class);
            instance.ajoutScore(scorePartie, instance.getReadableDatabase());
            chrono.stop();
            startActivity(i);
        }
    }


}