import java.util.Arrays;
import java.util.Random;

public class Particle {

    /**
     * Current location
     */
    double[] location;

    /**
     * Current velocity vector
     */
    private double[] vector;

    /**
     * Value of particle's personal best location
     */
    private double pBestValue;

    /**
     * Array representation of every dimension in personal best location
     */
    private double[] pBestLocation;

    /**
     * Function on which particle's location will be evaluated
     */
    private String function;

    /**
     * Personal best acceleration coefficient.
     */
    private double phi1 = 2.05;

    /**
     * Global best acceleration coefficient.
     */
    private double phi2 = 2.05;

    private double phi = phi1 + phi2;

    private  double constrictionFactor = 0.7298;

    public Particle(String function, int numDimensions){
      this.function = function;
      initializeLocation(function, numDimensions);
      initializeVector(function, numDimensions);
      pBestLocation = this.location;
      pBestValue = eval();
    }


    /**
     * Initialize the location of the vector
     * @param function the function that we will minimize.
     * @param numDimensions number of dimensions to initialize the particle's loc in.
     */
    private void initializeLocation(String function, int numDimensions){
      Random rand = new Random();

        location = new double[numDimensions];
        if (function.equals("sp")) {
            for (int d = 0; d < numDimensions; d++) {
                location[d] = Math.pow(-1, rand.nextInt(2))*(16 + rand.nextDouble() * (32 - 16));
            }
        }
        if(function.equals("ack")) {
            for (int d = 0; d < numDimensions; d++) {
                location[d] = Math.pow(-1, rand.nextInt(2))*(16 + rand.nextDouble() * (32 - 16));
            }
        }
        else if(function.equals("rok")) {
            for (int d = 0; d < numDimensions; d++) {
                location[d] = Math.pow(-1, rand.nextInt(2))*(15 + rand.nextDouble() * (30 - 15));
            }
        }
        else if(function.equals("ras")) {
            for (int d = 0; d < numDimensions; d++) {
                location[d] = Math.pow(-1, rand.nextInt(2))*(2.56 + rand.nextDouble() * (5.12 - 2.56));
            }
        }
    }


    /**
     * Initialize a random vector based on the function and the number of
     * dimensions.
     * @param function type of function we will evaluate. impacts initialization.
     * @param numDimensions number of dimensions to initialize the vector in.
     */
    private void initializeVector(String function, int numDimensions){
        Random rand = new Random();
        vector = new double[numDimensions];
        if(function.equals("sp")) {
            for (int d = 0; d < numDimensions; d++) {
                vector[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
        }
        if(function.equals("ack")) {
            for (int d = 0; d < numDimensions; d++) {
                vector[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
        }
        else if(function.equals("rok")) {
            for (int d = 0; d < numDimensions; d++) {
                vector[d] = -2 + rand.nextDouble() * Math.abs(-2 - 2);
            }
        }
        else if(function.equals("ras")) {
            for (int d = 0; d < numDimensions; d++) {
                vector[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
        }
    }

    // returns the value of the specified function for point (x, y)
    private double eval() {
        double retValue = 0;
        if (function.equals("sp")) {
            retValue = evalSphere(location);
        } else if (function.equals("rok")) {
            retValue = evalRosenbrock(location);
        } else if (function.equals("ras")) {
              retValue = evalRastrigin(location);
        } else if (function.equals("ack")) {
              retValue = evalAckley(location);
        }
        if(retValue <= pBestValue){
            pBestValue = retValue;
            pBestLocation = location.clone();
        }
        return retValue;
    }


    void move(double[] nBestLoc){

        Random rand = new Random();

        for (int d = 0; d < location.length; d++) {

            // ****** compute the acceleration due to personal best
            double PBestAttract = pBestLocation[d] - location[d];

            PBestAttract *= rand.nextDouble() * phi1;

            // ****** compute the acceleration due to global best
            double GBestAttract = nBestLoc[d] - location[d];

            GBestAttract *= rand.nextDouble() * phi2;

            // ****** constrict the new velocity and reset the current velocity
            vector[d] += PBestAttract + GBestAttract;

            vector[d] *= constrictionFactor;

            // ****** update the position
            location[d] += vector[d];
          }
          eval();
    }



    // returns the value of the Sphere function at point (x, y)
    //   minimum is 0.0, which occurs at (0.0,...,0.0)
    public double evalSphere (double[] x) {
      double sum = 0;
      for (int d = 0; d < location.length; d++) {
        sum += (x[d] * x[d]);
      }
      return sum;
    }



    // returns the value of the Rosenbrock Function at point (x, y)
    //   minimum is 0.0, which occurs at (1.0,...,1.0)
    public double evalRosenbrock (double[] dimensionVals) {

      double counter = 0;
      for (int i = 0; i < dimensionVals.length - 1; i++) {
        double leftSide = 100 * Math.pow(dimensionVals[i + 1] - Math.pow(dimensionVals[i], 2), 2);
        double rightSide = Math.pow(dimensionVals[i] - 1, 2);
        counter = counter + leftSide + rightSide;
      }
      return counter;
    }


    // returns the value of the Rastrigin Function at point (x, y)
    //   minimum is 0.0, which occurs at (0.0,...,0.0)
    public double evalRastrigin (double[] dimensionVals) {


      double counter = 0;
      for (double i : dimensionVals) {
        counter += Math.pow(i, 2) - (10 * Math.cos(2 * Math.PI * i));
      }
      return (10 * dimensionVals.length) + counter;

    }

    // returns the value of the Ackley Function at point (x, y)
    //   minimum is 0.0, which occurs at (0.0,...,0.0)
    private double evalAckley (double[] dimensionVals) {

      double a = 20.0;
      double b = .2;
      double c = 2 * Math.PI;

      double firstExp = 0;
      double secondExp = 0;
      for (double i : dimensionVals) {
        firstExp += Math.pow(i, 2);
        secondExp += Math.cos(c * i);
      }

      firstExp = firstExp * (1.0 / dimensionVals.length);
      firstExp = Math.sqrt(firstExp);
      firstExp = -b * firstExp;

      secondExp = secondExp * (1.0 / dimensionVals.length);

      return (-a * Math.exp(firstExp)) - Math.exp(secondExp) + a + Math.E;
    }

    /**
     * Getter for personal best value
     * @return pBestVal
     */
    public double getPBestValue() {
        return pBestValue;
    }


    /**
     * Getter for personal best location.
     * @return location array
     */
    public double[] getPBestLocation() {
        return pBestLocation;
    }


    /**
     * toString
     * @return string representation of Particle -> its location.
     */
    public String toString() {
        return Arrays.toString(location);
    }

}
