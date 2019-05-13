import com.sun.source.doctree.StartElementTree;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Declaras la gramatica, esta es un HashMap de reglas
        HashMap<String, Regla> gramatica = new HashMap<>();
        Scanner sc;
        try {
            sc = new Scanner(new File("src/Gramatica.txt"));

        } catch (FileNotFoundException e) {
            System.out.println("No se pudo encontrar el archivo: Gramatica.txt");
            return;
        }

        String simboloInicial = "";

        boolean esReglaInicial = true;

        //Mientras haya una linea en el archivo leerla para encontrar las reglas
        while (sc.hasNextLine()) {
            //Separa la linea en dos usando "->" como flag. linTemp[0] contiene el nombre y linTemp[1] contiene las
            //reglas no separadas

            String linTemp = sc.nextLine();
            String[] nombreReglas = linTemp.split("->");

            //Separa laS reglas utilizando el caracter "|"
            //Crear nueva regla y agregarla a gramatica

            if (esReglaInicial){
                simboloInicial = nombreReglas[0];
                esReglaInicial = false;
            }


            gramatica.put(nombreReglas[0], new Regla(nombreReglas[0], nombreReglas[1].split("/")));

        }
        //Finaliza el while para crear la gramatica

        imprimirGramatica(gramatica);

        ///Eliminar recursion en el Simbolo inicial


        Regla reglaS = gramatica.get(simboloInicial);

        boolean tieneRecursionInicial = false;

        for (Transicion trans : reglaS.getTransiciones()){
            if(trans.getProduccion().contains("S")){

                tieneRecursionInicial = true;
            }
        }

        if(tieneRecursionInicial){
            String[] arrTemp = {"S"};
            gramatica.put("S'", new Regla("S'", arrTemp));
            simboloInicial = "S'";
        }
        System.out.println("");

        imprimirGramatica(gramatica);

        ///Finaliza eliminar recursion inicial


        ///Eliminar reglas lambda
        Set<String> NULL = new HashSet<>();
        Set<String> PREV = new HashSet<>();

        for (String key : gramatica.keySet()){
            for(Transicion trans : gramatica.get(key).getTransiciones()) {
                if (trans.getLamba()) {
                        NULL.add(key);
                }
            }
        }


        if (NULL.contains(simboloInicial)){
            NULL.remove(simboloInicial);
        }

        while ( !(NULL.equals(PREV))){
            PREV = NULL;
            for(String key : gramatica.keySet()){

                for(Transicion trans : gramatica.get(key).getTransiciones()) {

                    Set<String> temp = new HashSet<String>();


                    for (Character car : trans.getVariables()){
                        temp.add(car.toString());
                    }

                    if(PREV.equals(temp)){

                        NULL.add(key);
                    }

                }
            }
        }

        System.out.println();

        System.out.print("NULL = ");

        System.out.println(NULL);

        ///

        for(String key : gramatica.keySet()){
            Set<String> nuevasTransiciones = new HashSet<>();
            gramatica.get(key).eliminarLabda();

            for(Transicion trans: gramatica.get(key).getTransiciones()){
                    Set<String> NEW = new HashSet<>();
                    Set<String> ULT = new HashSet<>();
                    Set<String> NEWtemp = new HashSet<>();

                    ULT.add(trans.getProduccion());

                    while (ULT.size()>0){
                        for (String tempProd : ULT){

                            String[] temp = tempProd.split("");

                            for (int i = 0;i<temp.length;i++){

                                if(NULL.contains(temp[i])){
                                    ///Crear metodo que regrese un string sin el character que esta en cierto indice
                                    NEWtemp.add(eliminarCaracter(tempProd,i));


                                }
                            }
                        }

                        ULT.clear();
                        ULT.addAll(NEWtemp);
                        NEW.addAll(NEWtemp);
                        NEWtemp.clear();

                    }
                    nuevasTransiciones.addAll(NEW);

            }

            ///Ya que se obtuvieron todas las nuevas transicones se deben de agregar a la regla
            //System.out.println("Nuevas Transiciones:   "+nuevasTransiciones);

            for(String transicion : nuevasTransiciones){
                if(!transicion.equals("")){
                    gramatica.get(key).addTransicion(transicion);
                }

            }

        }

        gramatica.get(simboloInicial).addTransicion("&");



        imprimirGramatica(gramatica);

        ///Eliminar reglas encadenadas

        ///Se rea una copia del hashmap para almacenar las transiciones originales

        HashMap<String, Regla> gramaticaCopia = new HashMap<>();
        gramaticaCopia.putAll(gramatica);

        gramatica.clear();
        ///Crear los sets que requiere el algoritmo o vaciarlos para poder reusar el nombre

        for(String key : gramaticaCopia.keySet()){
            Set<String> CHAIN = new HashSet<>();
            PREV.clear();
            Set<String> NEW = new HashSet<>();
            CHAIN.add(key);

            while( !CHAIN.equals(PREV)){

                NEW = restaSet(CHAIN, PREV);

                PREV.clear();

                PREV.addAll(CHAIN);

                for (String B : NEW){
                    for(Transicion trans : gramaticaCopia.get(B).getTransiciones()){
                        if(trans.getVariables().length == 1){
                            List<String > temp = new ArrayList<>();
                            for(Character car : trans.getVariables()){
                                temp.add(car.toString());
                            }

                            CHAIN.addAll(temp);

                        }

                    }

                }


            }
            System.out.println(CHAIN);
            ///Crear un array que contenga la union de las transiciones
            Set<String> nuevasTrans = new HashSet<>();

            for(String clave : CHAIN){
                for(Transicion trans : gramaticaCopia.get(clave).getTransiciones()){
                    if(!CHAIN.contains(trans.getProduccion())) {
                        nuevasTrans.add(trans.getProduccion());
                    }
                }

            }



            gramatica.put(key, new Regla(key, nuevasTrans.toArray(new String[nuevasTrans.size()]) ));

        }


        imprimirGramatica(gramatica);







        ///Eliminar simbolos useless


        ///Hacer algoritmos de Chomsky



    }



    ///Metodo para eliminar un caracter de un String de cierto indice

    public static String eliminarCaracter(String palabra, int indice){
        String temp = "";
        StringBuilder sb = new StringBuilder();
        char[] arrayTemp = palabra.toCharArray();
        for(int i = 0; i< arrayTemp.length ;i++){
            if(i!=indice) {
                sb.append(arrayTemp[i]);
            }
        }

        temp = sb.toString();

        return temp;
    }


    ///Inicia sumSet
    public static Set<String> sumSet(Set<String> setA, Set<String> setB){
        Set<String> temp = new HashSet<>();
        temp.addAll(setA);
        temp.addAll(setB);
        return temp;
    }
    ///Finaliza sumSet

    /// Simula la resta de Set A menos Set B. Regresa un set que contiene los elementos de A excepto aquellos que esten
    /// en B.
    public static Set<String> restaSet(Set<String> setA, Set<String> setB){
        Set<String> temp = new HashSet<>(setA);
        for(String car: setB){
            if(temp.contains(car)){
                temp.remove(car);
            }
        }
        return temp;
    }
    //Finaliza restaSet


    /// imprimirGramatica metodo que se puede usar para imprimir una gramatica representada
    // en el modo HashMap<String,Regla>
    public static void imprimirGramatica(HashMap<String,Regla> gramatica){
        for(String key: gramatica.keySet()){
            System.out.println();
            System.out.print(key+ "-->");
            List<Transicion> temp = gramatica.get(key).getTransiciones();
            for (Transicion trans : temp){
                System.out.print(trans.getProduccion() +"|");
            }
        }
        System.out.println();

    }
    // Finaliza imprimirGramatica

}