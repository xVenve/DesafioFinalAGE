public class Rules {
    public int getThrust(double distance, double angle){
        System.err.println("OK");

        double[] distanceRanges = {50, 200, 500}; //0 - 16000
        double[] angleRanges = {20, 45, 90, 270, 315, 340}; //0 - 360
        int[][] thrustInRange = { //length = distanceRanges.length+1 X angleRanges.length+1
                {10, 5, 5, 0, 5, 5, 10},
                {50, 40, 40, 0, 40, 40, 50},
                {100, 100, 50, 0, 50, 100, 100},
                {200, 200, 100, 0, 100, 200, 200},
            }; //0 - 200

        for (int i=0; i<distanceRanges.length+1; i++){
            double dr = 160000;
            if(i<distanceRanges.length)
                dr = distanceRanges[i];

            if(angle<dr){
                for (int j=0; j<angleRanges.length+1; j++){
                    double ag = 360;
                    if(j<angleRanges.length)
                        ag = angleRanges[j];
                    if(distance<ag)
                        return thrustInRange[i][j];
                }
            }
        }
        return 0;
    }
}
