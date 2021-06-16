package f.l.tp_final;

import java.util.ArrayList;

public class Cartes {

    private ArrayList<String> lesCartes;
    private String[] carteAJouer;

    public Cartes() {
        lesCartes = new ArrayList<>();
        carteAJouer = new String[8];

        // création de carte de 2 à 97
        for (int i = 2; i < 98; i++) {
            lesCartes.add(String.valueOf(i));
        }
        // donne des valeurs au 8 premières carte puis enlève ces valeurs du array pour ne pas les ravoir
        for (int i = 0; i < carteAJouer.length; i++) {
            carteAJouer[i] = lesCartes.get(unIntRandom());
            lesCartes.remove(carteAJouer[i]);
        }


    }

    public String[] getCarteAJouer() {
        return carteAJouer;
    }

    public ArrayList<String> getLesCartes() {
        return lesCartes;
    }

    public void setUneCarteAJouer(String uneCarte, int index) {
        this.carteAJouer[index] = uneCarte;
    }

    public String ajouterNouvelCarte() {
        String temp;
        if (!lesCartes.isEmpty()) {
            temp = lesCartes.get(unIntRandom());
            lesCartes.remove(temp);
        } else
            temp = "";

        return temp;
    }

    public int unIntRandom() {
        return (int) (Math.random() * lesCartes.size());
    }

    // a = valeur de la pile ascendante 1 , b = valeur de la pile ascendante 2, c = valeur de la pile descendante 1, d = valeur de la pile descendante 2
    public boolean playCheck(int a, int b, int c, int d) {
        int tempo = 0;
        for (String s : carteAJouer) {
            int intVal = Integer.parseInt(s);
            if (intVal > a || intVal > b || intVal < c || intVal < d || intVal == a - 10 || intVal == b - 10 || intVal == c + 10 || intVal == d + 10) {
                tempo++;
                break;
            }
        }
        // si aucun coups possible tempo == 0
        return tempo != 0;

    }

    public int scoreHelper() {
        int tempo = 0;
        if (lesCartes.size() <= 88 && lesCartes.size() >= 66) {
            tempo = 1;
        } else if (lesCartes.size() <= 65 && lesCartes.size() >= 45) {
            tempo = 2;
        } else if (lesCartes.size() <= 44 && lesCartes.size() >= 22) {
            tempo = 3;
        } else if (lesCartes.size() <= 21 && lesCartes.size() >= 0) {
            tempo = 4;
        }

        return tempo;
    }
}
