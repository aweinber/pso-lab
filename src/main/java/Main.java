public class Main {
    public static void main(String[] args ) {

        String errorMessage = "Topology must be a string gl/ri/vn/ra\n " +
                "Swarm size must an integer in range [0,]\n " +
                "Num iterations must be an integer in range [0, ]\n " +
                "Function to optimize on must be a string rok/ack/ras\n " +
                "Dimensionality must be an integer in range [1, ]";

        if ( ! areValidArgs(args) ) {
            System.out.println(errorMessage);
            return;
        }

        String topology, functionName;
        int swarmSize, numIterations, numDimensions;

        topology = args[0];
        swarmSize = Integer.parseInt(args[1]);
        numIterations = Integer.parseInt(args[2]);
        functionName = args[3];
        numDimensions = Integer.parseInt(args[4]);

        Particle[] particles = new Particle[swarmSize];

        for(int i = 0; i < swarmSize; i++){
            particles[i] = new Particle(functionName, numDimensions);
        }

        Swarm s = new Swarm(particles, topology);
        s.initializeNeighborhoods();

        for (int i = 0; i < numIterations; i++) {
            s.move();
            System.out.println("Iteration #: " + (i+1) + ", global best: " + s.getgBestValue());
        }

    }

    /**
     * Ensure that inputs are valid.
     * @param args user parameters
     * @return whether or not we have a valid set of inputs.
     */
    private static boolean areValidArgs(String[] args) {
        if (args.length != 5) {
            System.out.println("Arg.lengths != 5, == " + args.length);
            return false;
        }

        String topology, functionName;
        int swarmSize, numIterations, numDimensions;

        try {
            topology = args[0];
            swarmSize = Integer.parseInt(args[1]);
            numIterations = Integer.parseInt(args[2]);
            functionName = args[3];
            numDimensions = Integer.parseInt(args[4]);
        }
        catch (NumberFormatException e) {
            System.out.println("Error thrown");
            return false;
        }
        if (swarmSize < 0 || numIterations < 0 || numDimensions < 1) {
            return false;
        }
        if (! (topology.equals("gl") || topology.equals("ri") || topology.equals("vn") || topology.equals("ra"))) {
            return false;
        }
        if (! (functionName.equals("sp") || functionName.equals("rok") || functionName.equals("ack") || functionName.equals("ras"))) {
            return false;
        }
        return true;
    }

    /**
     * Test function for Rastrigin.
     */
    public static void testRastrigin() {
        // returns the value of the Rastrigin Function at point (x, y)
        //   minimum is 0.0, which occurs at (0.0,...,0.0)
        double newVal = 1.0;
        System.out.println("newval: " + newVal + " expected answer: 3");
        double[] dimensionVals = new double[3];
        for (int i = 0; i < dimensionVals.length; i++) {
            dimensionVals[i] = newVal;
        }

        double counter = 0;
        for (double i : dimensionVals) {
            counter += Math.pow(i, 2) - (10 * Math.cos(2 * Math.PI * i));
        }
        double ans = 10 * dimensionVals.length + counter;
        System.out.println("answer: " + ans);


    }

    /**
     * Test function for Rosenbrock.
     */
    public static void testRosenbrock() {
    // returns the value of the Rosenbrock Function at point (x, y)
    //   minimum is 0.0, which occurs at (1.0,...,1.0)
        double newVal = .5;
        System.out.println("newval: " + newVal + " expected answer: 13");
        double[] dimensionVals = new double[3];
        for (int i = 0; i < dimensionVals.length; i++) {
            dimensionVals[i] = newVal;
        }

        double counter = 0;
        for (int i = 0; i < dimensionVals.length - 1; i++) {
            double leftSide = 100 * Math.pow(dimensionVals[i + 1] - Math.pow(dimensionVals[i], 2), 2);
            double rightSide = Math.pow(dimensionVals[i] - 1, 2);
            counter = counter + leftSide + rightSide;
            System.out.println("New counter: " + counter);
        }
        System.out.println("Rosenbrock counter = " + counter);
    }

    /**
     * Test the Ackley function.
     */
    private static void testAckley() {
        double newVal = (Math.PI / 2.0);
        System.out.println("Newval: " + newVal + " expected ans: " + 7.04);

        double[] dimensionVals = new double[3];
        for (int i = 0; i < dimensionVals.length; i++) {
            dimensionVals[i] = newVal;
        }


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

        double ans = (-a * Math.exp(firstExp)) - Math.exp(secondExp) + a + Math.E;

        System.out.println("Answer: " + ans);

    }


}
