import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public void addTransicion(Set<String> newTransicones){
        for(String trans : newTransicones) {
            this.transiciones.add(new Transicion(trans));
        }
    }

    public void addTransicion(String newTransicion){

            this.transiciones.add(new Transicion(newTransicion));

    }

    public void eliminarLabda(){
        for ( int i = 0; i < this.transiciones.size() ; i++){
            if(this.transiciones.get(i).getLamba()){
                this.transiciones.remove(i);
            }
        }
    }
}
