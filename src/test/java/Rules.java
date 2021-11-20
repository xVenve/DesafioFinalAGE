public class Rules {
    public int getThrust(double distance){
        int thrust = (int) Math.min(200, distance);
        return thrust;
    }
}
