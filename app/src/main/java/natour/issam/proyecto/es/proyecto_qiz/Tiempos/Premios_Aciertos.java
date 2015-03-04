package natour.issam.proyecto.es.proyecto_qiz.Tiempos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import natour.issam.proyecto.es.proyecto_qiz.MetodosSqlite;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;
import natour.issam.proyecto.es.proyecto_qiz.R;

public class Premios_Aciertos extends ActionBarActivity {
    private final int NOTIFICATION_ID = 1010;
TextView textopremio;
    Button botonseleccionarpremio;
    int idTest=0;
    int testHechos=0;
    int numerodeayudasfiftyfifty=0;
    int numerodeayudasconsejos=0;
    int numerodeayudassumartiempo=0;
    int monedastotales=0;
    int diamantestotales=0;
    float puntuaciontotal=0;
    float experienciatotal=0;
    //Tests

    Test mitesthecho;
    MetodosSqlite metodosSqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios__aciertos);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///
        ParseUser currentUser = ParseUser.getCurrentUser();
        metodosSqlite = new MetodosSqlite(this);

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


        TextView textousadopremios = (TextView) findViewById(R.id.textotiempousadopremios);
        textousadopremios.setText(String.valueOf(monedastotales));
        TextView textoistimeusedpremios = (TextView) findViewById(R.id.textoistimeusedpremios);
        textoistimeusedpremios.setText(String.valueOf(numerodeayudassumartiempo));
        TextView textofiftyusedpremios = (TextView) findViewById(R.id.textofiftyusedpremios);
        textofiftyusedpremios.setText(String.valueOf(numerodeayudasfiftyfifty));
        TextView textoconsejos = (TextView) findViewById(R.id.textoconsejoisusadopremios);
        textoconsejos.setText(String.valueOf(numerodeayudasconsejos));

        botonseleccionarpremio = (Button) findViewById(R.id.buttonSeleccionarPremio);

        textopremio = (TextView) findViewById(R.id.textoPremio);

        triggerNotification();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_premios__aciertos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void premiosYprobabilidad(View view){
        RandomCollection randomCollection = new RandomCollection();



        randomCollection.add(15,"nada");
        randomCollection.add(7,1/1.4+" monedas ");
        randomCollection.add(2,"1 diamante ");
        randomCollection.add(5,2/2+" puntos de exp ");
        randomCollection.add(20,"1 vida extra!");
        randomCollection.add(25,"1 consejo extra!");
        randomCollection.add(25,"1 ayuda fiftyfifty!");
        randomCollection.add(1,"1 saltar pregunta!");
        String resultado = String.valueOf(randomCollection.next());


        textopremio.setText("Has ganado: "+resultado);

        if(resultado.contains("consejo")){
            Log.i("consejo","");
        }

        botonseleccionarpremio.setEnabled(false);

    }

    public class RandomCollection<E> {
        private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
        private final Random random;
        private double total = 0;
        double value;
        public RandomCollection() {
            this(new Random());
        }

        public RandomCollection(Random random) {
            this.random = random;
        }

        public void add(double weight, E result) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, result);

        }

        public E next() {
             value = random.nextDouble() * total;
            return map.ceilingEntry(value).getValue();

        }

        public Double getkey(){
           Double num= map.ceilingEntry(value).getKey();

            return num;
        }
    }
    private void triggerNotification(){

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.logooo, "¡QIZIT INFORMA!!!", System.currentTimeMillis());

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.img_notification, R.drawable.logooo);
        contentView.setTextViewText(R.id.txt_notification, "Fin del Juego!!!!");

        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, Premios_Aciertos.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
