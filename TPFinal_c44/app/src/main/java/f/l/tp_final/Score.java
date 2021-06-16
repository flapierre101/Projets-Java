package f.l.tp_final;

public class Score {
    private int unScore, trancheUn, trancheDeux, trancheTrois, trancheQuatre;

    public Score() {
        unScore = 0;
        trancheUn = 5;
        trancheDeux = 10;
        trancheTrois = 15;
        trancheQuatre = 20;
    }

    public int getUnScore() {
        return unScore;
    }

    public void calcScore(int a) {
        switch (a) {
            case 1:
                unScore += trancheUn;
                break;
            case 2:
                unScore += trancheDeux;
                break;
            case 3:
                unScore += trancheTrois;
                break;
            case 4:
                unScore += trancheQuatre;
                break;
        }

    }
}
