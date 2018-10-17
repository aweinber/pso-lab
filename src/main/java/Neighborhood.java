import java.util.Random;
import java.util.Arrays;


public class Neighborhood {
  public Particle[] neighbors;
  private double nBestValue;
  private double[] nBestLoc;

  Neighborhood(Particle[] neighbors){
    this.neighbors = neighbors;
    nBestValue = neighbors[0].getPBest();
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBest() < nBestValue){
        nBestValue = neighbors[i].getPBest();
        nBestLoc = neighbors[i].getPBestLocation();
      }
    }
  }

  public void updateNBest(){
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBest() < nBestValue){
        nBestValue = neighbors[i].getPBest();
        nBestLoc = neighbors[i].getPBestLocation();
      }
    }
  }

  public double getNBestValue(){
    return nBestValue;
  }

  public double[] getNBestLoc(){
    return nBestLoc;
  }

}
