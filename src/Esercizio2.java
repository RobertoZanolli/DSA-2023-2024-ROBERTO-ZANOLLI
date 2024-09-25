/*
NOME: ROBERTO
COGNOME: ZANOLLI
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Esercizio2 {
    /*
     * Metodo per Inizializzare un HashMap con le associazioni
     * codice-carattere.
     */
    public static HashMap<String, Character> initializeCipher() {
        HashMap<String, Character> cipher = new HashMap<>();
        cipher.put("0", 'A');
        cipher.put("00", 'B');
        cipher.put("001", 'C');
        cipher.put("010", 'D');
        cipher.put("0010", 'E');
        cipher.put("0100", 'F');
        cipher.put("0110", 'G');
        cipher.put("0001", 'H');
        return cipher;
    }

    /*
     * Metodo che controlla la presenza di decodifiche alternative per una stringa binaria lunga n (code) .
     *
     * Sfrutta la programmazione dinamica risolvendo i sottoproblemi S[i] con 0<=i<n (sottostringhe
     * formate da i primi i caratteri) e riutilizzando le soluzioni dei sottoproblemi ripetuti,
     * arriva alla soluzione finale S[n].
     *
     * Le soluzioni dei sottoproblemi (le possibili decodifiche di ogni sottostringa di lunghezza i) vengono
     * memorizzate nel vettore di liste di stringhe seq[].
     *
     * Questo metodo ha complessità O(n^2*m) nel caso peggiore.
     *
     * NOTA:
     * Poiché si osserva che la lunghezza massima delle sottostringhe nel cifrario sia 4,
     * potremmo ridurre le iterazioni evitando di verificare se sottostringhe più lunghe di 4 caratteri abbiano una codifica.
     */

    public static List<String> getSequences(String code) {

        HashMap<String, Character> cipherMap = initializeCipher();
        int n = code.length();

        List<String>[] seq = new ArrayList[n + 1];
        seq[0] = new ArrayList<>();
        seq[0].add("");


        for (int i = 1; i <= n; i++) {
            seq[i] = new ArrayList<>();

            // Ciclo per analizzare tutte le possibili sottostringhe che TERMINANO a i.
            // Estrae la sottostringa (j:i) e verifica se è presente nella mappa di cifratura.

            for (int j = 0; j < i; j++) {
                String substring = code.substring(j, i);

                if (cipherMap.containsKey(substring)) {
                    char character = cipherMap.get(substring);
                    // Aggiungo il nuovo carattere ad ogni prefisso
                    for (String prefix : seq[j]) {
                        seq[i].add(prefix + character);
                    }
                }
            }
        }

        return seq[n];
    }

    /*
     * Legge la stringa binaria dal file e la restituisce.
     */
    public static String readFile(String inputFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(inputFile));
        String code = "";
        while (scanner.hasNextLine()) {
            code = scanner.nextLine();
        }
        scanner.close();
        return code;
    }

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 1) {
            System.err.println("Errore, utilizza il formato: java Esercizio2 <file.txt>");
            return;
        }

        String code = readFile(args[0]);

        if (code == null || code.equals("")) {
            System.out.println(0);
            return;
        }
        List<String> sequences = getSequences(code);

        //Viene fatto il sorting solo per rispettare l'output della consegna
        sequences.sort(null);

        if (sequences.isEmpty()) {
            System.out.println("0");
        } else {
            System.out.println(sequences.size() + ",");
            for (String seq : sequences) {
                System.out.println(seq + ",");
            }
        }
    }


}
