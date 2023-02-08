package main.java.hescha.inverse_matrix.service;

import main.java.hescha.inverse_matrix.model.Matrix;

import java.text.DecimalFormat;

/**
 * Solver class that provides a solution for a system of linear equations.
 */
public class Solver {
    /*

    Format for the rounded values in the solution.
    */
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.###");

    /**
     * Solves a system of linear equations given the coefficients and constants.
     *
     * @param coefficients the coefficients of the variables in the system of linear equations
     * @param constants    the constants in the system of linear equations
     * @return the solution of the system of linear equations
     */
    public static double[] solve(double[][] coefficients, double[] constants) {
        // get the number of variables in the system of linear equations
        int n = coefficients.length;

        // create a Matrix object from the coefficients
        Matrix A = new Matrix(coefficients);

        // create a Matrix object from the constants
        Matrix B = new Matrix(constants, n);

        // find the inverse of matrix A and multiply it with matrix B
        Matrix X = A.inverse().times(B);

        // get a row-packed copy of the result matrix
        double[] rowPackedCopy = X.getRowPackedCopy();

        // round the values in the solution
        round(rowPackedCopy);

        // return the solution
        return rowPackedCopy;
    }

    /**
     * Rounds the values in the given array.
     *
     * @param rowPackedCopy the array of values to be rounded
     */
    private static void round(double[] rowPackedCopy) {
        // loop through the array
        for (int i = 0; i < rowPackedCopy.length; i++) {
        // round each value in the array
            rowPackedCopy[i] = Double.parseDouble(decimalFormat.format(rowPackedCopy[i]));
        }
    }
}
