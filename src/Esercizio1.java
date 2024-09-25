/*
NOME: ROBERTO
COGNOME: ZANOLLI
*/


/*
 *---SPIEGAZIONE LOGICA ALGORITMO DI COMPARAZIONE ALBERI---
 *
 *    Il confronto dei due alberi si basa sull'invocare su di essi il metodo areEqual() che a sua volta confronta
 *    le due radici con il metodo equals() della classe Node di cui è stato fatto override.
 *
 *    Il metodo equals() della classe Node verifica che i due valori contenuti nei nodi siano uguali e invoca il
 *    metodo equals() di HashSet sui due insiemi di figli (children).
 *
 *    Il metodo equals() di HashSet a sua volta (dopo aver confrontato le dimensioni) confronta ciascun elemento
 *    (nodo figlio) utilizzando il metodo contains() (per verificare che sia presente anche nell'altro set)
 *    che ha sua volta sfrutta il metodo equals() della classe Node, creando così un meccanismo ricorsivo perchè
 *    a loro volta verranno confrontati anche gli HashSet di figli dei nodi figli.
 *
 *    Questo avverrà fino al raggiungimento delle foglie.
 *
 *    E' stato fatto override anche del metodo hashCode() in modo che restituisca il valore del nodo stesso.
 *
 * ---ANALISI COSTO COMPUTAZIONALE---
 *
 *    Il caso peggiore per questo algoritmo ricorsivo è che i due alberi siano effettivamente uguali
 *    e che quindi sia richiesto di confrontare tutte le n coppie di nodi.
 *
 *    Il confronto tra il valore dei due nodi avviene in O(1).
 *    Il lookup() di un elemento in un HashSet senza collisioni (non ci sono elementi diversi con stesso hashcode)
 *    avviene in O(1).
 *    Per ogni nodo i con m(i) figli vengono fatti m(i) lookup() ma la sommatoria di tutti i m(i) (con 0<i<n-1)
 *    è uguale a n-1 (sommando il numero di figli di ogni nodo si arriva a n-1 perchè uno è la radice dell'albero).
 *    Da questo si ricava che per confrontare tutti gli n nodi avrò una complessità totale di O(n).
 *
 *    Test aggiuntivo:
 *
 *    Ipotizzando che l'albero abbia un numero medio fissato di figli m (considerando l'albero m-ario bilanciato
 *    il numero di confronti non cambia in quanto tutti i nodi dovranno essere confrontati) in modo da poter ricavare 
 *    una equazione di ricorrenza è possibile applicare il master theorem:
 *
 *    L'equazione di ricorrenza ottenuta è del tipo:
 *
 *    T(n) = | O(1) se n = 1
 *           | m*T(n/m) + O(1) se n > 1
 *
 *    da cui:
 *    a=b=m
 *    f(n) = O(1) = c
 *    n^(log_b(a)) = n^(log_m(m)) = n^1
 *
 *    E' il caso 1:
 *    Confrontando n^(log_b(a)) con f(n) è evidente che f(n) = O(n^(1-ε)) con ε>0 e di conseguenza T(n) = THETA(n).
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.*;



public class Esercizio1 {

    /*
     * Implementazione dell'albero mediante nodi dotati di valore (int) e hashset di nodi figli .
     * E' stato scelto di implementarli con un set perchè nella traccia è specificato che due alberi sono
     * da considerarsi uguali anche se in ordine diverso e che i valori sono univoci.
     * Un insieme non considera l'ordine e non ammette duplicati.
     * Inoltre HashSet permette ricerca, aggiunta e rimozione in modo efficiente.
     */
    public static class Tree {
        public Node root;

        public static class Node {
            public final int value;
            public final HashSet<Node> children;

            public Node(int value) {
                this.value = value;
                this.children = new HashSet<>();
            }

            /*
             * Restituisce il valore del nodo.
             */

            public int getValue() {
                return value;
            }
            /*
             * Restituisce l'insieme dei figli.
             */
            public HashSet<Node> getChildren() {
                return children;
            }
            /*
             * Aggiunge un nodo all'insieme dei figli del nodo padre.
             */
            public void addChild(Node child) {
                children.add(child);
            }

            /*
             * Viene fatto override del metodo equals di Node:
             * 2 nodi sono da considerarsi uguali se  hanno lo stesso valore e se i 2 set di figli
             * sono uguali secondo il metodo equals() del set.
             * Non è richiesto effettuare controlli sulla dimensione dei figli perchè già
             * il metodo equals di Set effettua il controllo prima di verificare che gli
             * elementi contenuti siano uguali.
             */
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;

                if (!(obj instanceof Node)) return false;

                Node otherNode = (Node) obj;
                return this.value == otherNode.value && this.children.equals(otherNode.children);
            }

            /*
             * Viene fatto override del metodo hashCode(), fondamentale per il lookup nel hashset.
             * Utilizzo direttamente il value come hashcode perchè identifica univoacamaente il nodo.
             */
            @Override
            public int hashCode() {
                return value;
            }
        }

        public Tree() {}

        public Tree(Node root) {
            this.root = root;
        }

        public Node getRoot() {
            return root;
        }

        /*
         * Invoca il metodo visit() facendolo partire dalla radice.
         */
        public String visit() {
            return visit(root, 0);
        }
        /*
         * Algoritmo di visita che tiene conto della profondità a cui si trova per stampare i nodi figli
         * dello stesso nodo con la stessa indentazione.
         */
        public String visit(Node node, int depth) {
            StringBuilder s = new StringBuilder();
            if (node != null) {

                StringBuilder indent = new StringBuilder();
                for (int i = 0; i < depth; i++) {
                    indent.append("  ");
                }

                s = new StringBuilder(indent.toString() + node.getValue() + "\n");

                // Chiamata ricorsiva per ciascun figlio con profondità incrementata
                for (Node child : node.getChildren()) {
                    s.append(visit(child, depth + 1));
                }
            }
            return s.toString();
        }
    }

    /*
     * Verifica se alberi con radici root1 e root2 sono uguali.
     * Due alberi sono considerati uguali se hanno la stessa struttura e gli stessi valori in tutti i nodi.
     * Il confronto tra gli alberi viene effettuato direttamente invocando il metodo equals()
     * implementato nella classe Node (invocandolo sulle radici).
     */
    public static boolean areEqual(Tree tree1, Tree tree2) {
        if(tree1 == null && tree2 == null)
            return true;
        else if(tree1 == null || tree2 == null)
            return false;
        else   
            return tree1.getRoot().equals(tree2.getRoot());

    }


    /*
     * Costruisce un albero a partire da un file contenente coppie di valori padre-figlio.
     * Ogni riga del file rappresenta una relazione padre-figlio, con i valori separati da una virgola.
     * Il metodo costruisce i nodi dell'albero, li collega e individua il nodo radice per costruire
     * un oggetto Tree.
     */
    public static Tree buildFromPairs(String file) throws IOException {

        Map<Integer, Tree.Node> nodeMap = new HashMap<>();
        Set<Integer> childrenSet = new HashSet<>();
        Scanner scanner = new Scanner(new File(file));

        //Controlla se i nodi esistono già nella mappa (usando il valore come
        // chiave), se no, li crea e li aggiunge.

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll(" ", "");
            String[] parts = line.split(",");
            int parentVal = Integer.parseInt(parts[0]);
            int childVal = Integer.parseInt(parts[1]);

            Tree.Node parent = nodeMap.get(parentVal);
            if (parent == null) {
                parent = new Tree.Node(parentVal);
                nodeMap.put(parentVal, parent);
            }

            Tree.Node child = nodeMap.get(childVal);
            if (child == null) {
                child = new Tree.Node(childVal);
                nodeMap.put(childVal, child);
            }
            //Collegamento padre-figlio
            parent.addChild(child);
            childrenSet.add(childVal);
        }
        scanner.close();

        // Cerco nodo il cui valore non è contenuto nel set dei nodi figli --> questo nodo
        // non è figlio di nessuno quindi è radice.
        Tree tree = new Tree();
        for (int key : nodeMap.keySet()) {
            if (!childrenSet.contains(key)) {
                tree = new Tree(nodeMap.get(key));
                break;
            }
        }
        return tree;
    }

    /*
     * Costruisce un albero a partire da un file contenente una stringa rappresentante delle liste
     * annidate.
     *
     * I caratteri ‘[‘ e ‘]’ vengono usati per delimitare una lista e gli elementi all’interno
     * della lista sono separati dal carattere ‘,’.
     * Viene usato uno stack per memorizzare la gerarchia dei nodi, i nodi che vengono creati
     * sono figli dell'ultimo nodo in cima.
     *
     * Viene usato un oggetto StringBuilder invece che String perchè ha prestazioni migliori 
     * quando deve essere modificato.
     */
    public static Tree buildFromNestedList(String file) throws IOException {
        Scanner scanner = new Scanner(new File(file));
        String nested = scanner.nextLine().replaceAll(" ", "");
        scanner.close();

        Stack<Tree.Node> nodeStack = new Stack<>();
        Tree.Node root = null;
        StringBuilder number = new StringBuilder();


        for (int i = 0; i < nested.length(); i++) {
            char ch = nested.charAt(i);

            // Se il carattere è una cifra numerica la aggiungo  number (potrebbe non essere finito).
            if (Character.isDigit(ch)) {
                number.append(ch);
            }

            // Se il carattere è '[' vuol dire che sta per iniziare una lista di nodi quindi
            // si controlla se number conteneva un numero e se si viene creato un nodo con quel valore.
            // Se la stack NON è vuota questo nodo è figlio di quello in cima.Infine il nodo viene inserito
            // in cima alla pila.
            else if (ch == '[') {
                if (!number.isEmpty()) {
                    Tree.Node node = new Tree.Node(Integer.parseInt(number.toString()));
                    if (!nodeStack.isEmpty()) {
                        nodeStack.peek().addChild(node);
                    }
                    nodeStack.push(node);

                    number.setLength(0);
                }

            }

            // Se il carattere è ']' vuol dire che il numero è finito e che è finita
            // una lista di nodi quindi si controlla se la stringa number conteneva un numero e se si
            // viene creato un nodo con quel valore.
            // Se la stack NON è vuota questo nodo è figlio di quello in cima.Il nodo viene poi inserito in cima.
            // Infine se stack non è vuota viene rimosso l'elemento in cima.
            else if (ch == ']') {
                if (!number.isEmpty()) {
                    Tree.Node node = new Tree.Node(Integer.parseInt(number.toString()));
                    if (!nodeStack.isEmpty()) {
                        nodeStack.peek().addChild(node);
                    }
                    nodeStack.push(node);
                    if (root == null) {
                        root = node;
                    }
                    number.setLength(0);
                }
                if (!nodeStack.isEmpty()) {
                    nodeStack.pop();
                }
            }
            // Considerando un input corretto se non è ne una cifra ne '[', ne ']' è ','.
            // Se il carattere è ',' vuol dire che il numero è finito ma che non è finita
            // la lista di nodi quindi si controlla se la stringa number conteneva un numero e se si
            // viene creato un nodo con quel valore.
            // Se la stack NON è vuota questo nodo è figlio di quello in cima
            // e poi viene a sua volta inserito in cima.
            else {
                if (!number.isEmpty()) {
                    Tree.Node node = new Tree.Node(Integer.parseInt(number.toString()));
                    if (!nodeStack.isEmpty()) {
                        nodeStack.peek().addChild(node);
                    }
                    nodeStack.push(node);
                    number.setLength(0);
                    if (root == null) {
                        root = node;
                    }
                }
            }
        }
        return new Tree(root);
    }


    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Errore, utilizza il formato: java Esercizio1 <file1.txt> <file2.txt>");
            return;
        }
        String pairsFile = args[0];
        String nestedListFile = args[1];

        Tree tree1 = buildFromPairs(pairsFile);
        Tree tree2 = buildFromNestedList(nestedListFile);

        //Prova di visita
        System.out.println(tree1.visit());
        System.out.println(tree2.visit());

        if (areEqual(tree1, tree2)) {
            System.out.println("I due alberi sono uguali.");
        } else {
            System.out.println("I due alberi non sono uguali.");
        }
    }

}