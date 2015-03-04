package natour.issam.proyecto.es.proyecto_qiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.Pregunta;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Respuestas;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;
import natour.issam.proyecto.es.proyecto_qiz.monstruos.Habilidades;
import natour.issam.proyecto.es.proyecto_qiz.monstruos.Monstruos;


public class MetodosSqlite {
    SQLiteDatabase db;
    ConnectSQLite connectSQLite;

    public MetodosSqlite(Context context) {
        connectSQLite = new ConnectSQLite(context);
        db = connectSQLite.getWritableDatabase();
    }

    public void crearUsuarioLite(String usuario,String id) {

        db.beginTransaction();

        db.execSQL("INSERT INTO USUARIOS "+ "VALUES " + "(null,'"+usuario+"','"+id+"',+"+1+")");
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public ArrayList<Monstruos> getMonstruos(){
        ArrayList<Monstruos> mismonstruos = new ArrayList<Monstruos>();
        String SQLrespuestas = "Select * FROM MONSTRUOS ";
        int posicion=0;


        Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);
        String [] nombreMonstruos=new String[cursormonstruos.getCount()];
        if (cursormonstruos.moveToFirst()) {
            do {
                int id=cursormonstruos.getInt(cursormonstruos.getColumnIndex("id"));
                String nombre= cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));
                int monedas= cursormonstruos.getInt(cursormonstruos.getColumnIndex("monedas"));
                int diamantes =    cursormonstruos.getInt(cursormonstruos.getColumnIndex("diamantes"));
                int escomprado = cursormonstruos.getInt(cursormonstruos.getColumnIndex("escomprado"));
                String imagen = cursormonstruos.getString(cursormonstruos.getColumnIndex("imagen"));
                Monstruos monstruo = new Monstruos(id,nombre,monedas,diamantes,escomprado,imagen);
                mismonstruos.add(monstruo);

                nombreMonstruos[posicion]=cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));

                posicion++;
            } while (cursormonstruos.moveToNext());
            cursormonstruos.close();

        }

    return mismonstruos;

    }

    public Monstruos monstruoactual(int id){
        String SQLrespuestas = "Select * FROM MONSTRUOS WHERE id="+id;
        Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);
