package f.l.tp01;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Vector;

public class EnsembleChansons {

    private Vector<Chanson> playList;
    private ContentResolver resolv;

    //singleton
    private static EnsembleChansons instance;

    public static EnsembleChansons getInstance(){
        if (instance == null){
            instance = new EnsembleChansons();
        }
            return instance;
    }

    private EnsembleChansons(){
        playList = new Vector<Chanson>();
    }

    /*public Vector<Chanson> chansonCinqEtPlus(Activity a){
        resolv = a.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri imageUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Vector<Chanson> resultat = new Vector<Chanson>();
        Chanson tempo;
        Bitmap pochette = null;

        Cursor musicCursor = resolv.query(musicUri, new String[]{"_id", "duration", "artist", "title", "album"},"duration > 5000", null, "duration");

        while(musicCursor.moveToNext()){
            tempo = new Chanson((int)musicCursor.getLong(0), musicCursor.getInt(1),null, musicCursor.getString(2), musicCursor.getString(3), musicCursor.getString(4));

            Cursor pochetteCursor = resolv.query(imageUri, new String[]{"album_id", "album"}, "album=?", new String[]{musicCursor.getString(4)}, null);

            if (pochetteCursor != null && pochetteCursor.moveToFirst()){
                long id_de_Lalbum = pochetteCursor.getLong(0);

                Uri contentUri = ContentUris.withAppendedId(imageUri, id_de_Lalbum);

                try {
                    pochette = resolv.loadThumbnail(contentUri, new Size(640, 480), null);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }

                tempo.setPochette(pochette);
                pochetteCursor.close();
            }

            resultat.add(tempo);
        }
        musicCursor.close();
        return resultat;
    }*/

        // pour la listview des artistes
    public void lesArtistes(Activity a){
        playList.removeAll(playList); //vide la playlist avant de faire une nouvelle requete

        resolv = a.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Chanson tempo;
        Cursor musicCursor = resolv.query(musicUri, new String[]{"_id", "duration", "artist", "title", "album"},null, null, "artist");

        while(musicCursor.moveToNext()){
            tempo = new Chanson((int)musicCursor.getLong(0), musicCursor.getInt(1),null, musicCursor.getString(2), musicCursor.getString(3), musicCursor.getString(4));

            playList.add(tempo);
        }
        Log.i("ContenuVecteur:", String.valueOf(playList));
        musicCursor.close();

    }

    // pour la list view de toutes les chansons OU toutes les chansons d<un artiste en particulier avec le deuxieme argument qui peu etre null
    public void lesChansons(Activity a, @Nullable String artiste){
        playList.removeAll(playList); //vide la playlist avant de faire une nouvelle requete

        resolv = a.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri imageUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor;
        Chanson tempo;
        Bitmap pochette = null;

        if (artiste != null) {
            musicCursor = resolv.query(musicUri, new String[]{"_id", "duration", "artist", "title", "album"}, "artist = ?", new String[]{artiste}, "title");
        }
        else{
            musicCursor = resolv.query(musicUri, new String[]{"_id", "duration", "artist", "title", "album"}, null, null, "_id");
        }

        while(musicCursor.moveToNext()){
            tempo = new Chanson((int)musicCursor.getLong(0), musicCursor.getInt(1),null, musicCursor.getString(2), musicCursor.getString(3), musicCursor.getString(4));
            Cursor pochetteCursor = resolv.query(imageUri, new String[]{"album_id", "album"}, "album=?", new String[]{musicCursor.getString(4)}, null);

            if (pochetteCursor != null && pochetteCursor.moveToFirst()) {
                long id_de_Lalbum = pochetteCursor.getLong(0);

                Uri contentUri = ContentUris.withAppendedId(imageUri, id_de_Lalbum);

                try {
                    pochette = resolv.loadThumbnail(contentUri, new Size(150, 150), null);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                tempo.setPochette(pochette);
                pochetteCursor.close();
            }

            playList.add(tempo);
        }
        musicCursor.close();

    }

    public Vector<Chanson> getPlayList() {
        return playList;
    }
}
