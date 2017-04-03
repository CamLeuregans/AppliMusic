package secmob.applimusic;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.SeekBar;

/**
 * Created by Kiki on 28/03/2017.
 */

public class MonThread extends AsyncTask<Void, Integer, Void> {
    private Context cont;
    private SeekBar sb;
    private MediaPlayer mediaPlayer;


    public MonThread(Context cont, SeekBar sb, MediaPlayer mediaPlayer) {
        this.cont = cont;
        this.sb = sb;
        this.mediaPlayer = mediaPlayer;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mediaPlayer.start();
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        sb.setProgress(values[0]);
    }


    @Override
    protected Void doInBackground(Void... voids) {
        while (mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration() && !this.isCancelled()) {
            publishProgress(mediaPlayer.getCurrentPosition());
        }
        return null;
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        mediaPlayer.stop();
        sb.setProgress(0);
    }
}