Monstruos mimonstruo=null;
        if (cursormonstruos.moveToFirst()) {
            do {

                int idmonster=cursormonstruos.getInt(cursormonstruos.getColumnIndex("id"));
                String monstruonombre=cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));
                int escomprado=cursormonstruos.getInt(cursormonstruos.getColumnIndex("escomprado"));
                int monedas=cursormonstruos.getInt(cursormonstruos.getColumnIndex("monedas"));
                int diamantes=cursormonstruos.getInt(cursormonstruos.getColumnIndex("diamantes"));
                String imagen=cursormonstruos.getString(cursormonstruos.getColumnIndex("imagen"));

                mimonstruo = new Monstruos(idmonster,monstruonombre,monedas,diamantes,escomprado,imagen);
            } while (cursormonstruos.moveToNext());
            cursormonstruos.close();
        }

        return  mimonstruo;
    }

    public int getidmonstruoactual(ParseUser user){

            String SQLrespuestas = "Select monstruoactual FROM USUARIOS WHERE objectId='"+user.getObjectId()+"'";

            int id=0;
            Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);

            if (cursormonstruos.moveToFirst()) {
                do {

                    id=cursormonstruos.getInt(cursormonstruos.getColumnIndex("monstruoactual"));

                } while (cursormonstruos.moveToNext());
                cursormonstruos.close();
            }
            return id;
    }

    public int getCantidadFromUserAndIdhabilidad(ParseUser user,int idhabilidad){
    String sqlsentence="SELECT * FROM USUARIOS_HABILIDADES WHERE id_user='"+user.getObjectId()+"' and id_habilidad="+idhabilidad;
        Cursor cursorhabilidad = db.rawQuery(sqlsentence, null);
        int cantidaddehabilidades=0;
        if (cursorhabilidad.moveToFirst()) {
            do {
                cantidaddehabilidades = cursorhabilidad.getInt(cursorhabilidad.getColumnIndex("cantidad"));

            } while (cursorhabilidad.moveToNext());
            cursorhabilidad.close();
        }

    return  cantidaddehabilidades;

    }


    public ArrayList<Habilidades> seleccionarhabilidadesdemonstruoid(int id){
     ArrayList<Habilidades> todashabilidades = new ArrayList<>();
        String sqlsentence="SELECT HABILIDADES.* FROM MONSTRUOS_HABILIDADES INNER JOIN HABILIDADES ON MONSTRUOS_HABILIDADES.id_habilidad=HABILIDADES.id \n" +
                "INNER JOIN MONSTRUOS ON MONSTRUOS_HABILIDADES.id_monstruo=MONSTRUOS.id WHERE MONSTRUOS.id="+id;

        Cursor cursorhabilidad = db.rawQuery(sqlsentence, null);
        Habilidades habilidad=null;
        if (cursorhabilidad.moveToFirst()) {
            do {

               int idhab=cursorhabilidad.getInt(cursorhabilidad.getColumnIndex("id"));
                String nombrehab = cursorhabilidad.getString(cursorhabilidad.getColumnIndex("nombre"));

                String imagenhab = cursorhabilidad.getString(cursorhabilidad.getColumnIndex("imagen"));

                habilidad = new Habilidades(idhab,nombrehab,imagenhab);
                todashabilidades.add(habilidad);
            } while (cursorhabilidad.moveToNext());
            cursorhabilidad.close();
        }
        return  todashabilidades;
    }

    public String[] getNombreDeMonstruos(){
        String SQLrespuestas = "Select * FROM MONSTRUOS ";
       int posicion=0;

        Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);
        String [] nombreMonstruos=new String[cursormonstruos.getCount()];
        if (cursormonstruos.moveToFirst()) {
            do {

                nombreMonstruos[posicion]=cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));

                posicion++;
            } while (cursormonstruos.moveToNext());
            cursormonstruos.close();

        }

        return nombreMonstruos;
    }

    public ArrayList<Monstruos> getmonstruosdelusuarioActual(ParseUser currentUser){
        ArrayList<Monstruos> todosmonstruosdeusuario = new ArrayList<>();
        String sqlsentence="SELECT MONSTRUOS.* FROM USUARIOS_MONSTRUOS INNER JOIN MONSTRUOS ON USUARIOS_MONSTRUOS.id_monster=MONSTRUOS.id " +
                "INNER JOIN USUARIOS ON USUARIOS_MONSTRUOS.id_user=USUARIOS.id WHERE USUARIOS.objectId='"+currentUser.getObjectId()+"'";

        Cursor cursorMONSTERARGGG = db.rawQuery(sqlsentence, null);
        if (cursorMONSTERARGGG.moveToFirst()) {
            do {
                int id=cursorMONSTERARGGG.getInt(cursorMONSTERARGGG.getColumnIndex("id"));
                String monstruonombre = cursorMONSTERARGGG.getString(cursorMONSTERARGGG.getColumnIndex("nombre"));
                int escomprado = cursorMONSTERARGGG.getInt(cursorMONSTERARGGG.getColumnIndex("escomprado"));
                int monedas=cursorMONSTERARGGG.getInt(cursorMONSTERARGGG.getColumnIndex("monedas"));
                int diamantes=cursorMONSTERARGGG.getInt(cursorMONSTERARGGG.getColumnIndex("diamantes"));
                String imagen = cursorMONSTERARGGG.getString(cursorMONSTERARGGG.getColumnIndex("imagen"));
                Monstruos monster = new Monstruos(id,monstruonombre,monedas,diamantes,escomprado,imagen);

                todosmonstruosdeusuario.add(monster);

            } while (cursorMONSTERARGGG.moveToNext());
            cursorMONSTERARGGG.close();

        }
        return  todosmonstruosdeusuario;
    }

    public void mostrarUsuario() {
        String SQLrespuestas = "Select * FROM USUARIOS ";


        Cursor cursorGetuser = db.rawQuery(SQLrespuestas, null);

        if (cursorGetuser.moveToFirst()) {
            do {

                Log.i("NOMBRE",cursorGetuser.getString(cursorGetuser.getColumnIndex("nombre"))+" "+cursorGetuser.getString(cursorGetuser.getColumnIndex("objectId")));
            } while (cursorGetuser.moveToNext());
            cursorGetuser.close();

        }

    }




    public Test getTestporId(int id) {
        SQLiteDatabase database = db;
        Test mitest = null;
        String sqltest="Select * FROM TEST WHERE id="+id;
        Cursor cursorTest = database.rawQuery(sqltest,null);

        ArrayList<Pregunta> ListaPreguntas = null;
        if(cursorTest.moveToFirst()){
            do{
                ListaPreguntas = new ArrayList<Pregunta>();
                String SQLrespupreguntas="Select * FROM PREGUNTAS WHERE fid_preguntas="+cursorTest.getInt(cursorTest.getColumnIndex("id"));
                Cursor cursorPreguntas = database.rawQuery(SQLrespupreguntas,null);



                if (cursorPreguntas.moveToFirst()) {
                    do {
                        ArrayList<Respuestas> ListaRespuestas = new ArrayList<Respuestas>();
                        String SQLrespuestas="Select * FROM RESPUESTAS WHERE id_preguntas="+cursorPreguntas.getInt(cursorPreguntas.getColumnIndex("id_preguntas"));


                        Cursor cursorRespuestas = database.rawQuery(SQLrespuestas,null);

                        if(cursorRespuestas.moveToFirst()){
                            do {

                                int miid=cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("id_respuestas"));
                                String respuestatitulo=cursorRespuestas.getString(cursorRespuestas.getColumnIndex("respuesta"));
                                int solucionarespuesta= cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("solucion"));
                                int id_pregunta=cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("id_preguntas"));

                                Respuestas respuestas = new Respuestas(miid,respuestatitulo,solucionarespuesta,id_pregunta);
                                ListaRespuestas.add(respuestas);

                            }while (cursorRespuestas.moveToNext());
                            cursorRespuestas.close();
                        }

                        int id_pregunta=cursorPreguntas.getInt(cursorPreguntas.getColumnIndex("id_preguntas"));
                        String titulopregunta = cursorPreguntas.getString(cursorPreguntas.getColumnIndex("titulo"));
                        String categoriapregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("categoria"));
                        String imagenpregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("imagen"));
                        String tipopregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("tipo"));
                        String consejo = cursorPreguntas.getString(cursorPreguntas.getColumnIndex("Consejo"));
                        Pregunta pregunta = new Pregunta(id_pregunta,titulopregunta,categoriapregunta,imagenpregunta,tipopregunta,consejo,ListaRespuestas);
                        ListaPreguntas.add(pregunta);

                        Log.i("IDPREGUNTA",String.valueOf(pregunta.getId()));
                    }while (cursorPreguntas.moveToNext());
                }

                cursorPreguntas.close();

                int idTest=cursorTest.getInt(cursorTest.getColumnIndex("id"));
                String nombre=cursorTest.getString(cursorTest.getColumnIndex("nombre"));;
                int  casilla=cursorTest.getInt(cursorTest.getColumnIndex("casilla"));;
                float puntuacion=cursorTest.getFloat(cursorTest.getColumnIndex("puntuacion"));
                int monedas=cursorTest.getInt(cursorTest.getColumnIndex("monedas"));;
                int diamantes=cursorTest.getInt(cursorTest.getColumnIndex("diamantes"));;
                float experiencia=cursorTest.getFloat(cursorTest.getColumnIndex("experiencia"));
                Date fecha=null;

                mitest = new Test(idTest,nombre,casilla,puntuacion,monedas,diamantes,experiencia,fecha,ListaPreguntas);

            } while (cursorTest.moveToNext());

        }

        cursorTest.close();
        database.close();

        return mitest;
    }
    public void restarhabilidaddeusuario(ParseUser currentuser,int idhabilidad){
        db.execSQL("UPDATE USUARIOS_HABILIDADES SET cantidad= cantidad-1 WHERE id_user='"+currentuser.getObjectId()+"' and id_habilidad="+idhabilidad);
    }


    public void cambiarMonstruoactual (String nombremonstruoacambiar){
        String sqlmonstruo = "Select id FROM MONSTRUOS WHERE nombre='"+nombremonstruoacambiar+"'";


        Cursor cursorSeleccionarnuevomonster = db.rawQuery(sqlmonstruo, null);
        int idmonstruoacambiar=1;
        if (cursorSeleccionarnuevomonster.moveToFirst()) {
            do {
                idmonstruoacambiar = cursorSeleccionarnuevomonster.getInt(cursorSeleccionarnuevomonster.getColumnIndex("id"));
            } while (cursorSeleccionarnuevomonster.moveToNext());
            cursorSeleccionarnuevomonster.close();

        db.execSQL("UPDATE USUARIOS SET monstruoactual="+idmonstruoacambiar);


        }
    }

}
