package it.polimi.ingsw.model;

public interface Requirement {
    MatrixHelper checkRequirement(String value, int[] curPos, int[] lastPos, MatrixHelper matrix);
}
