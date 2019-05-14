import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class Transicion {
    private String produccion;
    private Character[] finales;
    private Character[] variables;
    private Boolean lamba = false;


    public Transicion(String produccion){
        this.produccion = produccion;
        // Dividir los caracteres dependiendo de si son finales o variables
        List<Character> finTemp = new ArrayList<>();
        List<Character> varTemp = new ArrayList<>();

        char[] letritas = produccion.toCharArray();
        List<Character> letrasTemp = new ArrayList<>();

        for(char letra : letritas){
            letrasTemp.add(letra);
        }
        Character[] letras = letrasTemp.toArray( new Character[letrasTemp.size()]);

        for(int i = 0; i < letras.length ; i++){
            if( letras[i].equals('&')){
                this.lamba = true;
            } else if(Character.isUpperCase(letras[i])){
                varTemp.add(letras[i]);

            } else {
                finTemp.add(letras[i]);
            }
        }


        // Una vez terminado convertir la lista a un array y poner en finales y variables
        this.finales = new Character[finTemp.size()];
        this.variables = new Character[varTemp.size()];

        this.finales = finTemp.toArray(this.finales);
        this.variables = varTemp.toArray(this.variables);



    }

    public void setFinales(Character[] finales) {
        this.finales = finales;
    }

    public void setProduccion(String produccion) {
        this.produccion = produccion;
    }

    public void setVariables(Character[] variables) {
        this.variables = variables;
    }

    public Character[] getFinales() {
        return finales;
    }

    public Character[] getVariables() {
        return variables;
    }
    public String getProduccion() {
        return produccion;
    }

    public Boolean getLamba() {
        return lamba;
    }

    public void setLamba(Boolean lamba) {
        this.lamba = lamba;
    }

}
