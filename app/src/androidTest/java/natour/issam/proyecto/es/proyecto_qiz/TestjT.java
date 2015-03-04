package natour.issam.proyecto.es.proyecto_qiz;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class TestjT extends ActivityInstrumentationTestCase2<Login> {

    private EditText etext1;
    private EditText etext2;
    private ImageView enter;
    private static final String Login = "issam22";
    private static final String Password = "hola";

    private Login actividad;

    public TestjT() {
        super(Login.class);
    }

// -
    protected void setUp() throws Exception {
        super.setUp();

        actividad = getActivity();

        etext1 = (EditText) actividad.findViewById(R.id.editText);
        etext2 = (EditText) actividad.findViewById(R.id.editText2);
        enter = (ImageView) actividad.findViewById(R.id.imageViewLogin);


    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testAddValues() {

        String NombreActividadMainMenu;

        // on value 1 entry
        TouchUtils.tapView(this, etext1);
        sendKeys(Login);

        // now on value2 entry
        TouchUtils.tapView(this, etext2);
        sendKeys(Password);

        // now on Login button
        TouchUtils.clickView(this, enter);


        actividad = getActivity();
        NombreActividadMainMenu = actividad.getClass().getSimpleName();
System.out.print("a");

        assertTrue("Add result should be...", NombreActividadMainMenu.equals("Panel_Inicio"));
    }
}