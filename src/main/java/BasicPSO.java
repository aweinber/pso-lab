/*

 Basic Particle Swarm Optimization

 Stephen Majercik
 2013 April 6

*/

// for random numbers
import java.util.Random;
import java.util.Arrays;


public class BasicPSO {

  // ****************  GRAPHICS  ******************
  // window dimensions
  private final int WINDOW_WIDTH = 1000;
  private final int WINDOW_HEIGHT = WINDOW_WIDTH;

  // make background black
  private final int BACKGROUND_ALPHA = 0;

  // adjust for personal preference
  private final int PARTICLE_SIZE = 10;


  // ****************  MISCELLANEOUS    ******************

  // for random numbers
  private Random rand = new Random();


  // ****************  PSO  ******************

  // x and y positions for each particle
  private double[][] loc;

  // x and y velocities for each particle
  private double[][] vel;

  // pBest positions and values for each particle
  private double[][] pBestLoc;
  private double[] pBestValue;

  private Neighborhood[] neighborhoods;

  // gbest position and value
  private double[] gBestLoc;
  private double gBestValue;

  // initial speed range
  private final double MIN_INIT_SPEED = -3.0;
  private final double MAX_INIT_SPEED = 3.0;

  // shifts the optimum so that it is in the center of the window
  private final double FUNCTION_SHIFT = WINDOW_WIDTH/2.0;
  // establishes a zone around the optimum that is off limits for particles when the
  // swarm is initialized (to make it a little harder)
  private final double NO_INIT_ZONE_SIDE = 100;
  private final double NO_INIT_ZONE_LEFT_COORD = FUNCTION_SHIFT - NO_INIT_ZONE_SIDE/2.0;
  private final double NO_INIT_ZONE_RIGHT_COORD = FUNCTION_SHIFT + NO_INIT_ZONE_SIDE/2.0;
  private final double NO_INIT_ZONE_TOP_COORD = FUNCTION_SHIFT - NO_INIT_ZONE_SIDE/2.0;
  private final double NO_INIT_ZONE_BOTTOM_COORD = FUNCTION_SHIFT + NO_INIT_ZONE_SIDE/2.0;

  // ******************************************************************************************
  // ****** You will need to play with the values of some of the variables below
  // ******************************************************************************************

  // number of particles in the swarm
  private int numParticles = 100;

  // number of dimensions
  private int numDimensions = 2;

  // personal best acceleration coefficient
  private double phi1 = 2.05;
  // global best acceleration coefficient
  private double phi2 = 2.05;

  // constriction factor
  private double phi = phi1 + phi2;
  public   double constrictionFactor = 0.7298;

  //topology types
  private final int GLOBAL = 1;
  private final int RING = 2;
  private final int VON_NEUMANN = 3;
  private final int RANDOM = 4;

  public int topologyType;

  // test functions
  private final int SPHERE_FUNCTION_NUM = 1;
  private final int ROSENBROCK_FUNCTION_NUM = 2;
  private final int ACKLEY_FUNCTION_NUM = 3;
  private final int RASTRIGIN_FUNCTION_NUM = 4;
  private final int GRIEWANK_FUNCTION_NUM = 5;

  // which one to
  public int testFunction = SPHERE_FUNCTION_NUM;

  // for controlling termination
  private int iterationNum = 0;
  private int maxIterations = 100;


  BasicPSO(String topology, int swarmSize, int numIterations, String function, int numDimensions) {
    Swarm s = new Swarm(topology, function, swarmSize, numDimensions);
    this.maxIterations = numIterations;
  }



  // the "loop forever" method in Processing
  public void draw() {
    s.move();
  }


  // returns the maximum distance between particles in the swarm
  public double maxDistance (double[] x, double[] y) {

    double maxDist = -1.0;
    for (int p1 = 0 ; p1 < numParticles ; p1++) {
      for (int p2 = p1+1 ; p2 < numParticles ; p2++) {
        double thisDistance = distance(x[p1], y[p1], x[p2], y[p2]);
        if (thisDistance > maxDist)
          maxDist = thisDistance;
      }
    }

    return maxDist;
  }


  // returns the distance between (x1, y1) and (x2, y2)
  public double distance (double x1, double y1, double x2, double y2) {

    return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
  }


  //returns the distance between two multidimensional points
  public double distance(double[] x, double[] y) {
    double squareCounter = 0.0;
    for (int i = 0; i < x.length; i++) {
      squareCounter += Math.pow(x[i] - y[i], 2.0);
    }
    return Math.sqrt(squareCounter);
  }



  // returns the value of the specified function for point (x, y)
  public double eval(int functionNum, double[] x) {

    double retValue = 0.0;

    if (functionNum == SPHERE_FUNCTION_NUM) {
      retValue = evalSphere(x);
    }
    else if (functionNum == ROSENBROCK_FUNCTION_NUM) {
      retValue = evalRosenbrock(x);
    }
    else if (functionNum == RASTRIGIN_FUNCTION_NUM) {
      retValue = evalRastrigin(x);
    }
    else if (functionNum == ACKLEY_FUNCTION_NUM) {
      retValue = evalAckley(x);
    }


    return retValue;
  }



  // returns the value of the Sphere function at point (x, y)
  //   minimum is 0.0, which occurs at (0.0,...,0.0)
  public double evalSphere (double[] x) {
    double sum = 0;
    for (int d = 0; d < numDimensions; d++) {
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


}
