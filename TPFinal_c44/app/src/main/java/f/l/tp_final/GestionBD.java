package f.l.tp_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GestionBD extends SQLiteOpenHelper {
    private static GestionBD instance;

    // methode de base pour un Singleton
    public static GestionBD getInstance(Context ctx) {
        if (instance == null) {
            instance = new GestionBD(ctx);
        }
        return instance;
    }

    private SQLiteDatabase database;

    public GestionBD(Context ctx) {
        super(ctx, "dbScore", null, 1);
        ouvrirBD();
    }

    @Override
    public void onCreate(SQLiteDatabase dbScore) {
        dbScore.execSQL("create table 'CartesScore' (_id INTEGER PRIMARY KEY AUTOINCREMENT, Score INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbScore, int i, int i1) {
        dbScore.execSQL("drop table CartesScore");
        onCreate(dbScore);
    }

    public void ajoutScore(Score sc, SQLiteDatabase dbScore) {
        ContentValues cv = new ContentValues();
        cv.put("Score", sc.getUnScore());
        dbScore.insert("CartesScore", null, cv);

    }

    public void ouvrirBD() {
        database = this.getWritableDatabase();
    }

    public void fermerBD() {
        database.close();
    }

    public String retourTopScore() {
        String rScore;
        Cursor c = database.rawQuery("SELECT Score FROM CartesScore", null);
        if (c.moveToFirst()) {
            rScore = c.getString(0);
        } else
            rScore = "--";
        return rScore;
    }
}
