import java.util.Random;
import java.util.Arrays;


public class Neighborhood {

  /**
   * Array of particles in the neighborhood.
   */
  private Particle[] neighbors;

  /**
   * Neighborhood best value.
   */
  private double nBestValue;

  /**
   * Neighborhood best location.
   */
  private double[] nBestLoc;

  /**
   * Initializes a neighborhood. Sets minimum nBestLoc and nBestVal fields
   * then calls update() to improve if possible.
   * @param neighbors Particle array representing the neighbors.
   */
  Neighborhood(Particle[] neighbors){
    this.neighbors = neighbors;
    nBestValue = neighbors[0].getPBestValue();
    nBestLoc = neighbors[0].getPBestLocation();
    updateNBest();
  }

  /**
   * Iterate through the neighborhood and check the particle's values.
   */
  void updateNBest() {

    for (Particle particle : neighbors) {
      if(particle.getPBestValue() < this.nBestValue){
        nBestValue = particle.getPBestValue();
        nBestLoc = particle.getPBestLocation();
      }
    }
  }


  /**
   * Getter
   * @return neighborhood Best value
   */
  public double getNBestValue(){
    return nBestValue;
  }

  /**
   * Getter
   * @return neighborhood best location
   */
  public double[] getNBestLocation(){
    return nBestLoc;
  }

}
