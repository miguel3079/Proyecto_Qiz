package natour.issam.proyecto.es.proyecto_qiz.monstruos;

/**
 * Created by Issam on 18/02/2015.
 */
public class Habilidades {
    int id;
    String nombre;

    String imagen;




    public Habilidades(int id,String nombre,String imagen){
    this.id=id;
    this.nombre=nombre;
    this.imagen=imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
