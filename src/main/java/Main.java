public class Main {
    public static void main(String[] args ) {
        if (args.length != 5) {
            System.out.println("Arg.lengths != 5, == " + args.length);
            return;
        }
        String messageError = "Topology must be a string gl/ri/vn/ra\n " +
                "Swarm size must an integer in range [0,]\n " +
                "Num iterations must be an integer in range [0, ]\n " +
                "Function to optimize on must be a string rok/ack/ras\n " +
                "Dimensionality must be an integer in range [1, ]";

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
            System.out.println(messageError);
            return;
        }
//        if (swarmSize < 0 || numIterations < 0 || numDimensions < 1) {
//            System.out.println(messageError);
//            return;
//        }
//        if (! (topology.equals("gl") || topology.equals("ri") || topology.equals("vn") || topology.equals("ra"))) {
//            System.out.println(messageError);
//            return;
//        }
//        if (! (functionName.equals("sp") || functionName.equals("rok") || functionName.equals("ack") || functionName.equals("ras"))) {
//            System.out.println(messageError);
//            return;
//        }
        BasicPSO pso = new BasicPSO(topology, swarmSize, numIterations, functionName, numDimensions);
        pso.setup();
        for (int i = 0; i < numIterations; i++) {
            pso.draw();
        }

    }
}
