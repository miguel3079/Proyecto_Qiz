package natour.issam.proyecto.es.proyecto_qiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.CargarTests;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;
import natour.issam.proyecto.es.proyecto_qiz.monstruos.MiMonstruo;

public class Panel_Inicio extends ActionBarActivity {
    SQLiteDatabase db;
    ///Notificaciones///
    private final int NOTIFICATION_ID = 1010;
    /////////////////
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    NavigationAdapter NavAdapter;
    ////
    private static int RESULT_LOAD_IMAGE = 1;
     ProfilePictureView userProfilePictureView;
    ProfilePictureView userProfilePictureViewNavigation;
    ParseImageView ImagenParseUserNavigation;
    ParseImageView ImagenParseUser;
    Context context = this;
    ImageView buttonPuntuacion;
    private MediaPlayer mediaPlayer;
    TextView textoNombre;
    TextView textonivel;
    private CharSequence mTitle;
    TextView monedas;
    TextView diamantes;
    ImageView imageViewJugar;

    SharedPreferences appPrefs;
    SharedPreferences.Editor editablepref;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///
        appPrefs = context.
                getSharedPreferences("Opciones",
                        Context.MODE_PRIVATE);
        editablepref= appPrefs.edit();



        //Drawer Layout
        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Lista
        NavList = (ListView) findViewById(R.id.lista);
        //Declaramos el header el caul sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);
        //Establecemos header
        NavList.addHeaderView(header);
        //Tomamos listado  de imgs desde drawable
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);
        //Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.nav_options);
        //Listado de titulos de barra de navegacion
        NavItms = new ArrayList<Item_objct>();
        //Agregamos objetos Item_objct al array
        //Perfil
        NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));

        //Configuracion
        NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));
        //Share
        NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));
        //Declaramos y seteamos nuestrp adaptador al cual le pasamos el array con los titulos
        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);

        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("posicion",position+"");
                selectItem(position);
            }
        });

        //Siempre vamos a mostrar el mismo titulo
        textonivel=(TextView ) findViewById(R.id.textViewnivel);
        textoNombre=(TextView ) findViewById(R.id.textViewNombre);
        monedas=(TextView ) findViewById(R.id.textViewMonedas);
        diamantes=(TextView ) findViewById(R.id.textViewDiamantes);
        imageViewJugar = (ImageView)findViewById(R.id.imageViewJugar);

        buttonPuntuacion = (ImageView) findViewById(R.id.imageViewRanking);
        mTitle = getTitle();
        ParseUser currentUser = ParseUser.getCurrentUser();

        //imagen de facebook del user logeado
        userProfilePictureView= (ProfilePictureView) findViewById(R.id.imageFb);
        userProfilePictureViewNavigation = (ProfilePictureView) findViewById(R.id.imageFbnavigation);
        userProfilePictureViewNavigation.setVisibility(View.INVISIBLE);
        userProfilePictureView.setVisibility(View.INVISIBLE);
        // imagen del perfil del usuario normal Parse

        ImagenParseUserNavigation =(ParseImageView ) findViewById(R.id.imageUsernavigation);
        ImagenParseUser =  (ParseImageView ) findViewById(R.id.imageUser);

        // compruebo la session de facebook
        Session session = ParseFacebookUtils.getSession();

        //   Si el usuario actual es un usuario de facebook, pongo el imageview de la foto de los
        // usuarios normales invisible y llamo a Makemerequest() carga los datos en la vista

        if (session != null && session.isOpened()) {
            //   imageView.setVisibility(View.INVISIBLE);

            makeMeRequest();
            ImagenParseUserNavigation.setVisibility(View.INVISIBLE);
            ImagenParseUser.setVisibility(View.INVISIBLE);
        }
        // si es un user normal cargo datos del user normal
        if(currentUser != null && session==null ){
            cargarusuarioNormal();
// pongo la foto de perfil de facebook invisible
            userProfilePictureViewNavigation.setVisibility(View.INVISIBLE);
            userProfilePictureView.setVisibility(View.INVISIBLE);
        }

        reproducirMusica();

        buttonPuntuacion.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final Intent intent = new Intent();
                intent.setClass(context,Puntuacion.class);
                startActivity(intent);

            }
        });

        //// prueba SQLO

        ConnectSQLite connectSQLite=new ConnectSQLite(this);

        db = connectSQLite.getWritableDatabase();


        connectSQLite.createDataBase();
        connectSQLite.openDataBase();

        CargarTests cargarTests = new CargarTests(this);
        cargarTests.execute();
        try {
            ArrayList<Test> Tests = cargarTests.get();
            Log.i("TODOS TEST",Tests.get(0).getPreguntas().get(0).getRespuestas().get(0).getRespuesta());
            Log.i("TODOS TEST",Tests.get(0).getPreguntas().get(0).getRespuestas().get(1).getRespuesta());
            Log.i("TODOS TEST",Tests.get(0).getPreguntas().get(0).getRespuestas().get(2).getRespuesta());
            Log.i("TODOS TEST",Tests.get(0).getPreguntas().get(0).getRespuestas().get(3).getRespuesta());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void reproducirMusica(){


        boolean musicaActivada = appPrefs.getBoolean ("sonido", true);
        if(musicaActivada){
            mediaPlayer = MediaPlayer.create(this, R.raw.thepandofqueen);
            mediaPlayer.start();


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panel__inicio, menu);
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
/* Metodo-evento para cambiar la foto del perfil de  perfil y seleccionar una de la camara

 */
public void EmpezarAjugar(View view){
    Intent intent = new Intent(this, Intent_Test_Juego.class);
    intent.putExtra("id", 0);
    intent.putExtra("testRealizados", 0);
    intent.putExtra("numerodeayudasfiftyfifty", 0);
    intent.putExtra("numerodeayudasconsejos", 0);
    intent.putExtra("numerodeayudassumartiempo", 0);
    intent.putExtra("monedastotales", 0);
    intent.putExtra("diamantestotales", 0);
    intent.putExtra("puntuaciontotal", 0);
    intent.putExtra("experienciatotal", 0);


    startActivity(intent);
}

    @Override
    protected void onResume() {
        super.onResume();
        Session session = ParseFacebookUtils.getSession();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null && session==null ){
            cargarusuarioNormal();
// pongo la foto de perfil de facebook invisible

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    public void CerrarSesion(View v) {
        Intent emergente = new Intent(context,ventana_emergente.class);
        startActivity(emergente);
        //ParseUser.logOut();

        // finish();
    }
    public void CambiarImagen(View view){

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }
    /*
    Metodo que recibe la imagen selecionada en CambiarImagen() y procesa la imagen para subirla y guardarla en el Parse
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ImagenParseUserNavigation.setImageBitmap(yourSelectedImage);
            ImagenParseUser.setImageBitmap(yourSelectedImage);
            byte[] image = stream.toByteArray();
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseFile photoFile = new ParseFile("fotoperfil"+currentUser.getUsername()+".gif",image);

            currentUser.put("Imagen", photoFile);
            currentUser.saveInBackground();
        }

    }
    // si el usuario es un usario de facebook tengo que cargar sus datos con esto
    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {

                            JSONObject userProfile = new JSONObject();
                            try {

                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender", user.getProperty("gender"));
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email", user.getProperty("email"));
                                }


                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // muestro la informacion
                                updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d("TAG", "Error parsing returned user data. " + e);
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                    (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d("TAG", "The facebook session was invalidated." + response.getError());
                                logout();
                            } else {
                                Log.d("TAG",
                                        "Some other error: " + response.getError());
                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }




    private void logout() {
        // cerrar sesion usuario actual y cierra la pantalla
        Intent emergente = new Intent(context,ventana_emergente.class);
        startActivity(emergente);
        ParseUser.logOut();
        finish();
    }
    // muestra la informacion cargada en MAKEMEARQUEST
    private void updateViewsWithProfileInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.has("profile")) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.has("facebookId")) {
                    userProfilePictureViewNavigation.setProfileId(userProfile.getString("facebookId"));
                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureViewNavigation.setProfileId(null);
                    userProfilePictureView.setProfileId(null);
                }

                if (userProfile.has("name")) {
                    textoNombre.setText(userProfile.getString("name"));
                } else {
                    textoNombre.setText(userProfile.getString(""));
                }

                textonivel.setText(currentUser.get("Nivel").toString());
                monedas.setText(currentUser.get("Monedas").toString());
                diamantes.setText(currentUser.get("Diamantes").toString());
/*
                if (userProfile.has("gender")) {
                    userGenderView.setText(userProfile.getString("gender"));
                } else {
                    userGenderView.setText("");
                }

                if (userProfile.has("email")) {
                    userEmailView.setText(userProfile.getString("email"));
                } else {
                    userEmailView.setText("");
                }
*/          userProfilePictureViewNavigation.setVisibility(View.VISIBLE);
                userProfilePictureView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                Log.d("TAG", "Error parsing saved user data.");
            }
        }
    }
    // carga datos del usuario PARSE normal no facebook
    public void cargarusuarioNormal(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ImagenParseUserNavigation.setParseFile(currentUser.getParseFile("Imagen"));
        ImagenParseUser.setParseFile(currentUser.getParseFile("Imagen"));
        textonivel.setText(currentUser.get("Nivel").toString());
        textoNombre.setText(currentUser.getUsername());
        monedas.setText(currentUser.get("Monedas").toString());
        diamantes.setText(currentUser.get("Diamantes").toString());
        ImagenParseUserNavigation.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

            }
        });

        ImagenParseUser.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

            }
        });
    }
    ///Metodo de la notificacion


    private void selectItem(int position) {

        Intent intent_opciones = new Intent();
        Log.i("OPCIONES",String.valueOf(position));
        switch (position) {
            case 0:


                break;
            case 1:

                Intent intent5 = new Intent(this, MiMonstruo.class);

                startActivity(intent5);

                break;
            case 2:
                intent_opciones.setClass(this,Opciones.class);
                startActivity(intent_opciones);

                break;
            case 3:

                intent_opciones.setClass(this,Invitar_Amigos.class);
                startActivity(intent_opciones);


                break;

        }


    }

}
