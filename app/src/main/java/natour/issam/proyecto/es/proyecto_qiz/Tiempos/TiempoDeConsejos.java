package natour.issam.proyecto.es.proyecto_qiz.Tiempos;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

import natour.issam.proyecto.es.proyecto_qiz.R;


public class TiempoDeConsejos extends AsyncTask<Void, Integer, Void> {

    int myProgress;
    TextView time;
    Activity activity;
    public  TiempoDeConsejos(Activity activity){
      this.activity=activity;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
         time = (TextView) activity.findViewById(R.id.textime);
        myProgress = 0;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub


        activity.runOnUiThread(new Runnable() {
            public void run() {
                CountDownTimer cT =  new CountDownTimer(3600000, 1000) {

                    public void onTick(long millisUntilFinished) {


                        String v = String.format("%02d", millisUntilFinished/60000);
                        int va = (int)( (millisUntilFinished%60000)/1000);
                        time.setText( v+":"+String.format("%02d",va));
                    }

                    public void onFinish() {
                        time.setText("done!");
                    }


                };
                cT.start();
            }
        });



        return null;
        }



    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
    }

}



