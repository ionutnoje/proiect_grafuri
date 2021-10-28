


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

/*
 * default
 * private double c = 1.0;             //number of trails
 * private double alpha = 1;           //pheromone importance
 * private double beta = 5;            //distance priority
 * private double evaporation = 0.5;
 * private double Q = 500;             //pheromone left on trail per ant
 * private double antFactor = 0.8;     //no of ants per node
 * private double randomFactor = 0.01; //introducing randomness
 * private int maxIterations = 1000;
 */

public class AntColonyOptimization
{
    public String s = " ";
    private double c;                   //reprezinta val de inceput pentru feromon pe fiecare drum
    private double alpha;               //importanta feromonului
    private double beta;                //prioritatea distantei
    private double evaporation;         //coeficientul de evaporare
    private double Q;                   //feromonul lasat de furnici pe drum
    private double antFactor;           //numarul de furnici per nod(adica oras)
    private double randomFactor;        //factorul de random la alegerea drumului

    private int maxIterations;          //numarul de iteratii
    private int numberOfCities;         //numarul de orase
    private int numberOfAnts;           //numarul de furnici
    private double graph[][];           //matricea graph
    private double trails[][];          //matrice cu valorile feromonilor dintre orase
    private List<Ant> ants = new ArrayList<>();   //o liste de vectori
    private Random random = new Random();         //valoare generata random
    private double probabilities[];               //vector de probabilitati

    private int currentIndex;

    private int[] bestTourOrder;       //un vector de tip int in care se stocheaza ordinea de traversarea a oraselor din cel  mai bun run
    private double bestTourLength;      //lungimea celui mai bun run(celei mai bune rute)

    public AntColonyOptimization(double tr, double al, double be, double ev,
                                 double q, double af, double rf, int iter, int noOfCities)
    {
        c=tr; alpha=al; beta=be; evaporation=ev; Q=q; antFactor=af; randomFactor=rf; maxIterations=iter;

        graph = generateRandomMatrix(noOfCities);
        numberOfCities = noOfCities;
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];

