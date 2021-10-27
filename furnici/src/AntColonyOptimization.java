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

        return randomMatrix;
    }


    public void startAntOptimization()
    {
        for(int i=1;i<=3;i++)
        {
            s+=("\nAttempt #" +i);
            solve();
            s+="\n";
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
        s+=("\nBest tour length: " + (bestTourLength - numberOfCities));
        s+=("\nBest tour order: " + Arrays.toString(bestTourOrder));
        return bestTourOrder.clone();
    }


    private void setupAnts()
    {
        for(int i=0;i<numberOfAnts;i++)
        {
            for(Ant ant:ants)
            {
                ant.clear();
                ant.visitCity(-1, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    //la fircare iteratie mutam furnicile
    private void moveAnts()
    {
        for(int i=currentIndex;i<numberOfCities-1;i++)
        {
            for(Ant ant:ants)
            {
                ant.visitCity(currentIndex,selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    //selectare urmator oras pentru fiecare furnica
    private int selectNextCity(Ant ant)
    {
        return 0;
    }

    //calculul probabilitatii pentru alegerea urmatourlui oras
    public void calculateProbabilities(Ant ant)
    {

    }


    //update la drumurile deja traversate
    private void updateTrails()
    {

    }

    //update la cararea cea mai buna
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
                bestTourOrder = a.trail.clone();
            }
        }
    }

    //curatarea drumurilor
    private void clearTrails()
    {
        for(int i=0;i<numberOfCities;i++)
        {
            for(int j=0;j<numberOfCities;j++)
                trails[i][j]=c;
        }
    }
}