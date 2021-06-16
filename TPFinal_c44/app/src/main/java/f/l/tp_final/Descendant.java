package f.l.tp_final;

public class Descendant {
    private int valeurActu;

    public Descendant() {
        valeurActu = 98;
    }

    public boolean calcValidation(int val) {
        boolean temp = false;
        if (val < valeurActu || val == valeurActu + 10) {
            valeurActu = val;
            temp = true;
        }
        return temp;
    }

    public int getValeurActu() {
        return valeurActu;
    }
}
