import java.lang.reflect.Array;
import java.util.*;

public class Swarm {

    /**
     * Global best value
     */
    private double gBestValue;

    /**
     * Global best location
     */
    private double[] gBestLocation;

    /**
     * Particles that comprise swarm
     */
    private Particle[] particles;

    /**
     * Mapping of all particles to their neighborhoods
     */
    private Hashtable<Particle, Neighborhood> neighborhoodDict = new Hashtable<Particle, Neighborhood>();

    /**
     * Global neighborhood topology. All particles in each particle's neighborhood.
     */
    private final int GLOBAL = 1;

    /**
     * Ring neighborhood topology. Only left and right neighbors in each's neighborhood.
     */
    private final int RING = 2;

    /**
     * VN topology. Above, below, to the left, and to the right comprise a particle's
     * neighborhood.
     */
    private final int VON_NEUMANN = 3;

    /**
     * Random topology. Randomly selected particles comprise a particle's neighborhood.
     */
    private final int RANDOM = 4;

    /**
     * Field that represents which topology the swarm uses.
     */
    private int topology;


    /**
     * Constructor. Given topology type, function, swarmsize, and numDimensions,
     * create a swarm of particles and their attendant neighborhoods.
     * @param particles the particles in the swarm
     * @param topology determines neighborhood inclusion
     *
     */
    Swarm(Particle[] particles, String topology){

        this.particles = particles;

        if (topology.equals("gl")) { this.topology = GLOBAL; }
        if (topology.equals("ri")) { this.topology = RING; }
        if (topology.equals("vn")) { this.topology = VON_NEUMANN; }
        if (topology.equals("ra")) { this.topology = RANDOM; }

    }

    /**
     * Creates the neighborhoods required for the swarm to update.
     */
    public void initializeNeighborhoods() {

        if (topology == GLOBAL) { createGlobalNeighborhood(); }
        if (topology == RING) { createRingNeighborhoods(); }
        if (topology == VON_NEUMANN) { createVonNeumannNeighborhoods(); }
        if (topology == RANDOM) { createRandomNeighborhoods(); }

    }


    /**
     * Creates neighborhoods such that every particle's neighborhood is
     * every other neighborhood.
     */
    private void createGlobalNeighborhood() {
        Neighborhood neighborhood = new Neighborhood(this.particles);
        for (Particle p : this.particles) {
            neighborhoodDict.put(p, neighborhood);
        }
    }


    /**
     * Creates random neighborhoods for each particle in the swarm.
     * Saves each in a Hashmap with particle -> neighborhood association.
     */
    private void createRandomNeighborhoods() {

        Neighborhood newN;
        for (Particle particle : particles) {
            newN = createRandomNeighborhood(particle);
            neighborhoodDict.put(particle, newN);
        }
    }

    /**
     * Creates a random neighborhood of size k and map particle p to it.
     * Neighborhood cannot include p itself.
     * TODO: decide what size- why is it 5? parameterize this!
     * @param p is the particle to build a neighborhood around.
     * @return the neighborhood.
     */
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


    /**
     * Selects a random particle from the hashset of remaining particles that
     * can be chosen.
     * @param remaining a hashset of particles that remain available
     * @return one such particle
     */
    private Particle selectRandomParticle(HashSet<Particle> remaining) {

        while (true) {
            int randomIndex = (int) (Math.random() * particles.length);
            if (! remaining.contains(particles[randomIndex])) {
                return particles[randomIndex];
            }
        }
    }


    /**
     * Creates Ring neighborhoods and saves them. Imagines a particle's neighborhood
     * as the left and right of the particle in an array. Protects against index
     * out of bounds with special cases where middle particle is at start or
     * end of the array, to loop around.
     */
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

    }


    /**
     * Creates a von Neumann neighborhood such that every particle
     * is connected with the particles above, below, to the right,
     * and to the left of it in an imagined square array.
     */
    public void createVonNeumannNeighborhoods() {

        int numRows = (int) Math.sqrt(particles.length);
        Particle[][] vnParticles = new Particle[numRows][numRows];

        //place particles into square
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                vnParticles[i][j] = particles[i + (numRows * j)];
            }
        }


        //find neighborhood for each particle
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
            }
        }
    }

    /**
     * Calculates the new global best in the swarm.
     * Sets new global best value and location.
     */
    private void calculateNewGlobalBest() {

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

   }

    /**
     * Executes one iteration of the PSO. Gives each particle its neighborhood.
     * For each particle in the swarm, grabs the nBest location and orders the
     * particle to move (more on that process in the Particle class). Then
     * calculates new global best.
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
