package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.CargarTests;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;
import natour.issam.proyecto.es.proyecto_qiz.Tiempos.Premios_Aciertos;
import natour.issam.proyecto.es.proyecto_qiz.monstruos.Habilidades;


public class Intent_Test_Juego extends ActionBarActivity {
    MetodosSqlite metodosSqlite;
    private int TIEMPOLIMITE=15;
    private int TIEMPOAUMENTADO=1;

    TextView Pregunta;
    TextView textoconsejo;
    ImageView imagenPregunta;
    Button a, b, c, d;
    private final int NOTIFICATION_ID = 1010;

    ArrayList<Test> Tests;
    BackgroundAsyncTask tiempo;
    //cosas a guardar
    int idTest=0;
    int testHechos=0;
    int numerodeayudasfiftyfifty=0;
    int numerodeayudasconsejos=0;
    int numerodeayudassumartiempo=0;
    int monedastotales=0;
    int diamantestotales=0;
    float puntuaciontotal=0;
    float experienciatotal=0;

    ProgressBar barratime;
    Button sumartiempo;
    Button botonconsejo;
    Button fiftyfifty;
    boolean Cancelar;
    boolean issumartiempo;
    Activity activity;
    Context context;
    int myProgress;

    /* Comprobar variables    */
    boolean istimeused=false;
    boolean isfiftyfiftyused=false;
    boolean isconsejoused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent__test__juego);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///

        issumartiempo=false;
        Pregunta = (TextView) findViewById(R.id.textoPregunta12345);
        textoconsejo = (TextView) findViewById(R.id.textoconsejo);
        imagenPregunta = (ImageView) findViewById(R.id.imagenmipregunta);
        sumartiempo= (Button) findViewById(R.id.buttonsumartiempo);
        botonconsejo = (Button) findViewById(R.id.botonconejo);
        fiftyfifty = (Button) findViewById(R.id.botonfiftyfifty);

        a = (Button) findViewById(R.id.botonpreguntaAA);
        b = (Button) findViewById(R.id.botonpreguntaB);
        c = (Button) findViewById(R.id.botonpreguntaC);
        d = (Button) findViewById(R.id.botonpreguntaD);

        context=getBaseContext();
        metodosSqlite = new MetodosSqlite(context);
        sumartiempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),3)>0){
                    sumartiempo.setEnabled(false);
                    barratime.setMax(TIEMPOLIMITE + TIEMPOAUMENTADO);
                    issumartiempo = true;
                    numerodeayudassumartiempo+=1;
                    metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 3);
                    istimeused=true;
                    cargarHabilidades();
                }else {
                    Toast.makeText(context, "NO TIENES MAS TIME", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonconsejo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),1)>0) {
                    cargarConsejo(idTest);
                    botonconsejo.setEnabled(false);
                    isconsejoused=true;
                    numerodeayudasconsejos+=1;
                    metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 1);
                    cargarHabilidades();
                }else {
                    Toast.makeText(context, "NO TE QUEDAN CONSEJOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),2)>0){
                    ayudamitadrespuesta(idTest);
                    metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 2);
                    numerodeayudasfiftyfifty+=1;
                    isfiftyfiftyused=true;
                    cargarHabilidades();
                }else{
                    Toast.makeText(context, "NO TE QUEDAN FIFTYFIFTY", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cargarvariables();
        Intent intent = getIntent();
        Tests= cargarTests();



        cargarpregunta(idTest);
        activity= this;
        barratime = (ProgressBar)findViewById(R.id.progressBar);
        barratime.setProgress(0);
        barratime.setMax(15);
        tiempo = new BackgroundAsyncTask(activity);
        tiempo.execute();

        Cancelar=false;

        cargarHabilidades();

    }

    private void parartiempo(){
        Cancelar=true;
        tiempo.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        parartiempo();
    }



public ArrayList<Test> cargarTests(){
    ArrayList<Test> mistest = null;
    CargarTests cargarTests = new CargarTests(this);
    cargarTests.execute();
    try {
        mistest = cargarTests.get();

    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
    return  mistest;
}
    public void cargarvariables(){
        Bundle b = getIntent().getExtras();
        idTest = b.getInt("id");
        testHechos=b.getInt("testHechos");
        numerodeayudasfiftyfifty=b.getInt("numerodeayudasfiftyfifty");
        numerodeayudasconsejos=b.getInt("numerodeayudasconsejos");
        numerodeayudassumartiempo=b.getInt("numerodeayudassumartiempo");
        monedastotales=b.getInt("monedastotales");
        diamantestotales=b.getInt("diamantestotales");
        puntuaciontotal=b.getFloat("puntuaciontotal");
        experienciatotal=b.getFloat("experienciatotal");

        Log.i("puntos",puntuaciontotal+"");
        Log.i("experienciatotal",experienciatotal+"");
        Log.i("monedastotales",monedastotales+"");
        Log.i("numerodeayudasfiftyfifty",numerodeayudasfiftyfifty+"");

    }

    public void reiniciarActivityOk(){

    if(idTest<4) {
    Intent intentTes = new Intent(context, Intent_Test_Juego.class);
    intentTes.putExtra("id", idTest + 1);
    intentTes.putExtra("testHechos", testHechos + 1);
    intentTes.putExtra("numerodeayudasfiftyfifty", numerodeayudasfiftyfifty);
    intentTes.putExtra("numerodeayudassumartiempo", numerodeayudassumartiempo);
    intentTes.putExtra("numerodeayudasconsejos", numerodeayudasconsejos);
    intentTes.putExtra("monedastotales", monedastotales + Tests.get(idTest).getMonedas());
    intentTes.putExtra("diamantestotales", diamantestotales + Tests.get(idTest).getDiamantes());
    intentTes.putExtra("puntuaciontotal", puntuaciontotal + Tests.get(idTest).getPuntuacion());
    intentTes.putExtra("experienciatotal", experienciatotal + Tests.get(idTest).getExperiencia());
    finish();
    startActivity(intentTes);
    }else{
        Intent intentTes = new Intent(context, Premios_Aciertos.class);
        intentTes.putExtra("id", idTest );
        intentTes.putExtra("testHechos", testHechos );
        intentTes.putExtra("numerodeayudasfiftyfifty", numerodeayudasfiftyfifty);
        intentTes.putExtra("numerodeayudassumartiempo", numerodeayudassumartiempo);
        intentTes.putExtra("numerodeayudasconsejos", numerodeayudasconsejos);
        intentTes.putExtra("monedastotales", monedastotales + Tests.get(idTest).getMonedas());
        intentTes.putExtra("diamantestotales", diamantestotales + Tests.get(idTest).getDiamantes());
        intentTes.putExtra("puntuaciontotal", puntuaciontotal + Tests.get(idTest).getPuntuacion());
        intentTes.putExtra("experienciatotal", experienciatotal + Tests.get(idTest).getExperiencia());
        finish();
        startActivity(intentTes);
    }
    }

    public void reiniciarActivitynoOk(){

        if(idTest<4) {
            Intent intentTes = new Intent(context, Intent_Test_Juego.class);

            intentTes.putExtra("id", idTest+1 );


            intentTes.putExtra("testHechos", testHechos+1);
            intentTes.putExtra("numerodeayudasfiftyfifty", numerodeayudasfiftyfifty);
            intentTes.putExtra("numerodeayudassumartiempo", numerodeayudassumartiempo);
            intentTes.putExtra("numerodeayudasconsejos", numerodeayudasconsejos);
            intentTes.putExtra("monedastotales", monedastotales + 0);
            intentTes.putExtra("diamantestotales", diamantestotales + 0);
            intentTes.putExtra("puntuaciontotal", puntuaciontotal + 0);
            intentTes.putExtra("experienciatotal", experienciatotal + 0);
            finish();
            startActivity(intentTes);
        }else{
            Intent intentTes = new Intent(context, Premios_Aciertos.class);
            intentTes.putExtra("id", idTest );
            intentTes.putExtra("testHechos", testHechos );
            intentTes.putExtra("numerodeayudasfiftyfifty", numerodeayudasfiftyfifty);
            intentTes.putExtra("numerodeayudassumartiempo", numerodeayudassumartiempo);
            intentTes.putExtra("numerodeayudasconsejos", numerodeayudasconsejos);
            intentTes.putExtra("monedastotales", monedastotales + Tests.get(idTest).getMonedas());
            intentTes.putExtra("diamantestotales", diamantestotales + Tests.get(idTest).getDiamantes());
            intentTes.putExtra("puntuaciontotal", puntuaciontotal + Tests.get(idTest).getPuntuacion());
            intentTes.putExtra("experienciatotal", experienciatotal + Tests.get(idTest).getExperiencia());
            finish();
            startActivity(intentTes);
        }
    }
    private void ayudamitadrespuesta(int numerodetest){
        int contador=0;
        for (int i=0;i<4;i++){

         if(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(i).isSolucion()==0 && contador<2){
           String selecion1=Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(i).getRespuesta();
                 Log.i("seleccion",selecion1);
             if(a.getText().equals(selecion1)){
                 a.setEnabled(false);
             }else if(b.getText().equals(selecion1)){
                 b.setEnabled(false);
             }else if(c.getText().equals(selecion1)){
                 c.setEnabled(false);
             }else if(c.getText().equals(selecion1)){
                 c.setEnabled(false);
             }

           contador++;
         }
     }

    }

    private void cargarConsejo(int numerodetest){
        String consejo =Tests.get(numerodetest).getPreguntas().get(0).getConsejo();
        textoconsejo.setText(consejo);
    }



    private void cargarpregunta(int numerodetest){
        if(Tests.get(numerodetest).getPreguntas().get(0).getTipo().equals("texto")){
            Pregunta.setText(Tests.get(numerodetest).getPreguntas().get(0).getTitulo());
            imagenPregunta.setVisibility(View.INVISIBLE);

        }else if(Tests.get(numerodetest).getPreguntas().get(0).getTipo().equals("imagen")){
            String nameofdrawable=Tests.get(numerodetest).getPreguntas().get(0).getImagen();
            String titulopregunta=Tests.get(numerodetest).getPreguntas().get(0).getTitulo();
            Pregunta.setText(titulopregunta);


            int drawableResourceId = this.getResources().getIdentifier(nameofdrawable, "drawable", this.getPackageName());
            imagenPregunta.setImageResource(drawableResourceId);
}///asd
        a.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(0).getRespuesta());
        b.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(1).getRespuesta());
        c.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(2).getRespuesta());
        d.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(3).getRespuesta());
    }

    public void ComprobarA(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(0).isSolucion()==1) {
            Log.i("Boton A","TRUE");
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            parartiempo();

            reiniciarActivityOk();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            reiniciarActivitynoOk();parartiempo();
        }

    }
    public void ComprobarB(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(1).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton B","TRUE");
            reiniciarActivityOk();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            reiniciarActivitynoOk();parartiempo();
        }

    }
    public void ComprobarC(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(2).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton c","TRUE");
            reiniciarActivityOk();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            reiniciarActivitynoOk();parartiempo();
        }

    }
    public void ComprobarD(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(3).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton d","TRUE");
            reiniciarActivityOk();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            parartiempo();
            reiniciarActivitynoOk();

        }

    }

    public void cargarHabilidades(){
        ParseUser currenUser = ParseUser.getCurrentUser();
        int idmonster= metodosSqlite.getidmonstruoactual(currenUser);
        ArrayList<Habilidades> habilidad = metodosSqlite.seleccionarhabilidadesdemonstruoid(idmonster);



        sumartiempo.setVisibility(View.GONE);
        botonconsejo.setVisibility(View.GONE);
        fiftyfifty.setVisibility(View.GONE);

        for(int i=0;i<habilidad.size();i++){
            if(habilidad.get(i).getId()==1){

                TextView consejoview = (TextView)findViewById(R.id.textocantidaddeconsejos);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());

                consejoview.setText(String.valueOf(cantidad));
                botonconsejo.setVisibility(View.VISIBLE);
            } else if(habilidad.get(i).getId()==2){
                TextView fiftyfiftyview = (TextView)findViewById(R.id.textocantidaddefiftyfifty);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());

                fiftyfiftyview.setText(String.valueOf(cantidad));
                fiftyfifty.setVisibility(View.VISIBLE);
            }else if((habilidad.get(i).getId()==3)){
                TextView textotimeview = (TextView)findViewById(R.id.textocantidaddetiempo);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());
                sumartiempo.setVisibility(View.VISIBLE);
                textotimeview.setText(String.valueOf(cantidad));
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intent__test__juego, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
     class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {
        Activity actividad;


        int TIEMPOMAXIMO=TIEMPOLIMITE;
        public  BackgroundAsyncTask(Activity activity){
            this.actividad=activity;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            Toast.makeText(Intent_Test_Juego.this,
                    "tiempo acabado", Toast.LENGTH_LONG).show();
            actividad.finish();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
           // Toast.makeText(Intent_Test_Juego.this,
                    //"onPreExecute", Toast.LENGTH_LONG).show();

            myProgress = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            while(myProgress<TIEMPOMAXIMO){
                if (isCancelled())
                    break;

                if(Cancelar==true){
                    myProgress=TIEMPOMAXIMO;
                }else {

                    if(issumartiempo==true){
                        TIEMPOMAXIMO=TIEMPOAUMENTADO+TIEMPOLIMITE;

                    }
                    myProgress++;

                    publishProgress(myProgress);


                SystemClock.sleep(1000);}
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            barratime.setProgress(values[0]);
        }

    }




}

