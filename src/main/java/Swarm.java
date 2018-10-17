import java.util.*;

public class Swarm {

    private int topology;
    private Particle[] particles;
    private ArrayList<Neighborhood> neighborhoods;

    //topology types
    private final int GLOBAL = 1;
    private final int RING = 2;
    private final int VON_NEUMANN = 3;
    private final int RANDOM = 4;

    public int topologyType;


    public Swarm(Particle[] particles, ArrayList<Neighborhood> neighborhoods) {
        this.particles = particles;
        this.neighborhoods = neighborhoods;
    }


    public void createNeighborhoods() {
        if (topology == GLOBAL) {
            createGlobalNeighborhood();
        }
        else if (topology == RING) {
            createRingNeighborhoods();
        }
        else if (topology == VON_NEUMANN) {
            createVonNeumannNeighborhoods();
        }
        else if (topology == RANDOM) {
            createRandomNeighborhoods();
        }
    }

    public void createGlobalNeighborhood() {
        Neighborhood neighborhood = new Neighborhood(this.particles);
        this.neighborhoods = new ArrayList<Neighborhood>();
        this.neighborhoods.add(neighborhood);
    }

    public void createRandomNeighborhoods() {

    }


    public void createRingNeighborhoods() {

        for (int i = 1; i < particles[0].location.length - 1; i++) {
            Particle[] neighbors = new Particle[3];
            neighbors[0] = this.particles[i - 1];
            neighbors[1] = this.particles[i];
            neighbors[2] = this.particles[i + 1];
            Neighborhood neighborhood = new Neighborhood(neighbors);
            neighborhoods.add(neighborhood);
        }
        Particle[] firstLooped = new Particle[3];
        firstLooped[0] = this.particles[this.particles.length - 1];
        firstLooped[1] = this.particles[0];
        firstLooped[2] = this.particles[1];
        Neighborhood neighborhood = new Neighborhood(firstLooped);
        neighborhoods.add(neighborhood);


        Particle[] secondLooped = new Particle[3];
        secondLooped[0] = this.particles[this.particles.length - 2];
        secondLooped[1] = this.particles[this.particles.length - 1];


    }


    public void createVonNeumannNeighborhoods() {

    }


    private Particle findNearestParticle(Particle particle, HashSet<Particle> remainingP) {
        HashSet<Particle> tempRemaining = remainingP;
        double minDist = 100; //TODO: size of 'board'
        Particle nearest = null;
        for (Particle p : tempRemaining) {
            double dist = calculateDistance(particle, p);
            if (dist < minDist) {
                minDist = dist;
                nearest = p;
            }
            tempRemaining.remove(p);
        }

        return nearest;
    }

    private double calculateDistance(Particle fromP, Particle toP) {
        double counter = 0.0;
        for (int dim = 0; dim < fromP.location.length; dim++) {
            counter += Math.pow(fromP.location[dim] - toP.location[dim], 2);
        }
        return Math.sqrt(counter);
    }

}
