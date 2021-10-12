import java.util.*;


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

    private int[] bestTourrOrder;       //un vector de tip int in care se stocheaza ordinea de traversarea a oraselor din cel  mai bun run
    private double bestTourLenght;      //lungimea celui mai bun run(celei mai bune rute)




    public AntColonyOptimization(double Lnumber_of_trails, double Lpheromon_importance, double Ldistance_prioritity, double Levaporation_factor,
                                 double Lpheromon_left_by_ants, double Lant_factor, double Lrandom_factor, int Lnumber_of_iterations, int Lnumber_of_cities)
    {
        Lnumber_of_trails = c;
        Lpheromon_importance = alpha;
        Ldistance_prioritity = beta;
        Levaporation_factor = evaporation;
        Lpheromon_left_by_ants = Q;
        Lant_factor = antFactor;
        Lrandom_factor = randomFactor;
        Lnumber_of_iterations = maxIterations;
        Lnumber_of_cities = numberOfCities;



        graph = generateRandomMatrix(Lnumber_of_cities);
        numberOfCities = Lnumber_of_cities;
        numberOfAnts = (int)(numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];

        for(int i = 0; i < numberOfAnts; i++)
        {
            ants.add(new Ant(numberOfCities));
        }


    }










}
