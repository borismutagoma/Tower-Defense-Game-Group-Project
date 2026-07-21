package towerdefense.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import towerdefense.cell.Cell;
import towerdefense.util.Position;

public class PathTest {

    private Path path;

    @BeforeEach
    public void before(){
        path = new Path();
        path.addCell(new Cell(new Position(0, 0)));
    }

    @Test
    public void PathInitializationWithAListInParameter(){
        List<Cell> list = new ArrayList<>();
        Cell c1 = new Cell(new Position(0, 0));
        list.add(c1);
        list.add(new Cell(new Position(0, 1)));

        Path pathWithList = new Path(list);
        assertEquals(list, pathWithList.getCells());
        assertEquals(pathWithList.getCell(0), c1);
        assertTrue(pathWithList.size() == 2);
    }

    @Test
    public void IsEmptyReturnsTheCorrectBoolean(){
        Path path2 = new Path();
        assertTrue(path2.isEmpty());
        path2.addCell(new Cell(new Position(0, 0)));
        assertFalse(path2.isEmpty());
    }

    @Test
    public void getStartReturnsTheFirstCell(){
        assertEquals(path.getStart(), path.getCell(0));
    }

    @Test
    public void getEndReturnsTheLastCell(){
        path.addCell(new Cell(new Position(1, 0)));
        assertEquals(path.getEnd(), path.getCell(1));
    }

    @Test
    public void containsReturnsTrueIfTheCellIsPartOfThePath(){
        assertTrue(path.contains(new Cell(new Position(0, 0))));
    }
    @Test
    public void containsReturnsFalseIfTheCellNotIsPartOfThePath(){
        assertFalse(path.contains(new Cell(new Position(1, 0))));
    }

    @Test
    public void clearUnsetAndRemoveCellsPath(){
        Cell c2 = new Cell(new Position(1, 0));
        path.addCell(c2);
        assertTrue(c2.isOnPath());
        path.clear();
        assertTrue(path.isEmpty());
        assertFalse(c2.isOnPath());
    }
}
