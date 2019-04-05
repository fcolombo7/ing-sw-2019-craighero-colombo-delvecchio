package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NullMatrixHelperExceptoin;

public class MatrixHelper {
    private boolean[][] matrix;
    private int rowLength;
    private int colLength;

    public MatrixHelper(boolean[][] matrix) throws NullMatrixHelperExceptoin {
        if(matrix==null) throw  new NullMatrixHelperExceptoin("(matrix is null)");
        if(matrix[0]==null) throw new NullMatrixHelperExceptoin("(matrix[0] is null)");
        rowLength=matrix.length;
        colLength=matrix[0].length;
        this.matrix=new boolean[rowLength][colLength];
        for (int i=0;i<rowLength;i++){
            if(matrix[i]==null) {
                String msg="(matrix["+i+"] is null)";
                throw new NullMatrixHelperExceptoin(msg);
            }
            System.arraycopy(matrix[i],0,this.matrix[i],0,colLength);
        }
    }

    public boolean[][] toBooleanMatrix(){
        boolean [][]ret=new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++)
            System.arraycopy(matrix[i],0,ret[i],0,colLength);
        return ret;
    }

    public int getrowLength() {
        return rowLength;
    }

    public int getcolLength() {
        return colLength;
    }
    
    public MatrixHelper bitWiseAnd(MatrixHelper matrix) throws NullMatrixHelperExceptoin{
        if(matrix==null) throw new NullMatrixHelperExceptoin("(matrix is null)");
        if(matrix.rowLength!=this.rowLength) throw new NullMatrixHelperExceptoin("(The rows length of the matrices are different)");
        if(matrix.colLength!=this.colLength) throw new NullMatrixHelperExceptoin("(The columns length of the matrices are different)");
        boolean[][] m2=matrix.toBooleanMatrix();
        boolean[][] ret= new boolean[rowLength][colLength];
        for(int i=0;i<rowLength;i++){
            for(int j=0;j<colLength;j++)
                ret[i][j]=m2[i][j]&&this.matrix[i][j];
        }
        return new MatrixHelper(ret);
    }
}
