import java.util.Random;
import java.util.Arrays;


public class Neighborhood {
  private int[] neighborIndex;
//  private Particle[] particles;
  private double nBest;
  private double[] nBestLoc;

  private final int GLOBAL = 1;
  private final int RING = 2;
  private final int VON_NEUMANN = 3;
  private final int RANDOM = 4;

  private final int TOPOLOGY;

  Neighborhood(int topology, double[][] swarmLoc, double[] swarmPBest, double[][] swarmPBestLoc, int p){
    TOPOLOGY = topology;
    nBest = swarmPBest[0];
    neighborIndex = new int[swarmPBest.length];
    for(int i = 0; i < swarmPBest.length; i++){
      neighborIndex[i] = i;
      if(swarmPBest[i] < nBest){
        nBest = swarmPBest[i];
        nBestLoc = swarmPBestLoc[i].clone();

      }
    }
  }

  public void updateNBest(double[][] swarmLoc, double[] swarmPBest, double[][] swarmPBestLoc){
    for(int i = 0; i < neighborIndex.length; i++){
      if(swarmPBest[neighborIndex[i]] < nBest){
        nBest = swarmPBest[i];
        nBestLoc = swarmPBestLoc[i].clone();
      }
    }
  }

  public double getNBest(){
    return nBest;
  }

  public double[] getNBestLoc(){
    return nBestLoc;
  }

}
