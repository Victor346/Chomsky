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

            for(Transicion trans: gramatica.get(key).getTransiciones()){
                    Set<String> NEW = new HashSet<>();
                    List<String> temporal = Arrays.asList(trans.getProduccion());

                    String[] superTemp = temporal.get(0).split("");
                    List<String> pera = new ArrayList<>(Arrays.asList(temporal.get(0).split("")));
                    for(int i=0; i < superTemp.length; i++){
                        if(NULL.contains(superTemp[i])){

                            pera.remove(i);


                            System.out.println(pera);

                            pera = Arrays.asList(superTemp);
                        }


                    }

            }

        }





        ///Eliminar reglas encadenadas

        ///Eliminar simbolos useless


        ///Hacer algoritmos de Chomsky



    }

    ///Inicia sumSet
    public static Set<Character> sumSet(Set<Character> setA, Set<Character> setB){
        Set<Character> temp = new HashSet<>();
        temp.addAll(setA);
        temp.addAll(setB);
        return temp;
    }
    ///Finaliza sumSet

    /// Simula la resta de Set A menos Set B. Regresa un set que contiene los elementos de A excepto aquellos que esten
    /// en B.
    public static Set<Character> restaSet(Set<Character> setA, Set<Character> setB){
        Set<Character> temp = new HashSet<>(setA);
        for(Character car: setB){
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

    }
    // Finaliza imprimirGramatica

}