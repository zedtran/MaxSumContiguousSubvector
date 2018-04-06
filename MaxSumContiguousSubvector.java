import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.io.PrintWriter;
import java.lang.Math;
import java.text.DecimalFormat;


/**
* <h1>Maximum Sum Contiguous Subvector (MSCS) Problem</h1>
*
* <p>
* Compute the sum of the subsequence of numbers (a
* subsequence is a consecutive set of one or more numbers) in
* an array of numbers that sum to the largest value possible;
* but if this value is negative, MSCS is defined to be zero.
* <p>
*
* @author  Donald Tran
* @version 1.0
* @since   2018-03-31
*/
public class MaxSumContiguousSubvector {

   //Global Constants //
   private static final int[][] ARRAYS_2D = new int[19][];
   private static final int[] ARRAY_FROM_FILE = new int[10];
   private static final int RESOLUTION_SIZE = 1000;

   //Global variables //
   private static Random rand;
   private static int columnSize = 8;

   //////////
   // MAIN //
   //////////
   public static void main(String[] args) {

       /**
        * Reads in a .txt file that contains comma-space delimited integers and stores the values in an array.
        * Prints out the MSCS value for each algorithm as applied to the input array extracted from file.
        */
      try {
         Scanner scanner = new Scanner (new File("phw_input.txt")).useDelimiter(",\\s*|(\\n)");
         for (int i = 0; i < ARRAY_FROM_FILE.length; i++) {
            ARRAY_FROM_FILE[i] = scanner.nextInt();
         }
         scanner.close();
         System.out.println("algorithm-1: " + algorithm1(ARRAY_FROM_FILE));
         System.out.println("algorithm-2: " + algorithm2(ARRAY_FROM_FILE));
         System.out.println("algorithm-3: " + maxSum(ARRAY_FROM_FILE, 0, 9));
         System.out.println("algorithm-4: " + algorithm4(ARRAY_FROM_FILE));
      }
      catch (IOException e) {
         System.out.println("Error reading input file 'phw_input.txt'");
      }

        // Generates arrays 1 - 19,
        // of length 10, 15, 20, ..., 95, 100, ...
        // containing random integers ranging from -100 --> 100
      populateAllArrays(ARRAYS_2D);


      try {
              // Output to .txt file (Required) && output to .xls file for easier creation of Excel plots
         PrintWriter pw = new PrintWriter("dzt0021_phw_output.txt", "UTF-8");
         PrintWriter pwxls = new PrintWriter("dzt0021_phw_output.xls", "UTF-8");
         pw.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n", "algorithm-1,", "algorithm-2,", "algorithm-3,", "algorithm-4,", "T1(n),", "T2(n),", "T3(n),", "T4(n)");
         pwxls.print("Algorithm-1\tAlgorithm-2\tAlgorithm-3\tAlgorithm-4\tT1(n)\tT2(n)\tT3(n)\tT4(n)\tX-axis\n");

         DecimalFormat df = new DecimalFormat("0.00");
         int size = 10; /* The starting size of array[0] in ARRAYS_2D (Initially 10) */

              // For each array in ARRAYS_2D
         for (int[] array: ARRAYS_2D) {
                  // Print to files the actual run time execution from calling each function RESOLUTION_SIZE times
            for (int algo = 1; algo < 5; algo++) {
               pw.printf("%-15s", df.format(getElapsedTime(array, RESOLUTION_SIZE, algo)) + ",");
               pwxls.print(df.format(getElapsedTime(array, RESOLUTION_SIZE, algo)) + "\t");

            }
                  // Then print off the estimated run time based on each increasing number of inputs T(n)
                  // where n = 10, 15, 20, ... , 95, 100, ...
            for (int algo = 1; algo < 5; algo++) {
               if (algo == 4) {
                  pw.printf("%-15s%n", df.format(getTnValue(size, algo)));
                  pwxls.print(df.format(getTnValue(size, algo)) + "\t" + size + "\n");
               }
               else {
                  pw.printf("%-15s", df.format(getTnValue(size, algo)) + ",");
                  pwxls.print(df.format(getTnValue(size, algo)) + "\t");
               }
            }
            size += 5;
         }
           // Close PrintWriters for output files dzt0021_phw_output(.xls && .txt)
         pw.close();
         pwxls.close();

      }
      catch (IOException e) {
         System.out.println("Error writing to output file.");
      }

   }

   //////////////////
   // MSCS Methods //
   //////////////////

   // Algorithm-1
   public static int algorithm1(int[] x)
   {
      int maxSoFar = 0;
      for (int l = 0; l < x.length; l++) {
         for (int u = l; u < x.length; u++) {
            int sum = 0;
            for (int i = l; i <= u; i++) {
               sum = sum + x[i];
               /* sum now contains the sum of x[l..u] */
            }
            maxSoFar = Math.max(maxSoFar, sum);
         }
      }
      return maxSoFar;
   }

   // Algorithm-2
   public static int algorithm2(int[] x)
   {
      int maxSoFar = 0;
      for (int l = 0; l < x.length; l++) {
         int sum = 0;
         for (int u = l; u < x.length; u++) {
            sum = sum + x[u];
            /* sum now contains the sum of x[l..u] */
            maxSoFar = Math.max(maxSoFar, sum);
         }
      }
      return maxSoFar;
   }

