public class Particle {

    Neighborhood neighborhood;
    Point pBest;
    Point location;
    Vector vector;

    public Particle(Neighborhood neighborhood, Point pBest, Point location, Vector vector) {
        this.neighborhood = neighborhood;
        this.pBest = pBest;
        this.location = location;
        this.vector = vector;
    }

    //TODO: actually write the code for this function
    void updateVector(double constrictionFactor, double phiOne, double phiTwo) {
        Point nBest = neighborhood.nBest;
        Vector randomVecOne = new Vector(pBest.dimensionVals.length, phiOne);
        Vector randomVecTwo = new Vector(pBest.dimensionVals.length, phiTwo);
        for (int i = 0; i < pBest.dimensionVals.length; i++) {
            double left = constrictionFactor * randomVecOne[i] * (pBest.dimensionVals[i] - vector[i];
        }
    }
}
