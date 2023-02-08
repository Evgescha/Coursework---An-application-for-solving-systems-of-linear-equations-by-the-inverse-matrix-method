package main.java.hescha.inverse_matrix.model;

public class Matrix {
    // A two-dimensional array to store the elements of the matrix
    private double[][] elements;

    // Number of rows of the matrix
    private int rows;

    // Number of columns of the matrix
    private int columns;

    // A one-dimensional array to store the matrix elements as a packed vector
    public double[] vectors;

    /**
     * Constructs a matrix with the given number of rows and columns
     *
     * @param rows    number of rows
     * @param columns number of columns
     */
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.elements = new double[rows][columns];
    }

    /**
     * Constructs a matrix from the given two-dimensional array
     *
     * @param matrix two-dimensional array of elements
     */
    public Matrix(double[][] matrix) {
        this.rows = matrix.length;
        this.columns = matrix[0].length;

        // Ensure all the rows of the matrix have the same length
        for (int var2 = 0; var2 < this.rows; ++var2) {
            if (matrix[var2].length != this.columns) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.elements = matrix;
    }

    /**
     * Constructs a matrix from the given packed vector and the number of rows
     *
     * @param rows packed vector of elements
     * @param var2 number of rows
     */
    public Matrix(double[] rows, int var2) {
        this.rows = var2;
        this.columns = var2 != 0 ? rows.length / var2 : 0;

        // Ensure that the array length is a multiple of the number of rows
        if (var2 * this.columns != rows.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        } else {
            this.elements = new double[var2][this.columns];
            for (int var3 = 0; var3 < var2; ++var3) {
                for (int var4 = 0; var4 < this.columns; ++var4) {
                    this.elements[var3][var4] = rows[var3 + var4 * var2];
                }
            }
        }
    }

    /**
     * Makes a deep copy of this matrix
     *
     * @return deep copy of the matrix
     */
    public Matrix copy() {
        Matrix var1 = new Matrix(this.rows, this.columns);
        double[][] var2 = var1.getArray();
        for (int var3 = 0; var3 < this.rows; ++var3) {
            for (int var4 = 0; var4 < this.columns; ++var4) {
                var2[var3][var4] = this.elements[var3][var4];
            }
        }
        return var1;
    }

    public Object clone() {
        return this.copy();
    }

    public double[][] getArray() {
        return this.elements;
    }

    /**
     * Returns a packed copy of this matrix as a 1D array.
     * The elements are stored in row-major order.
     *
     * @return a packed copy of this matrix
     */
    public double[] getRowPackedCopy() {
        double[] packedCopy = new double[this.rows * this.columns];
        for (int row = 0; row < this.rows; ++row) {
            for (int column = 0; column < this.columns; ++column) {
                packedCopy[row * this.columns + column] = this.elements[row][column];
            }
        }
        return packedCopy;
    }

    /**
     * Returns the sum of this matrix and the given matrix.
     * Both matrices must have the same dimensions.
     *
     * @param matrix the matrix to add to this matrix
     * @return the sum of this matrix and the given matrix
     * @throws IllegalArgumentException if the matrices have different dimensions
     */
    public Matrix plus(Matrix matrix) {
        this.checkMatrixDimensions(matrix);
        Matrix result = new Matrix(this.rows, this.columns);
        double[][] resultArray = result.getArray();
        for (int row = 0; row < this.rows; ++row) {
            for (int column = 0; column < this.columns; ++column) {
                resultArray[row][column] = this.elements[row][column] + matrix.elements[row][column];
            }
        }
        return result;
    }

    /**
     * Returns the product of this matrix and the given matrix.
     * The number of columns of this matrix must be equal to the number of rows of the given matrix.
     *
     * @param matrix the matrix to multiply with this matrix
     * @return the product of this matrix and the given matrix
     * @throws IllegalArgumentException if the number of columns of this matrix is not equal to the number of rows of the given matrix
     */
    public Matrix times(Matrix matrix) {
        if (matrix.rows != this.columns) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        } else {
            Matrix result = new Matrix(this.rows, matrix.columns);
            double[][] resultArray = result.getArray();
            double[] tempArray = new double[this.columns];
            for (int column = 0; column < matrix.columns; ++column) {
                for (int i = 0; i < this.columns; ++i) {
                    tempArray[i] = matrix.elements[i][column];
                }
                for (int row = 0; row < this.rows; ++row) {
                    double[] currentRow = this.elements[row];
                    double sum = 0.0;
                    for (int i = 0; i < this.columns; ++i) {
                        sum += currentRow[i] * tempArray[i];
                    }
                    resultArray[row][column] = sum;
                }
            }
            return result;
        }
    }

    /**
     * Inverse method calculates the inverse of the matrix.
     *
     * @return The inverse of the matrix
     */
    public Matrix inverse() {
        Matrix identity = identity(this.rows, this.rows);
        return inverse(identity);
    }

    /**
     * identity method returns the identity matrix with the given dimensions.
     *
     * @param var0 Number of rows in the matrix
     * @param var1 Number of columns in the matrix
     * @return Identity matrix with the given dimensions
     */
    public static Matrix identity(int var0, int var1) {
        Matrix var2 = new Matrix(var0, var1);
        double[][] var3 = var2.getArray();
        // Fill the identity matrix with ones on the main diagonal and zeros elsewhere
        for (int var4 = 0; var4 < var0; ++var4) {
            for (int var5 = 0; var5 < var1; ++var5) {
                var3[var4][var5] = var4 == var5 ? 1.0 : 0.0;
            }
        }
        return var2;
    }

    /**
     * checkMatrixDimensions method checks if the matrix dimensions match the given matrix.
     *
     * @param var1 The matrix to compare dimensions with
     * @throws IllegalArgumentException if the dimensions do not match
     */
    private void checkMatrixDimensions(Matrix var1) {
        if (var1.rows != this.rows || var1.columns != this.columns) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }

    /**
     * inverse method calculates the inverse of a given matrix.
     *
     * @param matrix The matrix to calculate the inverse for
     * @return The inverse of the matrix
     */
    public Matrix inverse(Matrix matrix) {
        // Check that the matrix is square
        assert (matrix.rows == matrix.columns);

        // Augment the matrix by an identity matrix
        Matrix tmp = new Matrix(matrix.rows, matrix.columns * 2);
        for (int row = 0; row < rows; ++row) {
            // Copy elements from the original matrix
            System.arraycopy(elements[row], 0, tmp.elements[row], 0, columns);
            tmp.elements[row][row + columns] = 1;
        }
        // Convert the augmented matrix to reduced row echelon form
        tmp.toReducedRowEchelonForm();
        // Create a new matrix to store the inverse
        Matrix inv = new Matrix(rows, columns);
        for (int row = 0; row < rows; ++row)
            // Copy the inverted elements from the right half of the augmented matrix
            System.arraycopy(tmp.elements[row], columns, inv.elements[row], 0, columns);
        return inv;
    }

    /**
     * Transforms this matrix into reduced row echelon form (RREF) using the Gaussian elimination method.
     * A matrix in RREF is in echelon form and its leading non-zero entries, called pivots,
     * are ones and are located in the first non-zero entry of each row.
     */
    public void toReducedRowEchelonForm() {
        for (int row = 0, lead = 0; row < rows && lead < columns; ++row, ++lead) {
            int i = row;
            // Find a non-zero entry in the current column
            while (elements[i][lead] == 0) {
                if (++i == rows) {
                    i = row;
                    if (++lead == columns)
                        // If there are no more columns to process, return
                        return;
                }
            }
            // Swap the current row with the row containing the non-zero entry
            swapRows(i, row);
            // Divide the current row by the pivot
            if (elements[row][lead] != 0) {
                double f = elements[row][lead];
                for (int column = 0; column < columns; ++column)
                    elements[row][column] /= f;
            }
            // Subtract a multiple of the current row from each row below to make their
            // corresponding entries in the current column zero
            for (int j = 0; j < rows; ++j) {
                if (j == row)
                    continue;
                double f = elements[j][lead];
                for (int column = 0; column < columns; ++column)
                    elements[j][column] -= f * elements[row][column];
            }
        }
    }

    /**
     * Swaps two rows of this matrix.
     *
     * @param i the index of the first row
     * @param j the index of the second row
     */
    private void swapRows(int i, int j) {
        double[] tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
    }

    /**
     * Returns the vector representation of this matrix.
     *
     * @return the vector representation of this matrix
     */
    public double[] getVectors() {
        return vectors;
    }

    /**
     * Sets the vector representation of this matrix.
     *
     * @param vectors the new vector representation
     */
    public void setVectors(double[] vectors) {
        this.vectors = vectors;
    }

}


