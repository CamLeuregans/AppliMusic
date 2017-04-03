package secmob.applimusic;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final SeekBar sbProg = (SeekBar) findViewById(R.id.seekBar_avance);
        sbProg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button start = (Button) findViewById(R.id.button_start);
        final MonThread[] monT = new MonThread[1];
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (mediaPlayer == null && monT[0] == null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.girl);
                    mediaPlayer.setLooping(false);
                    sbProg.setMax(mediaPlayer.getDuration());
                    monT[0] = new MonThread (getApplicationContext(), sbProg, mediaPlayer);
                    monT[0].execute();
                }
            }
        });


        Button stop = (Button) findViewById(R.id.button_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (monT[0] != null) {
                    monT[0].cancel(true);
                    monT[0] = null;
                }
                sbProg.setProgress(0);
            }
        });


        Button pause = (Button) findViewById(R.id.button_pause);
        final boolean[] tabPause = {false};
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tP = mediaPlayer.getCurrentPosition();
                if (tabPause[0]){
                    mediaPlayer.seekTo(tP);
                    mediaPlayer.start();
                    tabPause[0] = false;
                } else {
                    mediaPlayer.pause();
                    tP = mediaPlayer.getCurrentPosition();
                    tabPause[0] =  true;
                }
            }
        });


        //Pour gerer le son du téléphone : audioManager
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //SeekBar pour gérer le son
        SeekBar sbSon = (SeekBar) findViewById(R.id.seekBar_volume);

        //Ici, on choisi que le son ne soit pas trop fort au début : 1/3 de la bar
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC/3), AudioManager.FLAG_SHOW_UI);

        //Ici, on défini que le max de la SeekBar = maximum du son
        sbSon.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        //Ici, on fait commencer la seekbar au meme niveau qu'on a défini le son
        sbSon.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/3);

        sbSon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
