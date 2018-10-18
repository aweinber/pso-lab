import java.util.Random;
import java.util.Arrays;


public class Neighborhood {
  public Particle[] neighbors;
  public double nBestValue;
  public double[] nBestLoc;

  Neighborhood(Particle[] neighbors){
    this.neighbors = neighbors;

    nBestValue = neighbors[0].getPBestValue();
    nBestLoc = neighbors[0].getPBestLocation();

    updateNBest();
  }

  public void updateNBest() {
//    double best = this.nBestValue;
    System.out.println("Update nBest: " + neighbors);
    for(int i = 0; i < neighbors.length; i++){
      if(neighbors[i].getPBestValue() < this.nBestValue){
        nBestValue = neighbors[i].getPBestValue();
//        nBestLoc = neighbors[i].getPBestLocation();
      }
    }
    System.out.println("new nBest: " + this.nBestValue);

  }

  public double getNBestValue(){
    return nBestValue;
  }


  public double[] getNBestLocation(){
    return nBestLoc;
  }

}
