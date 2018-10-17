/*

 Basic Particle Swarm Optimization

 Stephen Majercik
 2013 April 6

*/

// for random numbers
import java.util.Random;


public class BasicPSO {

  // ****************  GRAPHICS  ******************
  // window dimensions
  private final int WINDOW_WIDTH = 1000;


  // ****************  MISCELLANEOUS    ******************

  // for random numbers
  private Random rand = new Random();


  // ****************  PSO  ******************


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
  private int numParticles;

  // number of dimensions
  private int numDimensions;


//  // x and y positions for each particle
  private double[][] loc;

//  // x and y velocities for each particle
  private double[][] vel;

  // pBest positions and values for each particle
  private double[][] pBestLoc = new double[numParticles][numDimensions];
  private double[] pBestValue;

  // gBest position and value
  private double[] gBestLoc = new double[numDimensions];
  private double gBestValue;

  // personal best acceleration coefficient
  private double phi1 = 2.05;
  // global best acceleration coefficient
  private double phi2 = 2.05;

  // constriction factor
  private double phi = phi1 + phi2;
  private double constrictionFactor = 0.7298;

  //topology types
  private final int GLOBAL = 1;
  private final int RING = 2;
  private final int VON_NEUMANN = 3;
  private final int RANDOM = 4;

  public int topologyType;

  // test functions
  private final int SPHERE_FUNCTION_NUM = 0;
  private final int ROSENBROCK_FUNCTION_NUM = 1;
  private final int ACKLEY_FUNCTION_NUM = 2;
  private final int RASTRIGIN_FUNCTION_NUM = 3;

  // which one to
  public int testFunction;

  // for controlling termination
  private int maxIterations;


  BasicPSO(String topology, int swarmSize, int numIterations, String function, int numDimensions) {
    //set topology type

    if (topology.equals("gl")) {
      this.topologyType = GLOBAL;
    }
    if (topology.equals("ri")) {
      this.topologyType = RING;
    }
    if (topology.equals("vn")) {
      this.topologyType = VON_NEUMANN;
    }
    if (topology.equals("ra")) {
      this.topologyType = RANDOM;
    }

    this.numParticles = swarmSize;
    this.numDimensions = numDimensions;

    //set function num
    if (function.equals("sp")) {
      this.testFunction = SPHERE_FUNCTION_NUM;
    }
    if (function.equals("rok")) {
      this.testFunction = ROSENBROCK_FUNCTION_NUM;
    }
    if (function.equals("ack")) {
      this.testFunction = ACKLEY_FUNCTION_NUM;
    }
    if (function.equals("ras")) {
      this.testFunction = RASTRIGIN_FUNCTION_NUM;
    }

    this.maxIterations = numIterations;

  }

  public void execute() {
    this.initialize();
    for (int i = 0; i < maxIterations; i++) {
      this.iterateOnce();
      System.out.println("iteration " + i + "  gbest value = " + gBestValue);
    }
  }


  // initialize the simulation
  public void initialize() {


    // create arrays for particle positions
    loc = new double[numParticles][numDimensions];

    // create arrays for particle velocities
    vel = new double[numParticles][numDimensions];

    // create arrays for particle personal bests
    pBestLoc = new double[numParticles][numDimensions];
    pBestValue = new double[numParticles];

    // set gbest value very high so it will be replaced in the loop
    // that creates the particles
    gBestValue = Double.MAX_VALUE;

    // create particles and calculate initial personal bests;
    // find the initial global best

    for (int p = 0 ; p < numParticles; p++) {
      for(int d = 0; d < numDimensions; d++) {


        // set the coordinates and get the value of the objective function
        // at that point
        loc[p][d] = 15 + (30 - 15) * rand.nextDouble();

        // initialize velocities
        vel[p][d] = MIN_INIT_SPEED + rand.nextDouble() * (MAX_INIT_SPEED - MIN_INIT_SPEED);
      }
      // initial value
      double currValue = eval(testFunction, loc[p]);

      // ****** store initial personal best in the pBest arrays provided
      pBestLoc[p] = loc[p];
      pBestValue[p] = currValue;

      //TODO: shouldn't we be checking that currValue > gBest, not less than
      // ****** check for new global best and store, if necessary,
      // ****** in the variables provided
      if (currValue < gBestValue) {
        gBestLoc = loc[p];
        gBestValue = currValue;

      }
    }
  }


  // the "loop forever" method in Processing
  public void iterateOnce() {

    // update all the particles
    for (int p = 0 ; p < numParticles ; p++) {
      for (int d = 0; d < numDimensions; d++) {
        // ****** compute the acceleration due to personal best
        double PBestAttract = pBestLoc[p][d] - loc[p][d];

        PBestAttract *= rand.nextDouble() * phi1;

        // ****** compute the acceleration due to global best
        double GBestAttract = gBestLoc[d] - loc[p][d];

        GBestAttract *= rand.nextDouble() * phi2;

        // ****** constrict the new velocity and reset the current velocity
        vel[p][d] += PBestAttract + GBestAttract;

        vel[p][d] *= constrictionFactor;

        // ****** update the position
        loc[p][d] += vel[p][d];
      }

      // ****** find the value of the new position
      double currValue = eval(testFunction, loc[p]);

      //TODO: this is saying if curr is less than pBest -- should be more, no?
      // ****** update personal best and global best, if necessary
      if (currValue < pBestValue[p]) {
        pBestValue[p] = currValue;
        pBestLoc[p] = loc[p];

        if (currValue < gBestValue) {
          gBestValue = currValue;
          gBestLoc = loc[p];
        }
      }
    }

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



  // returns the value of the specified function for point [x1, x2, ..., xn]
  public double eval(int functionNum, double[] x) {
    for (int d = 0; d < numDimensions; d++) {
      x[d] -= FUNCTION_SHIFT;
    }

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
      counter += 100 * Math.pow((dimensionVals[i + 1] - dimensionVals[i]), 2) +
              Math.pow((dimensionVals[i] - 1), 2);
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
    return counter;

  }




  // returns the value of the Ackley Function at point (x, y)
  //   minimum is 0.0, which occurs at (0.0,...,0.0)
  private double evalAckley (double[] dimensionVals) {
    double firstCounter = 0;
    double secondCounter = 0;
    for (double i : dimensionVals) {
      firstCounter += Math.pow(i, 2);
      secondCounter += Math.cos( 2 * Math.PI * i );
    }
    double firstExp = -.2 * Math.sqrt( (1 / dimensionVals.length) * firstCounter );
    double secondExp = (1 / dimensionVals.length) * secondCounter;
    return 20 * Math.exp(firstExp) - Math.exp(secondExp) + 20 + Math.E;

  }








}
