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



    public List<Transicion> getTransiciones() {
        return transiciones;
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
