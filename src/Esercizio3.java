/*
NOME: ROBERTO
COGNOME: ZANOLLI
*/

import java.io.*;
import java.util.*;

public class Esercizio3 {

    //public static Random random = new Random(1070505);

    /*
     * Implementazione del grafo attraverso lista di adiacenza.
     * n è il numero di nodi e m è il numero di archi.
     */
    public static class Graph {
        public int n;
        public int m;
        public ArrayList<Edge>[] adjList;

        public Graph(int n, int m) {
            this.n = n;
            this.m = m;
            this.adjList = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                adjList[i] = new ArrayList<>();
            }
        }

        /*
         * Edge rappresenta un arco orientato con peso > 0.
         */
        public class Edge {
            public int src;
            public int dst;
            public double weight;

            public Edge(int src, int dst, double weight) {
                this.src = src;
                this.dst = dst;
                this.weight = weight;
            }
        }

        /*
         * Metodo che aggiunge un arco alla lista di adiacenza del grafo.
         */
        public void addEdge(int src, int dst, double weight) {
            adjList[src].add(new Edge(src, dst, weight));
        }

        /*
         * Metodo che restituisce l'attesa ad un incrocio.
         */
        public double attesa(int i, double t) {
            return 5.0;

        
        }

        /*
         * Metodo che implementa una variante dell'algoritmo di Dijkstra per trovare il percorso più breve da 0 a n-1.
         * Durante il calcolo della distanza aggiunge il risultato dell'invocazione del metodo attesa().
         * 
         * E' stato scelto questo algoritmo per la sua efficienza nel trovare il cammino di costo minimo e
         * perchè la consegna stabilisce che tutti i pesi (tempo di attraversamento) sono positivi (permettendone
         * l'applicazione).
         *
         * L'algoritmo di Dijkstra (se implementato con heap) ha un costo di O( (n+m)*log(n) ) con n = numero nodi
         * e m = numero archi.
         *
         * E' stata utilizzata la classe PriorityQueue implementata nella JDK (nella documentazione viene detto
         * che è implementata mediante minheap).
         *
         * Il Comparator permette alla PriorityQueue di associare agli interi simboleggianti i nodi le distanze
         * da usare per il confronto (come priority).
         */
        public void dijkstra(double[] dist, int[] pred, int start) {

            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            Arrays.fill(pred, -1);
            dist[start] = 0;

            PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<>() {
                @Override
                public int compare(Integer node1, Integer node2) {

                    return Double.compare(dist[node1], dist[node2]);
                }
            });

            pq.add(start);

            while (!pq.isEmpty()) {
                int u = pq.poll();

                // Se abbiamo raggiunto il nodo n-1, possiamo terminare l'algoritmo --> non serve
                // visitare tutti gli archi, la prima occorrenza del nodo n-1 indicherà che siamo
                // già in possesso del cammino di costo minimo.

                if (u == n - 1) {
                    break;
                }


                for (Edge edge : adjList[u]) {
                    int v = edge.dst;
                    double weight = edge.weight;
                    double wait = attesa(u, weight);

                    //Considero l'attesa nel controllo della distanza
                    if (dist[u] + weight + wait < dist[v]) {
                        dist[v] = dist[u] + weight + wait;
                        pred[v] = u;
                        pq.add(v);
                    }
                }
            }
        }

        /*
         * Stampa il cammino più breve da 0 a n-1.
         * Se la distanza associata a n-1 è ancora infinità significa
         * che non è stato raggiunto.
         */
        public void printPath(double[] dist, int[] pred) {

            if (dist[n - 1] == Double.POSITIVE_INFINITY) {
                System.out.println("non raggiungibile");
            } else {

                System.out.println(dist[n - 1]);

                List<Integer> path = new ArrayList<>();


                for (int i = n - 1; i != -1; i = pred[i]) {
                    path.add(i);
                }

                Collections.reverse(path);
                for (int node : path) {
                    System.out.print(node + " ");
                }
            }
        }


    }

    /*
     * Legge il grafo dal file di input e restituisce un oggetto Graph.
     */
    public static Graph readGraph(String fileName) throws FileNotFoundException {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(new File(fileName));
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Graph graph = new Graph(n, m);
        // Aggiunge archi al grafo man mano che vengono letti
        for (int i = 0; i < m; i++) {
            int src = scanner.nextInt();
            int dst = scanner.nextInt();
            double weight = scanner.nextDouble();
            graph.addEdge(src, dst, weight);
        }

        scanner.close();
        return graph;
    }

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 1) {
            System.out.println("Errore, utilizza il formato: java Esercizio3 <file.txt>");
            return;
        }

        Graph graph = readGraph(args[0]);

        double[] dist = new double[graph.n];
        int[] pred = new int[graph.n];

        graph.dijkstra(dist, pred, 0);

        graph.printPath(dist, pred);
    }
}




