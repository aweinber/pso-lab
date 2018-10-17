import java.util.*;

public class Swarm {

    private Particle[] particles;
    private ArrayList<Neighborhood> neighborhoods;

    public Swarm(Particle[] particles, ArrayList<Neighborhood> neighborhoods) {
        this.particles = particles;
        this.neighborhoods = neighborhoods;
    }

    public void createGlobalNeighborhood() {
        Neighborhood neighborhood = new Neighborhood();
        for (Particle p : particles) {
            neighborhood.addParticle(p);
        }
        this.neighborhoods = new ArrayList<Neighborhood>();
        this.neighborhoods.add(neighborhood);
    }

    public void createRandomNeighborhoods() {

    }


    public void createRingNeighborhoods() {

        Set<Particle> remainingParticles = new HashSet<Particle>();
        remainingParticles.addAll(Arrays.asList(this.particles));

        //this is an ordered list where the nearest particle to partices[i] is
        //particles[i+1] and the nearest particle to particles[i+1] is
        //particles[i+2]

        Particle[] orderedParticles = new Particle[particles.length];
        for (int i = 0; i < particles.length; i++) {
            Particle nearest = findNearestParticle(particles[i], remainingParticles);
            remainingParticles.remove(nearest);
            orderedParticles[i] = nearest;
        }


    }


    public void createVonNeumannNeighborhoods() {

    }


    private Particle findNearestParticle(Particle particle, HashSet<Particle> remainingP) {
        double minDist = 100; //TODO: size of 'board'
        Particle nearest;
        for (Particle p : remainingP) {
            double dist = calculateDistance(particle, p);
            if (dist < minDist) {
                minDist = dist;
                nearest = p;
            }
            remainingP.remove(p);
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
