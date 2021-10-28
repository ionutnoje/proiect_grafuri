public class Ant
{
    protected int trailSize;       //marimea tuturor drumurilor...absolut toate drumurile dintre toate orasele
    protected int trail[];          //marimea drumului pe care se afla actual furnicile
    protected boolean visited[];



    public Ant(int tourSize)                //functia de initializare a drumului si vectorului de orase vizitate sau nevizitate
    {
        this.trailSize = tourSize;          //se initializeaza marimea drumului
        this.trail = new int[tourSize];     // este un vector in care se stocheaza val feromonului pentru fiecare oras
        this.visited = new boolean[tourSize]; //vectorul de orase vizitate/nevizitate//pe parcursul programului orasele prin care s a trecut o sa se seteze pe true(se tine cont de orasele vizitate si nevizitate pentru viitoare parcurgeri)
    }

    protected void visitCity(int currentIndex, int city)     //functie care seteaza orasul curent pe oras vizitat cu ajutorul functiei visited
                                                              //in functie se dau currentIndex care este nr de orase deja vizitate
                                                            //si city care este generat aleator de functia selectNextCity din clasa antcolonyoptimization
                                                            //astfel prin trail[currentIndex + 1] = city....urmatourl spatiu din vectorul de drum este setat pe valoarea city primita random
    {
        trail[currentIndex + 1] = city;     //spatiul urmator din vectorul drumului o sa fie setat cu valoarea orasului ales random cu functia selectNextCity
        visited[city] = true;               //se seteaza valoarea in vectorul visited ca fiind true pentru orasul respectiv
    }

    protected boolean visited(int i)
    {
        return visited[i];   //o sa returneze in timpul rularii daca orasul respectiv a fost deja vizitat sau nu
    }



    protected double trailLength(double graph[][])
    {
        double lenght = 0;//se instantiaza distanta pe 0

        for(int i = 0; i < trailSize - 1; i++)  //loop prin toate nodurile din graph ul oraselor
        {
            System.out.println("\ngraph[trail[ " + i + "]][trail["+i + 1+"]]" + graph[trail[i]][trail[i + 1]]);
            System.out.println("trail[i] " + trail[i]);
            System.out.println("trail[i + 1]" + trail[i + 1]);
            System.out.println("graph[trail[i]][trail[i + 1]]" + graph[trail[i]][trail[i + 1]]);
            lenght += graph[trail[i]][trail[i + 1]];
        }
        System.out.println("se returneaza lenghtul de : " + lenght);
        return lenght;
    }

    protected void clear()        //functie care seteaza orasele pe false...pentru urmatoarea tura de furnici
    {
        for(int i = 0; i < trailSize; i++)   //loop prin toate orasele din toate nodurile
        {
            visited[i] = false;             //setarea oraselor pe false
        }
        System.out.println("trailsize = " + trailSize);
    }




}
