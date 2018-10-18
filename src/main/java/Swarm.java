import java.lang.reflect.Array;
import java.util.*;

public class Swarm {

    private double gBestValue;
    private double[] gBestLocation;

    private Particle[] particles;
    private Hashtable<Particle, Neighborhood> neighborhoodDict = new Hashtable<Particle, Neighborhood>();

    //topology types
    private final int GLOBAL = 1;
    private final int RING = 2;
    private final int VON_NEUMANN = 3;
    private final int RANDOM = 4;


    public int topology;


    Swarm(String topology, String function, int swarmSize, int numDimensions){

        this.particles = new Particle[swarmSize];


        for(int i = 0; i < swarmSize; i++){
            particles[i] = new Particle(function, numDimensions);
        }



        if (topology.equals("gl")) {
            createGlobalNeighborhood();
        }

        if (topology.equals("ri")) {
            createRingNeighborhoods();
        }

        if (topology.equals("vn")) {
            createVonNeumannNeighborhoods();
        }

        if (topology.equals("ra")) {
            createRandomNeighborhoods();
        }
    }


    private void createGlobalNeighborhood() {
        Neighborhood neighborhood = new Neighborhood(this.particles);
        for (Particle p : this.particles) {
            neighborhoodDict.put(p, neighborhood);
        }
    }


    private void createRandomNeighborhoods() {

        Neighborhood newN;
        for (int i = 0; i < particles.length; i++) {
            newN = createRandomNeighborhood(particles[i]);
            neighborhoodDict.put(particles[i], newN);
        }
    }


    private Neighborhood createRandomNeighborhood(Particle p) {
        int size = 5;
        Particle[] neighbors = new Particle[size];
        neighbors[0] = p;

        HashSet<Particle> remaining = new HashSet<Particle>(Arrays.asList(this.particles));
        remaining.remove(p);

        Particle newP;

        for (int i = 1; i < size; i++) {
            newP = selectRandomParticle(remaining);
            neighbors[i] = newP;
            remaining.remove(newP);
        }
        return new Neighborhood(neighbors);

    }

    private Particle selectRandomParticle(HashSet<Particle> remaining) {

        while (true) {
            int randomIndex = (int) (Math.random() * particles.length);
            if (! remaining.contains(particles[randomIndex])) {
                return particles[randomIndex];
            }
        }
    }


    private void createRingNeighborhoods() {

        //only interior particles in array
        for (int i = 1; i < particles.length - 1; i++) {
            Particle[] neighbors = new Particle[3];
            neighbors[0] = this.particles[i - 1];
            neighbors[1] = this.particles[i];
            neighbors[2] = this.particles[i + 1];
            neighborhoodDict.put(this.particles[i], new Neighborhood(neighbors));
        }

        //[a, b, c, d, e, f, g] => [g, a, b]
        Particle[] firstLooped = new Particle[3];
        firstLooped[0] = this.particles[this.particles.length - 1];
        firstLooped[1] = this.particles[0];
        firstLooped[2] = this.particles[1];
        neighborhoodDict.put(this.particles[0], new Neighborhood(firstLooped));

        //[a, b, c, d, e, f, g] => [f, g, a]
        Particle[] secondLooped = new Particle[3];
        secondLooped[0] = this.particles[this.particles.length - 2];
        secondLooped[1] = this.particles[this.particles.length - 1];
        secondLooped[2] = this.particles[0];
        neighborhoodDict.put(this.particles[this.particles.length - 1], new Neighborhood(secondLooped));

        System.out.println("Neighborhoods: " + neighborhoodDict);
    }


    public void createVonNeumannNeighborhoods() {

        int numRows = (int) Math.sqrt(particles.length);
        Particle[][] vnParticles = new Particle[numRows][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                vnParticles[i][j] = particles[i + (numRows * j)];
            }
        }


        for (int i = 0; i < vnParticles.length; i++) {

            for (int j = 0; j < vnParticles[0].length; j++) {

                Particle[] neighbors = new Particle[5];

                neighbors[0] = vnParticles[i][j];

                int a = j - 1; //y index of above
                int b = i + 1; //x index of right
                int c = j + 1; //y index of below
                int d = i - 1; //x index of left

                if (a < 0) a = vnParticles.length - 1; //if top row, set new y index to bottom
                if (b > vnParticles.length - 1) b = 0; //if rightmost, set new x index to left
                if (c > vnParticles.length - 1) c = 0; //if bottom row, set new y index to top
                if (d < 0) d = vnParticles.length - 1; //if leftmost, set new x index to right


                neighbors[1] = vnParticles[i][a]; //above
                neighbors[2] = vnParticles[b][j]; //right
                neighbors[3] = vnParticles[i][c]; //below
                neighbors[4] = vnParticles[d][j]; //left

                neighborhoodDict.put(vnParticles[i][j], new Neighborhood(neighbors));
                System.out.println(neighborhoodDict);
            }
        }
    }

    /**
     * Calculates the new global best in the swarm.
     * Sets new global best value and location
     * @return new global best value
     */
    private double calculateNewGlobalBest() {

        double gBestValue = particles[0].getPBestValue();
        double[] gBestLocation = particles[0].location;

        for (Particle p : neighborhoodDict.keySet()) {

            if (p.getPBestValue() < gBestValue) {
                gBestValue = p.getPBestValue();
                gBestLocation = p.getPBestLocation();
            }
        }

        this.gBestValue = gBestValue;
        this.gBestLocation = gBestLocation;

        return gBestValue;

   }

    /**
     * Executes one iteration of the PSO. Gives each particle its neighborhood
     */
   public void move(){

     for (Particle particle : particles) {

         Neighborhood n = neighborhoodDict.get(particle);

         n.updateNBest();

         particle.move(neighborhoodDict.get(particle).getNBestLocation()); //pass on neighborhood best

         if(topology == RANDOM && Math.random() < 0.2){
             neighborhoodDict.put(particle, createRandomNeighborhood(particle));
         }

         n.updateNBest();
     }

     calculateNewGlobalBest();



   }

    public double getgBestValue() {
        return gBestValue;
    }
}
