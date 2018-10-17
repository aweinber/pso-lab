import java.util.ArrayList;

public class Neighborhood {

    double nBest;
    ArrayList<Particle> particles;

    public Neighborhood(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public Neighborhood() {
    }

    public void addParticle(Particle p) {
        this.particles.add(p);
    }
}
