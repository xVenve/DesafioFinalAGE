public class Rules {
    public int getThrust(double distance, double angle){
        double[] distanceRanges = {1000, 2000, 4000}; //0 - 16000
        double[] angleRanges = {10, 45, 90, 270, 315, 350}; //0 - 360
        int[][] thrustInRange = { //length = distanceRanges.length+1 X angleRanges.length+1
                {10, 5, 5, 0, 5, 5, 10},
                {50, 40, 40, 0, 40, 40, 50},
                {100, 70, 50, 0, 50, 70, 100},
                {200, 150, 100, 0, 100, 150, 200},
            }; //0 - 200

        for (int i=0; i<distanceRanges.length+1; i++) {
            double dr = 160000;
            if (i < distanceRanges.length)
                dr = distanceRanges[i];

            if (distance < dr) {
                for (int j=0; j<angleRanges.length+1; j++){
                    double ag = 360;
                    if(j<angleRanges.length)
                        ag = angleRanges[j];
                    if(angle<ag)
                        return thrustInRange[i][j];
                }
            }
        }

        return 0;
    }
}
