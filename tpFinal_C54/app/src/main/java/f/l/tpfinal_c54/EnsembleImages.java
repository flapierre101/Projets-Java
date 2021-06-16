package f.l.tpfinal_c54;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Hashtable;
// Classe qui contient l'information en mémoire le temps que le programme roule
public class EnsembleImages extends AppCompatActivity {
    private static EnsembleImages inst;
    private Hashtable<String, Bitmap> imagesVilles;
    private ArrayList<UneVille> packVilles;
    private ContentResolver resolv;

    private EnsembleImages() {
        imagesVilles = new Hashtable<String, Bitmap>();
        packVilles = new ArrayList<>();
    }

    public static EnsembleImages getInstance() {
        if (inst == null) {
            inst = new EnsembleImages();
        }
        return inst;
    }

    // Méthode qui va chercher les images dans le "external storage" et les assignes à leur ville
    // selon le nom de fichier
    public void setLesImages(Activity a) {
        String tempoStr = "";
        resolv = a.getContentResolver();
        Bitmap tempoBitmap = null;
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] arg = {MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE};
        Cursor c = resolv.query(imgUri, arg, null, null, "_id");
        if (c != null) {
            while (c.moveToNext()) {

                Uri contentUri = ContentUris.withAppendedId(imgUri, c.getLong(0));
                try {
                    tempoBitmap = BitmapFactory.decodeFile("/storage/self/primary/Pictures/" + c.getString(1));
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                // pour les noms de fichier qui contiennent des charactères qui ne permettait pas
                // d'identifier la ville correctement. ex: mont_tremblant VS mont-tramblant
                tempoStr = c.getString(2);
                tempoStr = tempoStr.replaceAll("_", "-");
                imagesVilles.put(tempoStr, tempoBitmap);
            }
            c.close();
        }
    }

    public Hashtable<String, Bitmap> getImagesVilles() {
        return imagesVilles;
    }

    public ArrayList<UneVille> getPackVilles() {
        return packVilles;
    }

    public void setPackVilles(ArrayList<UneVille> packVilles) {
        this.packVilles = packVilles;
    }
}
