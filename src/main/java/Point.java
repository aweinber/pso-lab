public class Point {

    double[] dimensionVals;

    Point(double[] dimensionVals) {
        this.dimensionVals = dimensionVals;
    }

    double getRosenbrockVal() {
        double counter = 0;
        for (int i = 0; i < dimensionVals.length - 1; i++) {
            counter += 100 * Math.pow((dimensionVals[i + 1] - dimensionVals[i]), 2) +
                            Math.pow((dimensionVals[i] - 1), 2);
        }
        return counter;
    }

    double getAckleyVal() {
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

    double getRastriginVal() {
        double counter = 0;
        for (double i : dimensionVals) {
            counter += Math.pow(i, 2) - (10 * Math.cos(2 * Math.PI * i));
        }
        return counter;
    }
}
