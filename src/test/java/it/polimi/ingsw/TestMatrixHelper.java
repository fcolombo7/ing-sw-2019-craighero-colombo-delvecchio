package it.polimi.ingsw;

import it.polimi.ingsw.utils.MatrixHelper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit test for the class MatrixHelper
 */
public class TestMatrixHelper {

    /**
     * Test if MatrixHelper constructor throws NullMatrixHelperException when the parameter (matrix) is null
     */
    @Test
    public void NullMatrixInserted()
    {
        try {
            new MatrixHelper(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("Matrix is null."));
        }
    }

    /**
     * Test if MatrixHelper constructor throws MalformedMatrixHelperExcepton when the parameter (matrix) is null
     */
    @Test
    public void MalformedMatrixInserted()
    {
        try {
            new MatrixHelper(new boolean[][]{{true,false},{true,false,true}});
            fail("Expected a IllegalArgumentException to be thrown");
        }
        catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Columns length of the matrix are different."));
        }
    }

    /**
     * Test if MatrixHelper constructor does not throw Exception when valid parameter is inserted
     */
    @Test
    public void CorrectMatrixInserted()
    {
        boolean[][] mat= new boolean[][]{{true,true,false},{false,true,true}};
        StringBuilder msg=new StringBuilder();
        msg.append("MatrixHelper\n{");
        for(int i=0;i<mat.length;i++) {
            msg.append("{");
            for (int j = 0; j < mat[0].length-1; j++)
                msg.append(mat[i][j]).append(", ");
            msg.append(mat[i][mat[0].length-1]).append("}\n");
        }
        msg.append("}");
        assertEquals(msg.toString(),new MatrixHelper(mat).toString());
    }

    /**
     * Test if MatrixHelper bitWiseAnd throws MalformedMatrixHelperException when the parameter (matrix) is not valid
     */
    @Test
    public void bitWiseAndErrorRow()
    {
        try {
            MatrixHelper matrix= new MatrixHelper(new boolean[][]{{true,true,false},{false,true,true}});
            MatrixHelper matrix2= new MatrixHelper(new boolean[][]{{true,true,false},{false,true,true},{false,true,true}});
            matrix.bitWiseAnd(matrix2);
            fail("Expected a IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Rows length of the matrices are different."));
        }
    }

    /**
     * Test if MatrixHelper bitWiseAnd throws MalformedMatrixHelperException when the parameter (matrix) is not valid
     */
    @Test
    public void bitWiseAndErrorCol()
    {
        try {
            MatrixHelper matrix= new MatrixHelper(new boolean[][]{{true,true,false},{false,true,true}});
            MatrixHelper matrix2= new MatrixHelper(new boolean[][]{{true,true,false,true},{false,true,true,false}});
            matrix.bitWiseAnd(matrix2);
            fail("Expected a IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Columns length of the matrices are different."));
        }
    }

    /**
     * Test if MatrixHelper constructor does not throw Exception when valid parameter is inserted
     */
    @Test
    public void CorrectBitWiseAnd()
    {
        MatrixHelper m1=new MatrixHelper(new boolean[][]{{true,true,false},{false,true,true}});
        MatrixHelper m2=new MatrixHelper(new boolean[][]{{false,true,false},{true,true,true}});

        boolean[][] mat= new boolean[][]{{false,true,false},{false,true,true}};
        StringBuilder msg=new StringBuilder();
        msg.append("MatrixHelper\n{");
        for(int i=0;i<mat.length;i++) {
            msg.append("{");
            for (int j = 0; j < mat[0].length-1; j++)
                msg.append(mat[i][j]).append(", ");
            msg.append(mat[i][mat[0].length-1]).append("}\n");
        }
        msg.append("}");
        assertEquals(msg.toString(),m1.bitWiseAnd(m2).toString());

    }

}