   // Algorithm-3
   public static int maxSum(int[] x, int l, int u)
   {
      if (l > u) {
         return 0; /* zero-element vector */
      }
      if (l == u) {
         return Math.max(0, x[l]); /* one-element vector */
      }
      int m = (l + u) / 2; /* A is x[l..m], B is x[m+1, u] */
      /* Find max crossing to left */
      int sum = 0, maxToLeft = 0;
      for (int i = m; i >= l; i--) {
         sum = sum + x[i];
         maxToLeft = Math.max(maxToLeft, sum);
      }
      /* Find max crossing to right */
      sum = 0;
      int maxToRight = 0;
      for (int i = m + 1; i <= u; i++) {
         sum = sum + x[i];
         maxToRight = Math.max(maxToRight, sum);
      }
      int maxCrossing = maxToLeft + maxToRight;
      int maxInA = maxSum(x, l, m);
      int maxInB = maxSum(x, m + 1, u);

      return Math.max(Math.max(maxCrossing, maxInA), maxInB);
   }

   // Algorithm-4
   public static int algorithm4(int[] x)
   {
      int maxSoFar = 0;
      int maxEndingHere = 0;
      for (int i = 0; i < x.length; i++) {
         maxEndingHere = Math.max(0, maxEndingHere + x[i]);
         maxSoFar = Math.max(maxSoFar, maxEndingHere);
      }
      return maxSoFar;
   }

   /**
   * This method populates an array with random int
   * values ranging from -100 --> 100.
   * @param size This is the desired size for the created array
   * @return arr[] This returns the created array with values inserted
   */
   private static int[] populateArray(int size)
   {
      rand = new Random();
      int[] tempArray = new int[size];
      int val;
      for (int i = 0; i < size; i++) {
         val = rand.nextInt(201) - 100;
         tempArray[i] = val;
      }
      return tempArray;
   }

   /**
   * This method populates an array of arrays with each array containing
   * random values ranging from -100 --> 100. Each array in the 2d array, from first
   * to last, is of length 10, 15, ... , 95, 100, ...
   * @param arrsToPopulate This is the 2d array containing all arrays we'll use to evaluate Algorithms 1 to 4
   * @see ARRAYS_2D The initialized 2d array for this class
   * @return nothing
   */
   private static void populateAllArrays(int[][] arrsToPopulate) {
      int len = 10;
     // Assignment for each array in 2d array arrsToPopulate //
      for (int i = 0; i < arrsToPopulate.length; i++) {
         arrsToPopulate[i] = populateArray(len);
         len += 5;
      }
   }

   /**
   * This method uses the system clock to measure execution times t1 --> t4 on
   * a specified array to calculate an average time for either of algorithm1 --> algorithm4
   * after "n" calls.
   * @param arr This is the array which we want to use for T(n) calculation
   * @param n   Is the resolution problem size we want to evaluate on arr for the selected algorithm
   * @param algo   Is the algorithm for the time we want to measure ID'd from 1..4
   * @return double The elapsed time in microseconds, else -1 if invalid
   */
   private static double getElapsedTime(int[] arr, int n, int algo)
   {
      double start;
      double finish;

      switch (algo) {

         case 1: start = System.nanoTime();
            for (int i = 1; i < n + 1; i++) {
               algorithm1(arr);
            }
            finish = System.nanoTime();
            return (finish - start) / 1000;

         case 2: start = System.nanoTime();
            for (int i = 1; i < n + 1; i++) {
               algorithm2(arr);
            }
            finish = System.nanoTime();
            return (finish - start) / 1000;

         case 3: start = System.nanoTime();
            for (int i = 1; i < n + 1; i++) {
               maxSum(arr, 0, arr.length - 1);
            }
            finish = System.nanoTime();
            return (finish - start) / 1000;

         case 4: start = System.nanoTime();
            for (int i = 1; i < n + 1; i++) {
               algorithm2(arr);
            }
            finish = System.nanoTime();
            return (finish - start) / 1000;

         default:
            System.out.println("Invalid case provided for 'getElapsedTime()'!");

      }
      return -1;
   }

   /**
   * This method computes the ceiling(T_i_(n)) for the theoretically
   * calculated complexity of Algorithm-1 thru Algorithm-4
   * @param n   Represents each input size
   * @param algo   Is the algorithm for the theoretical time we want to measure ID'd from 1..4
   * @return double The computed ceiling of our theoretical T(n) else -1 if invalid
   */
   private static double getTnValue(int n, int algo) {

      switch(algo) {

         case 1:
            return Math.ceil((7/6) * Math.pow(n, 3) + (7 * Math.pow(n, 2)) + ((47/6) * n) + 4);
         case 2:
            //return Math.ceil(((7 * Math.pow(n, 2)) + (10 * n) + 4));
            return Math.ceil((6 * Math.pow(n, 2) + (8 * n) + 4));
         case 3:
            return Math.ceil(n * (Math.log(n) / Math.log(2)) + (n * 81));
         case 4:
            return Math.ceil((15 * n) + 4);
         default: System.out.println("Invalid case provided for 'getTnValue()'!");
      }
      return -1;
   }


}
