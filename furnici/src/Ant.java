public class Ant
{
    protected int trailSize;       //marimea tuturor drumurilor...absolut toate drumurile dintre toate orasele
    protected int trail[];          //marimea drumului pe care se afla actual furnicile
    protected boolean visited[];



    public Ant(int tourSize)                //functia de initializare a drumului si vectorului de orase vizitate sau nevizitate
    {
        this.trailSize = tourSize;          //se initializeaza marimea drumului
        this.trail = new int[tourSize];     // se updateaza in timpul codului cu numarul de orase prin care a trecut
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
        return visited[i];   //returneaza starea orasului in timpul rularii
    }



    protected double trailLength(double graph[][])
    {
        double lenght = 0;//retruneaza numarul de pe randul [trail[trailSize - 1]] si coloana [trail[0]]
//        System.out.println("graph[trail[trailSize - 1]][trail[0]]" + graph[trail[trailSize - 1]][trail[0]]);
//        System.out.println("trailsize" + trailSize);//trailsize este numerul de noduri
        for(int i = 0; i < trailSize - 1; i++)  //loop prin toate orasele din graph
        {
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
    }




}
