package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;

import java.util.ArrayList;
import java.util.List;

public class randomPlayer implements Player {
    @Override
    public Direction nextMove(Labyrinth l) {
        List<Direction> possibleDirections = new ArrayList<>(l.possibleMoves());
        int random = (int)(Math.random()*possibleDirections.size());
        while (true) {
            switch (random) {
                case 0:
                    if (possibleDirections.contains(Direction.EAST)) {
                        return Direction.EAST;
                    }
                    break;
                case 1:
                    if (possibleDirections.contains(Direction.WEST)){
                        return Direction.WEST;
                    }
                    break;
                case 2: if (possibleDirections.contains(Direction.SOUTH)){
                    return Direction.SOUTH;
                }
                break;
                case 3: if (possibleDirections.contains(Direction.NORTH)){
                    return Direction.NORTH;
                }
                break;
            }
        }
    }
}
