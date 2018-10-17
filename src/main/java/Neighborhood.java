import java.util.Random;
import java.util.Arrays;


public class Neighborhood {
  private Paricle[] neighbors;
  private double nBestValue;
  private double[] nBestLoc;

  Neighborhood(Paricle[] neighbors){
    this.neighbors = neighbors;
    nBestValue = neighbors[0].getPBest();
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBestValue() < nBestValue){
        nBestValue = neighbors[i].getPBestValue();
        nBestLoc = neighbors[i].getPBestLoc();
      }
    }
  }

  public void updateNBest(){
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBestValue() < nBestValue){
        nBestValue = neighbors[i].getPBestValue();
        nBestLoc = neighbors[i].getPBestLoc();
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