        for(int i=0;i<numberOfAnts;i++)
            ants.add(new Ant(numberOfCities));
    }


    public double[][] generateRandomMatrix(int n)//functie care seteaza distante random intre orase
    {
        double[][] randomMatrix = new double[n][n];

        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(i==j)
                    randomMatrix[i][j]=0;
                else
                    randomMatrix[i][j]=Math.abs(random.nextInt(100)+1);
            }
        }


        //afisarea matricei drumurilor

        s+=("\t");
        for(int i=0;i<n;i++)
            s+=(i+"\t");
        s+="\n";

        for(int i=0;i<n;i++)
        {
            s+=(i+"\t");
            for(int j=0;j<n;j++)
                s+=(randomMatrix[i][j]+"\t");
            s+="\n";
        }


        return randomMatrix;
    }

    /**
     * Perform ant optimization
     */
    public void startAntOptimization()//functia cu care se porneste programul
    {
        for (int i = 0; i <= 3; i++)
        {
            System.out.println("varianta " + i + " de raspuns");
            solve();

        }

    }


    public int[] solve()
    {
        setupAnts();//se pun toate orasele pe false in vectorul visited apoi se alege un oras de unde sa se inceapa programul
        clearTrails();//seteaza feromonul initial pentru fiecare oras la inceput de program

        for(int i=0;i<maxIterations;i++)//pentru fiecare iteratie o sa se mute furnicile la urmatorul oras prin apelul functiei moveAnts()
                                        // apoi o sa se updateze valoarea de feromon pentru fiecare oras prin apelul functiei updateTrails()
                                        //apoi se updateaza cel mai bun drum cu cea mai buna distanta prin apelul functiei updateBest()
        {
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nRuta : " + (bestTourLength));//afisam cea mai buna distanta
        s+=("\nLungimea rutei: " + Arrays.toString(bestTourOrder));//se afiseaza ruta cea mai buna
        return bestTourOrder;
    }


    private void setupAnts()//la inceput de program pentru fiecare obiect de tip furnica din array list se pun toate orasele vizitate pe false prin apelarea metodei clear din clasa Ant,
    {                       // apoi se seteaza orasul de unde incepe ant optimization prin apelul metodei visitCity
                            // variabila currentIndex este setata pe 0, ea ne ajuta pe parcursul programului sa retinem prin cate orase s a trecut
        for(int i=0;i<numberOfAnts;i++)
        {
            for(Ant ant:ants)
            {
                ant.clear();
                ant.visitCity(-1, 0);
            }
        }
        currentIndex = 0;
    }


    private void moveAnts()//functie care la fiecare iteratie o sa mute fiecare furnica la urmatorul oras
    {
        for(int i=currentIndex;i<numberOfCities-1;i++)//for care porneste de la nr de orase deja vizitate si pana la nr total de orase
                                                        //currentIndex este folosit ca un fel de contor pentru nr de orase deja vizitate
        {
            for(Ant ant:ants)//for each prin obiectele de tip furnica
            {
                ant.visitCity(currentIndex,selectNextCity(ant));//pentru fiecare furnica o sa se aleaga urmatorul oras prin apelarea functiei visitCity
            }
            currentIndex++;
        }
    }

    /**
     * Select next city for each ant
     */
    private int selectNextCity(Ant ant)//functia cu care se selecteaza urmatrul oras
    {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor)
        {
            int cityIndex=-1;
            for(int i=0;i<numberOfCities;i++)
            {
                if(i==t && !ant.visited(i))
                {
                    cityIndex=i;
                    break;
                }
            }
            if(cityIndex!=-1)
                return cityIndex;
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfCities; i++)
        {
            total += probabilities[i];
            if (total >= r)
                return i;
        }
        throw new RuntimeException("There are no other cities");
    }

    /**
     * Calculate the next city picks probabilites
     */
    public void calculateProbabilities(Ant ant)//functia care calculeaza probabilitatea de a alege un oras
    {
        int i = ant.trail[currentIndex];//valoarea de feromon din orasul curent
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++)
        {
            if (ant.visited(l) == false)
            {
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);//formula cu care se calculeaza peromonul pentru urmatorul oras
            }
        }
        for (int j = 0; j < numberOfCities; j++)
        {
            if (ant.visited(j) == true)
                probabilities[j] = 0.0;
            else
            {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails()//dupa fiecare run o sa se schimbe val feromonului ca dupa un timp anumite rute sa ramana fara
                                // deci sa nu mai prezinte interes pentru furnici si pentru viitoare run uri
    {
        for (int i = 0; i < numberOfCities; i++)
        {
            for (int j = 0; j < numberOfCities; j++)
            {
                trails[i][j] = trails[i][j] * evaporation;//aici se scade val feromonului pentru fiecare drum dintre orase
            }

        }
        for (Ant a : ants)//for each in care pentru fiecare furnica se specifica cat feromon sa fie lasat pe drum
        {
            double contribution = Q / a.trailLength(graph);//aici este val de feromon pe care o sa o lase furnica pe drum
            for (int i = 0; i < numberOfCities - 1; i++)
            {
                trails[a.trail[i]][a.trail[i + 1]] = trails[a.trail[i]][a.trail[i + 1]] * contribution;//valoarea deja existenta de feromon o sa se inmulteazca cu val de feromon lasata de furnica

            }
            trails[a.trail[numberOfCities - 1]][a.trail[0]] = trails[a.trail[numberOfCities - 1]][a.trail[0]] + contribution;//valoarea de feromon dintre primul oras si ultimul(banuiesc ca toata ruta dintre ele...trecand prin fiecare oras)
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest()
    {
        if (bestTourOrder == null)//daca vectorul cu ordinea cea mai buna este gol, adica este la prima interatie se stocheaza ordinea
        {
            bestTourOrder = ants.get(0).trail;  //se ia ordinea
            bestTourLength = ants.get(0).trailLength(graph);//se ia cea mai buna distanta
        }
        else
        {
            for (Ant a : ants)//pentru fiecare obiect de tip furnica din array ul de furnici se ia distanta di ordinea oraselor
            {
                if (a.trailLength(graph) < bestTourLength)//daca distanta din noul run este mai scurta ca si cea anterioara se seteaza pe noua valoare
                {
                    bestTourLength = a.trailLength(graph);
                    bestTourOrder = a.trail;
                }
            }
        }


    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails()//se seteaza dupa fiecare tura pe fiecare drum dintre orase o val pentru feromon...o valoare standard 1.0...ca sa se poate parcurge codul de la alegerea orasului
    {
        for(int i=0;i<numberOfCities;i++)
        {
            for(int j=0;j<numberOfCities;j++)
            {
                trails[i][j]=c;//c este constanta c care reprezinta val de inceput de feromon pe fiecare drum dintre fiecare nod
            }

        }
    }
}