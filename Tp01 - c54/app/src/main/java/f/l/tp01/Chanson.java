package f.l.tp01;

import android.graphics.Bitmap;


public class Chanson {
    private int chansonId;
    private int duree;
    private Bitmap pochette;
    private String artiste, chansonNom, album;

    public Chanson(int chansonId, int duree, Bitmap pochette, String artiste, String chansonNom, String album){
        this.chansonId = chansonId;
        this.duree = duree;
        this.pochette = pochette;
        this.artiste = artiste;
        this.chansonNom = chansonNom;
        this.album = album;

    }

    public String getChansonNom() {
        return chansonNom;
    }

    public void setPochette(Bitmap pochette) {
        this.pochette = pochette;
    }

    public int getChansonId() {
        return chansonId;
    }

    public int getDuree() {
        return duree;
    }

    public Bitmap getPochette() {
        return pochette;
    }

    public String getArtiste() {
        return artiste;
    }


}
