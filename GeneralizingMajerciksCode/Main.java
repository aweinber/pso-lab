public class Main {
    public static void main(String[] args ) {
      BasicPSO x = new BasicPSO();
      x.setup();
      for(int i = 0; i < 100; i++){
        x.draw();
      }
    }
}
