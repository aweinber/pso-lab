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


  // initialize the simulation
  public void setup() {



    // create arrays for particle positions
    loc = new double[numParticles][numDimensions];

    // create arrays for particle velocities
    vel = new double[numParticles][numDimensions];

    // create arrays for particle personal bests
    pBestValue = new double[numParticles];
    pBestLoc = new double[numParticles][numDimensions];

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

      // ****** check for new global best and store, if necessary,
      // ****** in the variables provided
      if (currValue < gBestValue) {
        gBestLoc = loc[p];
        gBestValue = currValue;

      }
    }
  }


  // the "loop forever" method in Processing
  public void draw() {

    ++iterationNum;

    // a kludgy way to stop, but keep the window visible;
    // it just stops for a "long" time, defined by the constant LONG_DELAY
    if (iterationNum > maxIterations) {
      System.out.println("DONE!!");
    }

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

     System.out.println("iteration " + iterationNum + "  gbest value = " + gBestValue);
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



  // returns the value of the specified function for point (x, y)
  public double eval(int functionNum, double[] x) {
    for (int d = 0; d < numDimensions; d++) {
      x[d] -= FUNCTION_SHIFT;
    }

    double retValue = 0.0;

    if (functionNum == SPHERE_FUNCTION_NUM) {
      retValue = evalSphere(x);
    }
    else if (functionNum == ROSENBROCK_FUNCTION_NUM) {
      retValue = evalRosenbrock(x[0], 0);
    }
    else if (functionNum == RASTRIGIN_FUNCTION_NUM) {
      retValue = evalRastrigin(x[0], 0);
    }
    else if (functionNum == ACKLEY_FUNCTION_NUM) {
      retValue = evalAckley(x[0], 0);
    }
    else if (functionNum == GRIEWANK_FUNCTION_NUM) {
      retValue = evalGriewank(x[0], 0);
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
  public double evalRosenbrock (double x, double y) {

    return 100.0 * Math.pow(y - x*x, 2.0) + Math.pow(x-1.0, 2.0);
  }




  // returns the value of the Rastrigin Function at point (x, y)
  //   minimum is 0.0, which occurs at (0.0,...,0.0)
  public double evalRastrigin (double x, double y) {

    double retVal = 0;
    retVal += x*x - 10.0*Math.cos(2.0*Math.PI*x) + 10.0;
    retVal += y*y - 10.0*Math.cos(2.0*Math.PI*y) + 10.0;

    return retVal;
  }




  // returns the value of the Ackley Function at point (x, y)
  //   minimum is 0.0, which occurs at (0.0,...,0.0)
  public double evalAckley (double x, double y) {

    double firstSum = x*x + y*y;
    double secondSum = Math.cos(2.0*Math.PI*x) + Math.cos(2.0*Math.PI*y);

    return -20.0 * Math.exp(-0.2 * Math.sqrt(firstSum/2.0)) -
      Math.exp(secondSum/2.0) + 20.0 + Math.E;
  }




  // returns the value of the Griewank function at point (x, y)
  //   minimum is 0.0, which occurs at (0.0,...,0.0)
  public double evalGriewank (double x, double y) {

    double sumSquares = x*x + y*y;
    double productCos = Math.cos(x/Math.sqrt(1)) * Math.cos(y/Math.sqrt(2));

    return sumSquares/4000.0 - productCos + 1.0;
  }



}