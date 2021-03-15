import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
//*******************************************************************
// This program aims to cluter datapoint by KMeans Clustering method from data.csv file.
// Student Name: Akzhan ABYLKASSYM
// SID: 19201001
//*******************************************************************


public class assignment01 {
    public static void main(String [] args) throws Exception{
        new assignment01().runApp(args);
    }
    void runApp(String [] args) throws Exception {
        String clusters= args[0];//K number input
        String filename = args[1];//input filename
        String outfile= args[2];// output filename
        File inputFile = new File(filename);

        if (!inputFile.exists()) {
            System.out.println("The file " + filename + " is not found.");
            System.exit(0);
        }
        Scanner in = new Scanner(inputFile);
        String row = in.nextLine();
        String[] rowsToken = getTokens(row);
        int rowNumber = st2int(rowsToken[0]);//converting first number to an integer
        int clusterNumber = st2int(clusters);//converting K number to an integer
        int [][] centroids = getCentroids(clusterNumber);//generating random new centroids of the clusters
        int [][] matrix =  getMatrixFromFile(filename);//reading data from the file and convert the data to 2darray
        int[] clusterAssignment = new int[matrix.length];//creating a new array for cluster assignment
        int[][] ClusterArray = getClusterArray(matrix, centroids, clusterAssignment);//new Cluster with Assigned CLuster numbers
        int[][] means = getNewCentroids(ClusterArray, centroids.length);// generating  new centroids of the Clusters which are the average of CLuster datapoints
        int [][] previosClusters;//new cluster to compare the old cluster with the new cluster
        do {//repeating assigning the cluster number of recalculation of new centroids
            previosClusters = ClusterArray;
            ClusterArray = getClusterArray(ClusterArray, means, clusterAssignment);
            means= getNewCentroids(ClusterArray, centroids.length);
        }
        while(!sameClusters(previosClusters, ClusterArray));//old cluster array is not equal to the new cluster array
        System.out.println(rowNumber);
        printMatrix(means);
        printMatrix(ClusterArray);

        System.out.println();

        FileWriter fwriter = new FileWriter(outfile, true);//printing the output file
        PrintWriter out = new PrintWriter(fwriter);
        out.println(rowNumber);
        for(int i=0;i< means.length;i++) {
            out.println(means[i][0]+","+means[i][1]);
        }
        for(int j=0;j< ClusterArray.length;j++) {
            out.println(ClusterArray[j][0]+","+ClusterArray[j][1]+","+ ClusterArray[j][2]);
        }
        out.close();
    }
       boolean sameClusters(int [][] clusterA, int [][] clusterB) {//checking if  no data point gets reclassified
            if (clusterA.length != clusterB.length){ return false;}
            for (int i = 0; i < clusterA.length; i++) {
                if ( !(clusterA[i][0] == clusterB[i][0] && clusterA[i][1] == clusterB[i][1])) {
                    return false;
                }
            }
            return true;
        }



    double getDistance(int []centroid, int [] point){//calculating the distance of points from centroids
        double distance;
        distance=Math.sqrt(Math.pow(centroid[0]-point[0],2)+Math.pow(centroid[1]-point[1],2));
        return distance;
        }
        int[][] getNewCentroids(int[][] ClusterArray, int centroidsLength) {//recalculating centroids coordinates
        int [][] sums = new int[centroidsLength][];
        int[] count = new int[centroidsLength];

        for (int i = 0; i < centroidsLength; i++) {
            sums[i] = new int[] {0, 0};
            count[i] = 0;
        }

        int k;

        for (int i = 0; i < ClusterArray.length; i++) {
            k = ClusterArray[i][2];
            sums[k][0] += ClusterArray[i][0];
            sums[k][1] += ClusterArray[i][1];

            //System.out.printf("sums[%d]: %d, %d\n", k, sums[k][0], sums[k][1]);

            count[k]++;
        }

        for (int i = 0; i < sums.length; i++) {
            sums[i][0] = sums[i][0] / count[i];
            sums[i][1] = sums[i][1] / count[i];
        }

        return sums;
        }

