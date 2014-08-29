package br.com.recomusic.persistencia.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Guilherme7
 */
public class TesteKMeans {
public static final int CLUSTERS = 3;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[] data = {100.8,154,199.2,74.89,124.32,32.9,32,54.9,89.9,75.045,178.9,167.43};

        int length = data.length;

        double[][] sums = new double[CLUSTERS][length];

        double[][] centroids = {{0, 0, 0}, {100.8, 154,199.2}};

        int[] count = new int[CLUSTERS];

        int i, j, k;

        double minimum, difference;

        boolean converged = false;

        do {

            for(i = 0; i < CLUSTERS; i++) {
                
                centroids[0][i] = centroids[1][i];

                count[i] = 0;

                centroids[1][i] = 0;

            }

            for(i = 0; i < length; i++) {

                 

                sums[0][i] = 0;

                minimum = centroids[0][0] > data[i] ? centroids[0][0] - data[i] : data[i] - centroids[0][0];

                k = 0;
                for(j = 1; j < CLUSTERS; j++) {

                    sums[j][i] = 0;

                    difference = centroids[0][j] > data[i] ? centroids[0][j] - data[i] : data[i] - centroids[0][j];

                     

                    if(difference < minimum) {

                         

                        minimum = difference;

                        k = j;

                    }

                }

                 

                sums[k][i] = data[i];

                count[k]++;

            }

             

            converged = true;

             

            for(i = 0; i < CLUSTERS; i++) {

                 

                difference = 0;

                 

                if(count[i] > 0) {

                    for(j = 0; j < length; j++) {

                         

                        centroids[1][i] += sums[i][j] / count[i];

                       difference += sums[i][j] % count[i];

                        centroids[1][i] += difference / count[i];

                        difference %= count[i];

                    }

                }

                 

                converged &= centroids[0][i] == centroids[1][i];

            }

        }while(!converged);

 

        for(i = 0; i < CLUSTERS; i++) {

             

            System.out.println(centroids[1][i]);
        }
    }
}
