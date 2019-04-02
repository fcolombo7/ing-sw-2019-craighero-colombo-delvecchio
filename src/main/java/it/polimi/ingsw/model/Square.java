package it.polimi.ingsw.model;

public abstract class Square {
    private Color color;
    private MatrixHelper visibilityMatrix;
    private boolean[] doors;

    public Square(Color color, boolean[] doors){}

    public Color getColor(){
        return color;
    }

    public void setVisibilityMatrix(MatrixHelper visibilityMatrix){}

    public boolean hasDoor(Direction direction){
        return true;
    }

}
