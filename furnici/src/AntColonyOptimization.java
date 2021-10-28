


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
    private double c;                   //numarul de drumuri
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
    private double trails[][];
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


    public double[][] generateRandomMatrix(int n)
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
    public void startAntOptimization()
    {
        for (int i = 0; i <= 3; i++)
        {
            System.out.println("varianta " + i + " de raspuns");
            solve();

        }

    }


    public int[] solve()
    {
        setupAnts();
        clearTrails();
        for(int i=0;i<maxIterations;i++)
        {
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nRuta : " + (bestTourLength));
        s+=("\nLungimea rutei: " + Arrays.toString(bestTourOrder));
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
    private int selectNextCity(Ant ant)
    {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor)
        {
            int cityIndex=0;
            for(int i=0;i<numberOfCities;i++)
            {
                if(i==t && !ant.visited(i))
                {
                    cityIndex=i;
                    break;
                }
            }
            if(cityIndex!=0)
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
    public void calculateProbabilities(Ant ant)
    {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++)
        {
            if (!ant.visited(l))
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
        }
        for (int j = 0; j < numberOfCities; j++)
        {
            if (ant.visited(j))
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
    private void updateTrails()
    {
        for (int i = 0; i < numberOfCities; i++)
        {
            for (int j = 0; j < numberOfCities; j++)
                trails[i][j] *= evaporation;
        }
        for (Ant a : ants)
        {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++)
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest()
    {
        if (bestTourOrder == null)
        {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(graph);
        }

        for (Ant a : ants)
        {
            if (a.trailLength(graph) < bestTourLength)
            {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail;
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails()
    {
        for(int i=0;i<numberOfCities;i++)
        {
            for(int j=0;j<numberOfCities;j++)
                trails[i][j]=c;
        }
    }
}