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


    private Particle findNearestParticle

}
