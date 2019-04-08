package it.polimi.ingsw.model;

/**
 * This abstract class represents a Square
 */
public abstract class Square {
    /**
     * This attribute contains the Square color room.
     */
    private RoomColor roomColor;
    /**
     * This attribute contains a matrix representing which squares are visible from the current position (square).
     */
    private MatrixHelper visibilityMatrix;

    /**
     * This attribute represents the doors (if any) connected to the Square
     */
    private boolean[] doors;

    /**
     * This constructor instantiates a Square
     * @param roomColor RoomColor representing the color of the room
     * @param doors  boolean[] representing the doors connected to the Square
     */
    public Square(RoomColor roomColor, boolean[] doors) {
        if(roomColor==null)
            throw  new NullPointerException("The roomColor param can't have null value.");
        if(doors==null)
            throw  new NullPointerException("The doors param can't have null value.");
        if(doors.length!=4)
            throw new IllegalArgumentException("The doors param length must be 4.");
        this.roomColor=roomColor;
        this.doors=new boolean[4];
        System.arraycopy(doors,0,this.doors,0,4);
    }

    /**
     * This method return the RoomColor of the square
     * @return RoomColor representing the color of the room
     */
    public RoomColor getRoomColor(){
        return roomColor;
    }

    /**
     * This method set the visibilityMatrix of the Square
     * @param visibilityMatrix MatrixHelper representing which squares are visible from the current position (square).
     */
    public void setVisibilityMatrix(MatrixHelper visibilityMatrix){
        if(visibilityMatrix==null) throw new NullPointerException("Param 'visibilityMatrix' is null.");
        this.visibilityMatrix=visibilityMatrix;
    }

    /**
     * This method return visibilityMatrix of the square
     * @return MatrixHelper representing which squares are visible from the current position (square).
     */
    public MatrixHelper getVisibilityMatrix() {
        return visibilityMatrix;
    }

    /**
     * This method return a true value if there is a door connected to the square in the given direction.
     * @param direction Direction representing the position of the door you want to evaluate.
     * @return Boolean representing the presence of the door in the given direction.
     */
    public boolean hasDoor(Direction direction) {
        if(direction==null) throw new NullPointerException("Param 'direction' is null.");
        return doors[direction.ordinal()];
    }

    /**
     * This method return a String representation of the instantiated Square
     * @return String representing the instantiated Square
     * */
    @Override
    public String toString(){
        StringBuilder msg=new StringBuilder();
        msg.append("{\n");
        msg.append("Room color: ").append(roomColor.name()).append(",\n");
        msg.append("Doors: ").append("{NORTH: ").append(doors[Direction.NORTH.ordinal()]).append(",")
        .append(" EAST: ").append(doors[Direction.EAST.ordinal()]).append(",")
        .append(" SOUTH: ").append(doors[Direction.SOUTH.ordinal()]).append(",")
        .append(" WEST: ").append(doors[Direction.WEST.ordinal()]).append("},\n");
        msg.append("Visibility Matrix: ");
        if(visibilityMatrix==null) msg.append("null\n");
        else msg.append(visibilityMatrix.toString()).append("\n");
        return msg.toString();
    }

    /**
     * This method return a true value if the user can grab anything from the square
     * @return boolean representing the possibility of the user to grab anything in the square
     */
    public abstract boolean canGrab();

    /**
     * This method return a true value if the Square is full and no other objects can be put on the square.
     * @return boolean representing if the Square is full and no other objects can be put on the square.
     */
    public abstract boolean isFull();

}
