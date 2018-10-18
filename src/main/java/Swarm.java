import java.lang.reflect.Array;
import java.util.*;

public class Swarm {

    private double gBestValue;
    private double[] gBestLocation;

    private Particle[] particles;
    private Hashtable<Particle, Neighborhood> neighborhoodDict;

    //topology types
    private final int GLOBAL = 1;
    private final int RING = 2;
    private final int VON_NEUMANN = 3;
    private final int RANDOM = 4;

    int interationNum = 0;

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
            neighborhoodDict.put(p,neighborhood);
        }
    }


    private Neighborhood[] createRandomNeighborhoods() {
        Neighborhood[] neighborhoods = new Neighborhood[this.particles.length];
        Neighborhood newN;
        for (int i = 0; i < neighborhoods.length; i++) {
            newN = createRandomNeighborhood(particles[i]);
            neighborhoods[i] = newN;
        }
        return neighborhoods;
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


        for (int i = 1; i < particles[0].location.length - 1; i++) {
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
        neighborhoodDict.put(this.particles[this.particles.length], new Neighborhood(secondLooped));

    }


    public void createVonNeumannNeighborhoods() {

        int numRows = (int) Math.sqrt(particles.length);
        Particle[][] vnParticles = new Particle[numRows][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                vnParticles[i][j] = particles[i + (numRows * j)];
            }
        }


        for (int i = 0; i < particles.length; i++) {
            for (int j = 0; j < particles.length; j++) {
                Particle[] neighbors = new Particle[5];
                int x = i;
                int y = j;
                neighbors[0] = vnParticles[x][y];

                int a = j + 1; //above
                int b = i + 1; //right
                int c = j - 1; //below
                int d = i - 1; //left

                if (a < 0) a = particles.length - 1; //if top row, set new y index to bottom
                if (b > particles.length - 1) b = 0; //if rightmost, set new x index to left
                if (c > particles.length - 1) c = 0; //if bottom row, set new y index to top
                if (d < 0) d = particles.length - 1; //if leftmost, set new x index to right

                neighbors[1] = vnParticles[x][a]; //above
                neighbors[2] = vnParticles[b][j]; //right
                neighbors[3] = vnParticles[x][c]; //below
                neighbors[4] = vnParticles[d][y]; //left
                neighborhoodDict.put(vnParticles[x][y], new Neighborhood(neighbors));
            }
        }
    }

    /**
     * Calculates the new global best in the swarm.
     * Sets new global best value and location
     * @return new global best value
     */
    private double calculateNewGlobalBest() {
        double gBestValue = neighborhoodDict.get(particles[0]).getNBestValue(); //minimum it could be
        double[] gBestLocation = neighborhoodDict.get(particles[0]).getNBestLocation();

        for ( Neighborhood n : neighborhoodDict.values() ) {
            n.updateNBest();
            if (n.getNBestValue() < gBestValue) {
                gBestValue = n.getNBestValue();
                gBestLocation = n.getNBestLocation();
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

         particle.move(neighborhoodDict.get(particle).getNBestLocation()); //pass on neighborhood best

         if(topology == RANDOM && Math.random() < 0.2){
             neighborhoodDict.put(particle, createRandomNeighborhood(particle));
         }
     }

     calculateNewGlobalBest();

   }
}
