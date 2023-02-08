package main.java.hescha.inverse_matrix;

import main.java.hescha.inverse_matrix.model.Matrix;
import main.java.hescha.inverse_matrix.service.FileOpener;
import main.java.hescha.inverse_matrix.service.Solver;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MainForm {
    private JFrame frame;
    private JTable matrixTable;
    private JTable answersTable;
    private JTable vectorTable;
    private JTextField rowsTextField;
    private JTextField colsTextField;
    DefaultTableModel matrixTableModel = new DefaultTableModel();
    DefaultTableModel answerTableModel = new DefaultTableModel();
    DefaultTableModel vectorTableModel = new DefaultTableModel();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainForm window = new MainForm();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public MainForm() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        matrixTable = new JTable();
        matrixTable.setBounds(29, 37, 515, 308);
        frame.getContentPane().add(matrixTable);

        answersTable = new JTable();
        answersTable.setBounds(29, 372, 518, 35);
        frame.getContentPane().add(answersTable);

        vectorTable = new JTable();
        vectorTable.setBounds(554, 37, 51, 308);
        frame.getContentPane().add(vectorTable);

        JLabel label = new JLabel("Rows number");
        label.setBounds(615, 11, 216, 14);
        frame.getContentPane().add(label);

        rowsTextField = new JTextField("3");
        rowsTextField.setBounds(615, 34, 161, 20);
        frame.getContentPane().add(rowsTextField);
        rowsTextField.setColumns(10);

        JButton btnNewButton = new JButton("Create table");
        btnNewButton.addActionListener(e -> createTable(-1, -1));
        btnNewButton.setBounds(615, 133, 161, 23);
        frame.getContentPane().add(btnNewButton);

        JLabel label_1 = new JLabel("Columns number");
        label_1.setBounds(615, 65, 216, 14);
        frame.getContentPane().add(label_1);

        colsTextField = new JTextField("3");
        colsTextField.setBounds(615, 90, 161, 20);
        frame.getContentPane().add(colsTextField);
        colsTextField.setColumns(10);

        JButton button = new JButton("Solve");
        button.addActionListener(e -> solve());
        button.setBounds(615, 167, 161, 23);
        frame.getContentPane().add(button);

        JLabel label_2 = new JLabel("Answer");
        label_2.setBounds(29, 356, 147, 14);
        frame.getContentPane().add(label_2);

        JLabel label_3 = new JLabel("Vector");
        label_3.setBounds(549, 11, 77, 14);
        frame.getContentPane().add(label_3);

        JLabel label_4 = new JLabel("Matrix");
        label_4.setBounds(29, 11, 129, 14);
        frame.getContentPane().add(label_4);

        JButton openFileButton = new JButton("Open file");
        openFileButton.addActionListener(e -> openFile());
        openFileButton.setBounds(615, 201, 161, 23);
        frame.getContentPane().add(openFileButton);

        JButton saveFileButton = new JButton("Save file");
        saveFileButton.addActionListener(e -> saveFile());
        saveFileButton.setBounds(615, 251, 161, 23);
        frame.getContentPane().add(saveFileButton);
    }

    /**
     * opens a file using FileOpener and sets the matrix and vector values to the table models
     */
    private void openFile() {
        try {
            // open the file using FileOpener and get the matrix
            Matrix matrix = FileOpener.openFile();
            if (matrix != null) {
                // get the array representation of the matrix
                double[][] array = matrix.getArray();
                int rows = array.length;
                int cols = array[0].length;
                // create the table with the specified number of rows and columns
                createTable(rows, cols);
                // set the number of rows in the matrix table model
                matrixTableModel.setRowCount(rows);
                // set the number of columns in the matrix table model
                matrixTableModel.setColumnCount(cols);
                // set the values of the matrix in the matrix table model
                for (int i = 0; i < array.length; i++) {
                    for (int j = 0; j < array[0].length; j++) {
                        matrixTableModel.setValueAt(array[i][j], i, j);
                    }
                }
                // get the vectors from the matrix
                double[] vectors = matrix.getVectors();
                // set the number of rows in the vector table model
                vectorTableModel.setRowCount(vectors.length);
                // set the number of columns in the vector table model
                vectorTableModel.setColumnCount(1);
                // set the values of the vectors in the vector table model
                for (int i = 0; i < vectors.length; i++) {
                    vectorTableModel.setValueAt(vectors[i], i, 0);
                }
            }
        } catch (Exception e) {
            // print the stack trace and show an error message
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error while working with file. Please check your file.");
        }
    }

    /**
     * saves the matrix and vectors to a file
     */
    private void saveFile() {
        try {
            // create a file chooser to select the file to save
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // get the selected file to save
                File fileToSave = fileChooser.getSelectedFile();
                // get the matrix data
                double[][] matrix = readMatrix();
                // get the vector data
                double[] vector = readVector();
                // get the results
                double[] results = readResult();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                // create a file writer to write to the file
                FileWriter fw = new FileWriter(fileToSave);
                // write the number of rows and columns in the first line
                fw.append(matrixTable.getRowCount() + " " + matrixTableModel.getColumnCount());
                fw.append("\r\n");
                // write the matrix data
                for (double[] doubles : matrix) {
                    String matrixRow = Arrays.stream(doubles)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(" "));
                    fw.append(matrixRow).append("\r\n");
                }
                fw.append("\r\n");

                String vectorRow = Arrays.stream(vector)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                fw.append(vectorRow);
                fw.append("\r\n");
                fw.append("Results");
                fw.append("\r\n");
                String result = Arrays.stream(results)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                fw.append(result);
                fw.flush();
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Something goes wrong while saving file.");
        }
    }

    private void createTable(int rows, int cols) {
        matrixTable.removeAll();
        answersTable.removeAll();
        vectorTable.removeAll();
        if (rows < 0 || cols < 0) {
            rows = Integer.parseInt(rowsTextField.getText());
            cols = Integer.parseInt(colsTextField.getText());
        }
        matrixTable.setModel(matrixTableModel);
        matrixTableModel.setColumnCount(rows);
        matrixTableModel.setRowCount(cols);
        matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        matrixTable.setRowHeight(308 / cols);

        answersTable.setModel(answerTableModel);
        answerTableModel.setColumnCount(rows);
        answerTableModel.setRowCount(1);
        answersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        answersTable.setRowHeight(35);

        vectorTable.setModel(vectorTableModel);
        vectorTableModel.setRowCount(cols);
        vectorTableModel.setColumnCount(1);
        vectorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        vectorTable.setRowHeight(308 / cols);
    }


    private void solve() {
        try {
            double[][] matrixData = readMatrix();
            double[] vector = readVector();
            double[] result = Solver.solve(matrixData, vector);
            printResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Not all fields are completed or it contains wrong values.");
        }
    }

    private double[] readVector() {
        int rows = matrixTableModel.getRowCount();
        double[] vector = new double[rows];
        for (int i = 0; i < rows; i++) {
            vector[i] = Double.parseDouble(vectorTableModel.getValueAt(i, 0).toString());
        }
        return vector;
    }

    private double[] readResult() {
        int cols = matrixTableModel.getColumnCount();
        double[] vector = new double[cols];
        for (int i = 0; i < cols; i++) {
            vector[i] = Double.parseDouble(answerTableModel.getValueAt(0, i).toString());
        }
        return vector;
    }

    private double[][] readMatrix() {
        int rows = matrixTableModel.getRowCount();
        int cols = matrixTableModel.getColumnCount();
        double[][] matrixData = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrixData[i][j] = Double.parseDouble(matrixTableModel.getValueAt(i, j).toString());
            }
        }
        return matrixData;
    }

    private void printResult(double[] result) {
        answerTableModel.setColumnCount(result.length);
        for (int i = 0; i < result.length; i++) {
            answerTableModel.setValueAt(result[i], 0, i);
        }
    }
}