        int [][] getClusterArray(int [][] matrix, int [][] centroids, int [] clusterAssignment){//assigning cluster number to datapoints
        int [] assignCluster = getCluster(matrix,centroids,clusterAssignment);
        int [][] ClusterArray = new int[matrix.length][3];
            for(int i=0;i< matrix.length;i++){
                for(int j=0;j<2;j++){
                    ClusterArray[i][j]=matrix[i][j];
                }
                ClusterArray[i][2] = assignCluster[i];
            }
            return ClusterArray;
        }
        int [] getCluster(int [][] matrix, int [][] centroids,int [] clusterAssignment){
        for (int i = 0; i < matrix.length; i++) {
            int count=0;
            double currentDistance = 760;
            while(count<centroids.length) {
                double distance = getDistance(centroids[count],matrix[i]);
                if(distance<currentDistance){
                    currentDistance=distance;
                    clusterAssignment[i]=count;
                }
                count++;
            }
            currentDistance=760;
        }
        return clusterAssignment;
        }
        int [][] getCentroids(int clusterNumber){//random coordinates of  centroids
            int [][] centroidPositions= new int [clusterNumber][2];
            int a=0;
            int b=760;
            for(int i=0;i<clusterNumber;i++) {
                for (int j = 0; j < 2; j++) {
                    int randNum= a + (int) (Math.random() * b);
                    centroidPositions[i][j] =randNum;
                }
            }
            return centroidPositions;
        }

        void printMatrix(int [][] m){
            for(int j=0;j<m.length;j++){
                for(int k=0;k<m[j].length;k++){
                    System.out.print(m[j][k]+" ");
                }
                System.out.println();
            }
        }
        int[][] getMatrixFromFile(String filename) throws Exception {//reading the file
            File inputFile = new File(filename);
            if (!inputFile.exists()) {
                System.out.println("The file " + filename + " is not found.");
                System.exit(0);
            }
            Scanner in = new Scanner(inputFile);
            int count = 0;
            String row = in.nextLine();
            String[] rowsToken = getTokens(row);
            int rowNumber = st2int(rowsToken[0]);
            int colNumber=2;
            int[][] matrix = new int[rowNumber][colNumber];
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] tokens = getTokens(line);
                    for (int i = 0; i < colNumber; i++) {
                        matrix[count][i] = st2int(tokens[i]);
                    }

                count++;
            }
            in.close();
            return matrix;
        }
        int skipSpaces(String str, int start) {
            int pos = start;

            while(pos < str.length()) {
                char c = str.charAt(pos);
                if (c != ' ') {
                    break;
                } else {
                    pos++;
                }
            }
            return pos;
        }

        String nextToken(String str, int start) {
            String token = "";
            int pos = start;
            while (pos < str.length()) {
                char c = str.charAt(pos);
                if (c != ' ') {
                    token = token + c;
                    pos++;
                } else {
                    break;
                }
            }
            return token;
        }
        String [] addToTokenArray(String [] tokenArray, String newToken) {
            if (newToken.length() == 0) {
                return tokenArray;
            }
            String[] newTokenArray = new String[tokenArray.length + 1];
            for (int i = 0; i< tokenArray.length; i++) {
                newTokenArray[i] = tokenArray[i];
            }

            newTokenArray[newTokenArray.length-1] = newToken;

            return newTokenArray;
        }
        String[] getTokens(String str) {
            int pos = 0;
            String[] tokenArray = new String[0];

            while(pos < str.length()) {
                pos = skipSpaces(str, pos);
                String newToken = nextToken(str, pos);
                pos += newToken.length();

                tokenArray = addToTokenArray(tokenArray, newToken);
            }
            return tokenArray;
        }

        int [][] addMatrices (int [][] m1, int [][ ] m2){
            int [][] sum = new int [m1.length][m1[1].length];
            for(int i=0;i<m1.length;i++){
                for(int j=0;j<m1[i].length;j++){
                    sum[i][j] = m1[i][j]+m2[i][j];
                }
            }
            return sum;
        }
        int st2int(String str){
            int result = 0;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if(c==','){
                    continue;
                }
                int value = c - '0';
                result = result * 10 + value;
            }
            return result;
        }
    }
