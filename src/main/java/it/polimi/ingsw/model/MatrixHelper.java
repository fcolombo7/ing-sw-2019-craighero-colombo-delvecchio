package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a boolean matrix
 */
public class MatrixHelper {
    /**
     * This attribute contains the matrix
     */
    private boolean[][] matrix;

    /**
     * This attribute contains the matrix row length
     */
    private int rowLength;

    /**
     * This attribute contains the matrix column length
     */
    private int colLength;

    /**
     * This constructor instantiates a MatrixHelper
     * @param matrix representing a matrix composed by boolean values
     */
    public MatrixHelper(boolean[][] matrix) {
        if(matrix==null) throw  new NullPointerException("Matrix is null.");
        rowLength=matrix.length;
        colLength=matrix[0].length;
        this.matrix=new boolean[rowLength][colLength];
        for (int i=0;i<rowLength;i++){
            if(matrix[i].length!=colLength) throw new IllegalArgumentException("Columns length of the matrix are different.");
            System.arraycopy(matrix[i],0,this.matrix[i],0,colLength);
        }
    }

    /**
     * This method convert the MatrixHelper object into a matrix of boolean value
     * @return boolean[][] representing the MatrixHelper object
     */
    public boolean[][] toBooleanMatrix(){
        boolean [][]ret=new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++)
            System.arraycopy(matrix[i],0,ret[i],0,colLength);
        return ret;
    }

    /**
     * this method return the matrix row length
     * @return int representing the matrix row length
     */
    public int getRowLength() {
        return rowLength;
    }

    /**
     * this method return the matrix column length
     * @return int representing the matrix column length
     */
    public int getColLength() {
        return colLength;
    }

    /**
     * This method calculate the Bitwise-And of the current matrix and the one that is insert by the user
     * @param matrix representing the second matrix involved in the Bitwise-And
     * @return a MatrixHelper object representing the Bitwise-And of the current matrix and the one that is insert by the user
     */
    public MatrixHelper bitWiseAnd(MatrixHelper matrix) {
        verifyMatrixParam(matrix);
        boolean[][] m2=matrix.toBooleanMatrix();
        boolean[][] ret= new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++)
                ret[i][j]=m2[i][j]&&this.matrix[i][j];
        }
        return new MatrixHelper(ret);
    }

    /**
     * This method calculate the Bitwise-Or of the current matrix and the one that is insert by the user
     * @param matrix representing the second matrix involved in the Bitwise-Or
     * @return a MatrixHelper object representing the Bitwise-Or of the current matrix and the one that is insert by the user
     */
    public MatrixHelper bitWiseOr(MatrixHelper matrix) {
        verifyMatrixParam(matrix);
        boolean[][] m2=matrix.toBooleanMatrix();
        boolean[][] ret= new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++)
                ret[i][j]=m2[i][j]||this.matrix[i][j];
        }
        return new MatrixHelper(ret);
    }

    /**
     * This method verify if the matrix inserted as parameter of an operation is valid
     * @param matrix representing the matrix involved in the operation
     */
    private void verifyMatrixParam(MatrixHelper matrix) {
        if(matrix==null) throw new NullPointerException("Matrix is null");
        if(matrix.rowLength!=this.rowLength) throw new IllegalArgumentException("Rows length of the matrices are different.");
        if(matrix.colLength!=this.colLength) throw new IllegalArgumentException("Columns length of the matrices are different.");
    }

    /**
     * This method calculate the Bitwise-Not of the current matrix
     * @return a MatrixHelper object representing the Bitwise-Not of the current matrix
     */
    public MatrixHelper bitWiseNot(){
        boolean[][] ret= new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++)
                ret[i][j]=!this.matrix[i][j];
        }
        return new MatrixHelper(ret);
    }

    /**
     * This method return a matrix composed by only false values
     * @param rowLength represent the matrix rows number
     * @param colLength represent the matrix columns number
     * @return a MatrixHelper object representing the matrix matrix composed by only false values
     */
    public static MatrixHelper allFalseMatrix(int rowLength,int colLength){
        boolean[][] mat=new boolean[rowLength][colLength];
        for (boolean[] row:mat) for (int i = 0; i < colLength; i++) row[i] = false;
        return new MatrixHelper(mat);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj==this) return true;
        if(obj.getClass()!=this.getClass())
            return false;
        MatrixHelper mat=(MatrixHelper)obj;
        if(mat.rowLength!=this.rowLength||mat.colLength!=this.colLength) return false;
        boolean[][] matrixObj=mat.toBooleanMatrix();
        for(int i=0;i<rowLength;i++) {
            for(int j=0;j<colLength;j++){
                if(this.matrix[i][j]!=matrixObj[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * This method perform a OR-decomposition of the MatrixHelper object
     * @return List<MatrixHelper> representing the OR-decomposition of the MatrixHelper object
     */
    public List<MatrixHelper> decompose() {
        List<MatrixHelper> matrixes= new ArrayList<>();
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++){
                if(matrix[i][j]){
                   boolean[][] mat=MatrixHelper.allFalseMatrix(rowLength,colLength).toBooleanMatrix();
                   mat[i][j]=true;
                   matrixes.add(new MatrixHelper(mat));
                }
            }
        }
        return matrixes;
    }

    /**
     * This method return a String representation of the instantiated MatrixHelper
     * @return String representing the instantiated MatrixHelper
     */
    @Override
    public String toString(){
        StringBuilder msg=new StringBuilder();
        msg.append("MatrixHelper\n{");
        for(int i=0;i<rowLength;i++) {
            msg.append("{");
            for (int j = 0; j < colLength-1; j++)
                msg.append(matrix[i][j]).append(", ");
            msg.append(matrix[i][colLength-1]).append("}\n");
        }
        msg.append("}");
        return msg.toString();
    }
}
