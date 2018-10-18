import java.util.Random;
import java.util.Arrays;


public class Neighborhood {
  public Particle[] neighbors;
  public double nBestValue;
  public double[] nBestLoc;

  Neighborhood(Particle[] neighbors){
    this.neighbors = neighbors;

    nBestValue = neighbors[0].getPBestValue();

    updateNBest();
  }

  private void updateNBest(){
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBestValue() < nBestValue){
        nBestValue = neighbors[i].getPBestValue();
        nBestLoc = neighbors[i].getPBestLocation();
      }
    }
  }

  public double getNBestValue(){
    return nBestValue;
  }


  public double[] getNBestLocation(){
    return nBestLoc;
  }

}
