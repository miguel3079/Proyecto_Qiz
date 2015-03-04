package natour.issam.proyecto.es.proyecto_qiz;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;


public class ventana_emergente extends ActionBarActivity {
    Panel_Inicio panel_inicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_emergente);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ventana_emergente, menu);
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
    public void quitarven(View view){
        finish();

    }
    public void quitarvenSalir(View view){
        finish();

    }
}
