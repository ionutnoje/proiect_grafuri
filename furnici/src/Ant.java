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
    {
        trail[currentIndex + 1] = city;     //orasul actual
        visited[city] = true;               //se seteaza pe true in vectorul visited
    }

    protected boolean visited(int i)
    {
        return visited[i];   //returneaza starea orasului in timpul rularii
    }



    protected double trailLength(double graph[][])
    {
        double lenght = graph[trail[trailSize - 1]][trail[0]]; //retruneaza numarul de pe randul [trail[trailSize - 1]] si coloana [trail[0]]
        for(int i = 0; i < trailSize - 1; i++)  //loop prin toate orasele din graph
        {
            lenght += graph[trail[i]][trail[i + 1]];
        }
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
