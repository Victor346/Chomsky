import java.util.ArrayList;
import java.util.List;

public class Regla {
    private String nombre;
    private List<Transicion> transiciones;

    public Regla(String nombre, String[] transiciones){
        this.nombre = nombre;
        this.transiciones = new ArrayList<>();
        for(String element : transiciones){


            this.transiciones.add(new Transicion(element));

        }


    }

    public String getNombre() {
        return nombre;
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTransiciones(String[] transiciones) {
        for(String element : transiciones){
            this.transiciones.add(new Transicion(element));
        }

    }
}
