import java.util.*;

public class Swarm {

    private double gBest;
    private double[] gBestLocation;

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
            neighborhoods.add(new Neighborhood(neighbors));
        }

        //[a, b, c, d, e, f, g] => [g, a, b]
        Particle[] firstLooped = new Particle[3];
        firstLooped[0] = this.particles[this.particles.length - 1];
        firstLooped[1] = this.particles[0];
        firstLooped[2] = this.particles[1];
        neighborhoods.add(new Neighborhood(firstLooped));

        //[a, b, c, d, e, f, g] => [f, g, a]
        Particle[] secondLooped = new Particle[3];
        secondLooped[0] = this.particles[this.particles.length - 2];
        secondLooped[1] = this.particles[this.particles.length - 1];
        secondLooped[2] = this.particles[0];
        neighborhoods.add(new Neighborhood(secondLooped));


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

                if(a < 0) a = particles.length - 1; //if top row, set new y index to bottom
                if(b > particles.length - 1) b = 0; //if rightmost, set new x index to left
                if(c > particles.length - 1) c = 0; //if bottom row, set new y index to top
                if(d < 0) d = particles.length - 1; //if leftmost, set new x index to right

                neighbors[1] = vnParticles[x][a]; //above
                neighbors[2] = vnParticles[b][j]; //right
                neighbors[3] = vnParticles[x][c]; //below
                neighbors[4] = vnParticles[d][y]; //left
                neighborhoods.add(new Neighborhood(neighbors));
            }
        }

    }


   public double calculateNewGlobalBest() {
        double gBest = neighborhoods.get(0).neighbors[0].pBest; //minimum it could be
        double gBestLocation[] = neighborhoods.get(0).neighbors[0].pBestLocation;

        for (Neighborhood neighborhood : neighborhoods) {
            neighborhood.updateNBest();
            if (neighborhood.nBestValue < gBest) {
                gBest = neighborhood.nBestValue;
                gBestLocation = neighborhood.nBestLoc;
            }
        }
        this.gBest = gBest;
        this.gBestLocation = gBestLocation;
        return gBest;
   }

}
