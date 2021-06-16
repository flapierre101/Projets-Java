package f.l.tp01;

import java.io.Serializable;

// classe de base pour sérialiser. Permet de sauvegarder la chanson qui jouait lorsque l'app c'est fermé
public class SauverEtat implements Serializable {
    private String chId, svProgress;

    public void setChId(String chId) {
        this.chId = chId;
    }

    public String getChId() {
        return chId;
    }

    public void setSvProgress(String svProgress) {
        this.svProgress = svProgress;
    }

    public String getSvProgress() {
        return svProgress;
    }
}
