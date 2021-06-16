package f.l.tp01;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Player extends AppCompatActivity {
    ImageButton playPause, previous, next;
    SeekBar seekChanson;
    Chronometer chronoUp, timerDown;
    ImageView pochette;
    TextView nomChanson;
    Ecouteur ec;
    EnsembleChansons instanceEC;
    MusicService musicSrv;
    MediaPlayer mediaPlayer;
    boolean musicBound, chansonCheck = true;
    Intent playIntent;
    MusiqueConnection musicConnect;
    GestionDiffuseur gd;
    Chanson aJouer;
    int indexChanson, progress = 0, savedProgess = 0;
    SauverEtat sEtat = new SauverEtat();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        instanceEC = EnsembleChansons.getInstance();
        playPause = findViewById(R.id.playPause);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekChanson = findViewById(R.id.seekBarChanProgress);
        timerDown = findViewById(R.id.tempsRestant);
        timerDown.setCountDown(true); //chrono decroissant
        chronoUp = findViewById(R.id.tempsEcoule);
        pochette = findViewById(R.id.pochetteAlbum);
        nomChanson = findViewById(R.id.titreChanson);
        ec = new Ecouteur();

        // set ecouteurs
        playPause.setOnClickListener(ec);
        previous.setOnClickListener(ec);
        next.setOnClickListener(ec);
        seekChanson.setOnSeekBarChangeListener(ec);
        chronoUp.setOnChronometerTickListener(ec);

        // si la playlist de instanceEC est vide alors on vient de l'activité "intro" et donc on veut reprendre
        // une lecture précédente qui à été sérialisé
        if (instanceEC.getPlayList().size() == 0) {
            instanceEC.lesChansons(this, null);
            for (int i = 0; i < instanceEC.getPlayList().size(); i++) {
                if (instanceEC.getPlayList().get(i).getChansonId() == getIntent().getIntExtra("dernChanson", 0)){
                    indexChanson = i;
                }
            }
            savedProgess = getIntent().getIntExtra("progres", 0);
        }
        else {
            indexChanson = getIntent().getIntExtra("chanson", 0);
        }

        aJouer = instanceEC.getPlayList().get(indexChanson);

        updateChanson();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playIntent = new Intent(this, MusicService.class);
        musicConnect = new MusiqueConnection();
        bindService(playIntent, musicConnect, BIND_AUTO_CREATE);
    }

    private class Ecouteur implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Chronometer.OnChronometerTickListener{

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View view) {

            if (view.getId() == playPause.getId()){
                if (playPause.getTag().toString().equals("play")){
                    playPause.setBackground(getDrawable(R.drawable.pausev2));
                    playPause.setTag("pause");
                    // si la chanson vient d'être selectionné
                    if (chansonCheck){
                        musicSrv.choisirChanson(aJouer.getDuree(), (long) aJouer.getChansonId());
                        chansonCheck = false;
                    }
                    // si la chanson a été "pause" puis "play", on reprend à "progress"
                    if (progress > 0)
                        mediaPlayer.seekTo(progress);

                    mediaPlayer.start();
                    chronoUp.start();
                    timerDown.start();

                }
                else{
                    // si "Pause"
                    playPause.setBackground(getDrawable(R.drawable.playv2));
                    playPause.setTag("play");
                    progress = seekChanson.getProgress();
                    mediaPlayer.pause();
                    chronoUp.stop();
                    timerDown.stop();
                }
            }
            else if (view.getId() == previous.getId()){
                playPrevious();
            }
            else if (view.getId() == next.getId()){
                playNext();
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            // si la seekBar est changer par l'utilisateur, alors seekTo à la valeur de la seekBar
            if(b){
                mediaPlayer.seekTo(i);
                chronoUp.setBase(SystemClock.elapsedRealtime() - i);
                timerDown.setBase(timerDown.getBase() - chronoUp.getBase());
                progress = i;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            // le seekBar suit le chronoUp
            if (progress == 0)
                seekChanson.setProgress((int)(SystemClock.elapsedRealtime() - chronometer.getBase()));
            else
                seekChanson.setProgress(progress);

        }
    }

    private class MusiqueConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService(); //permet d’initialiser variable déclaréee
            mediaPlayer = musicSrv.getDiffuseur(); // permet d’initialiser variable déclarée

            gd = new GestionDiffuseur();
            mediaPlayer.setOnPreparedListener(gd);
            mediaPlayer.setOnCompletionListener(gd);
            mediaPlayer.setOnErrorListener(gd);
            mediaPlayer.setOnSeekCompleteListener(gd);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound=false;
        }
    }

    private class GestionDiffuseur implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            // En cas de retour sur une play list en sérialisation, on reprend ou était rendu la chanson
            if (savedProgess > 0){
                progress = savedProgess;
                savedProgess = 0;
                mediaPlayer.seekTo(progress);
                chronoUp.setBase(SystemClock.elapsedRealtime() - progress);
                timerDown.setBase(timerDown.getBase() - progress);
                seekChanson.setProgress(progress);
            }
            // sinon on prend du début
            else {
                progress = 0;
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
        }
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if (progress>0)
                playNext();

        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

            return false;
        }


        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicSrv.stopService(playIntent);
        sEtat.setChId(String.valueOf(aJouer.getChansonId()));
        sEtat.setSvProgress(String.valueOf(progress));
        sauvegarderObjet(sEtat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // j'ai ajouté des "configChanged" dans le manifest pour ne pas crasher l'app si on tourne l'app
        musicSrv.unbindService(musicConnect);
    }

    private void updateChanson(){
        // update la vue
        pochette.setImageBitmap(aJouer.getPochette());
        nomChanson.setText(aJouer.getChansonNom());
        chronoUp.setBase(SystemClock.elapsedRealtime());
        timerDown.setBase(SystemClock.elapsedRealtime() + aJouer.getDuree());
        seekChanson.setMax(aJouer.getDuree());

    }

    private void playNext(){
        // essaie d'aller à la prochaine chanson dans la playlist. si on est a la fin, on revient à la chanson 1 (index 0)
        try {
            mediaPlayer.pause();
            aJouer = instanceEC.getPlayList().get(indexChanson+=1);
        }
        catch (ArrayIndexOutOfBoundsException aioobe){
            aJouer = instanceEC.getPlayList().get(0);
            indexChanson = 0;

        }
        finally {
            // puis on démarre la chanson et on update la vue
            musicSrv.choisirChanson(aJouer.getDuree(), (long) aJouer.getChansonId());
            chansonCheck = true;
            updateChanson();

        }
    }

    private void playPrevious(){
        // même chose que la méthode "playNext" sauf pour aller en arrière dans la playlist
        try {
            mediaPlayer.pause();
            aJouer = instanceEC.getPlayList().get(indexChanson-1);
        }
        catch (ArrayIndexOutOfBoundsException aioobe){
            aJouer = instanceEC.getPlayList().get(instanceEC.getPlayList().size()-1);
            indexChanson = instanceEC.getPlayList().size()-1;
        }
        finally {
            musicSrv.choisirChanson(aJouer.getDuree(), (long) aJouer.getChansonId());
            chansonCheck = true;
            updateChanson();
        }
    }

    public void sauvegarderObjet (SauverEtat etat){
        // sérialisation d'ou on est rendu si l'on ferme l'application.
        try {
            FileOutputStream fos = openFileOutput("fichier.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(etat);
            oos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    // Sérialisation (suite)
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("etat", sEtat);
    }

    // pour ne pas aller dans "onDestroy" si l'on veux revenir au menu principal
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, Intro.class);

        startActivity(setIntent);
    }
}