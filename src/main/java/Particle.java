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

    public double getPBest() {
        return pBest;
    }

    public void setpBest(double pBest) {
        this.pBest = pBest;
    }

    public double[] getPBestLocation() {
        return pBestLocation;
    }

    public void setpBestLocation(double[] pBestLocation) {
        this.pBestLocation = pBestLocation;
    }
}
