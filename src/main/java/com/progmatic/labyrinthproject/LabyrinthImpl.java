package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    int labyrinthWidth;
    int labyrinthHeight;
    Map<Coordinate, CellType> map = new HashMap<>();
    Coordinate playerActualCoordinate;


    public LabyrinthImpl() {
    labyrinthWidth = 0;
    labyrinthHeight = 0;
    }

    @Override
    public int getWidth() {

        if (labyrinthWidth != 0) {
            return labyrinthWidth;
        } else {
            return -1;
        }
    }

    @Override
    public int getHeight() {
        if (labyrinthHeight != 0) {
            return labyrinthHeight;
        } else {
            return -1;
        }
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            setSize(width, height);
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            setCellType(new Coordinate(ww, hh), CellType.WALL);
                            break;
                        case 'E':
                            setCellType(new Coordinate(ww, hh), CellType.START);
                            break;
                        case 'S':
                            setCellType(new Coordinate(ww, hh), CellType.END);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException | CellException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (!map.containsKey(c)) {
            throw new CellException(c, "invalid coordinate nr.");
        }
        return map.get(c);
    }

    @Override
    public void setSize(int width, int height) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Coordinate c = new Coordinate(j,i);
                map.putIfAbsent(c, CellType.EMPTY);
            }
        }
        labyrinthWidth = width;
        labyrinthHeight = height;

    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (!map.containsKey(c)) {
            throw new CellException(c, "invalid coordinate nr.");
        }
        map.put(c,type);
        if (type.equals(CellType.START)) {
            playerActualCoordinate = new Coordinate(c.getCol(), c.getRow());
        }

    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerActualCoordinate;
    }

    @Override
    public boolean hasPlayerFinished() {

        for (Map.Entry<Coordinate, CellType> coordinateCellTypeEntry : map.entrySet()) {
            if (!(coordinateCellTypeEntry.getValue().equals(CellType.END)) && getPlayerPosition().equals(coordinateCellTypeEntry.getKey())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> directions = new ArrayList<>();
        Coordinate c = getPlayerPosition();
        Coordinate nextStep;
        int width = getWidth();
        int height = getHeight();
        if (width >= c.getCol() + 1) {
            nextStep = new Coordinate(c.getCol() + 1, c.getRow());
            if (map.get(nextStep).equals(CellType.EMPTY)) {
                directions.add(Direction.EAST);
            }
        }
        if (width <= c.getCol() - 1) {
            nextStep = new Coordinate(c.getCol() - 1, c.getRow());
            if (map.get(nextStep).equals(CellType.EMPTY)) {
                directions.add(Direction.WEST);
            }
        }
        if (height >= c.getRow() + 1) {
            nextStep = new Coordinate(c.getCol(), c.getRow() + 1);
            if (map.get(nextStep).equals(CellType.EMPTY)) {
                directions.add(Direction.SOUTH);
            }
        }
        if (height <= c.getRow() - 1) {
            nextStep = new Coordinate(c.getCol(), c.getRow() - 1);
            if (map.get(nextStep).equals(CellType.EMPTY)) {
                directions.add(Direction.NORTH);
            }
        }
        return directions;

    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        Coordinate playerPos = getPlayerPosition();
        int height = getHeight();
        int width = getWidth();
        List<Direction> possibleDirections = possibleMoves();
        if (possibleDirections.contains(direction)) {
            switch (direction) {
                case EAST:
                    Coordinate playerNextPos = new Coordinate(playerPos.getCol() + 1, playerPos.getRow());
                    setPlayerActualCoordinate(playerNextPos);
                    break;
                case WEST:
                     playerNextPos = new Coordinate(playerPos.getCol() - 1, playerPos.getRow());
                    setPlayerActualCoordinate(playerNextPos);
                    break;
                case SOUTH:
                     playerNextPos = new Coordinate(playerPos.getCol(), playerPos.getRow() + 1);
                    setPlayerActualCoordinate(playerNextPos);
                    break;
                case NORTH:
                     playerNextPos = new Coordinate(playerPos.getCol(), playerPos.getRow() - 1);
                    playerActualCoordinate = new Coordinate(playerNextPos.getCol(), playerNextPos.getRow());
                    break;
            }

        } else {
            throw new InvalidMoveException();
        }

    }

    public void setPlayerActualCoordinate(Coordinate playerActualCoordinate) {
        this.playerActualCoordinate = playerActualCoordinate;
    }
}
