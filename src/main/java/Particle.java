public class Particle {

    double[] location;
    double[] vector;

    double pBestValue;
    double[] pBestLocation;

    String function;
    int numDimensions;

    public Particle(String function, int numDimensions){
      this.function = function;
      this.numDimensions = numDimensions;
      this.location = initializeLocation(String function, int numDimensions)
      this.vector = initializeVector(String function, int numDimensions)
    }

    public Particle(double[] location, double[] vector, double pBest, double[] pBestLocation) {
        this.location = location;
        this.vector = vector;
        this.pBest = pBest;
        this.pBestLocation = pBestLocation;
    }

    public double getPBestValue() {
        return pBestValue;
    }

    public double[] getPBestLocation() {
        return pBestLocation;
    }

    private double[] initializeLocation(String function, int numDimensions){
        location = new double[numDimensions];
        if(function == "ack")
            for(int i = 0; i < numDimensions; i++){
              location[d] = 16 + rand.nextDouble() * (32 - 16);
            }
        else if(function == "ros")
            for(int i = 0; i < numDimensions; i++){
              location[d] = 15 + rand.nextDouble() * (30 - 15);
            }
        else if(function == "ras")
            for(int i = 0; i < numDimensions; i++){
              location[d] = 2.56 + rand.nextDouble() * (5.12 - 2.56);
            }
    }

    private double[] initializeVector(String function, int numDimensions){
        vector = new double[numDimensions];
        if(function == "ack")
            for(int i = 0; i < numDimensions; i++){
              vector[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
        else if(function == "ros")
            for(int i = 0; i < numDimensions; i++){
              location[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
        else if(function == "ras")
            for(int i = 0; i < numDimensions; i++){
              location[d] = -2 + rand.nextDouble() * Math.abs(-2 - 4);
            }
    }

    // returns the value of the specified function for point (x, y)
    public void eval() {

      if (function == "she") {
        retValue = evalSphere(x);
      }
      else if (function == "ros") {
        retValue = evalRosenbrock(x);
      }
      else if (function == "ras") {
        retValue = evalRastrigin(x);
      }
      else if (function == "ack") {
        retValue = evalAckley(x);
      }
      if(retValue < pBestValue){
        pBestValue = retValue;
        pBestLoc = location.clone();
      }
    }

    public void move(double nBestValue, double nBestLoc){
      for (int d = 0; d < numDimensions; d++) {
        // ****** compute the acceleration due to personal best
        double PBestAttract = pBestLoc[d] - location[d];

        PBestAttract *= rand.nextDouble() * phi1;

        // ****** compute the acceleration due to global best
        double GBestAttract = nBestLoc[d] - location[d];

        GBestAttract *= rand.nextDouble() * phi2;

        // ****** constrict the new velocity and reset the current velocity
        velocity[d] += PBestAttract + GBestAttract;

        velocity[d] *= constrictionFactor;

        // ****** update the position
        location[d] += velocity[d];
      }
      eval()
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
