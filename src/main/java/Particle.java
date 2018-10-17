public class Particle {

    double[] location;
    double[] vector;

    double pBest;
    double[] pBestLocation;

    public Particle(double[] location, double[] vector, double pBest, double[] pBestLocation) {
        this.location = location;
        this.vector = vector;
        this.pBest = pBest;
        this.pBestLocation = pBestLocation;
    }
}
