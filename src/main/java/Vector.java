public class Vector {

    double[] components;


    public Vector(double[] components) {
        this.components = components;
    }

    public Vector(int numDimensions, double max) {
        this.components = new double[numDimensions];
        for (int i = 0; i < numDimensions; i++) {
            components[i] = Math.random() * max;
        }
    }
}
