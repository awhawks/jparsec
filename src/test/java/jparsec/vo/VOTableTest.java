package jparsec.vo;

import cds.savot.model.SavotVOTable;
import jparsec.io.FileIO;

public class VOTableTest {
    /**
     * For unit testing only.
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String args[]) throws Exception
    {
        VOTableMeta fieldMeta[] = new VOTableMeta[] {
            new VOTableMeta("column 1", "1", "description of c1",
                VOTableMeta.DATATYPE_FLOAT, "4", "4", "ucd c1", "unit c1"),
            new VOTableMeta("column 2", "2", "description of c2", "string", "4", "4", "ucd c2", "unit c2"),
            new VOTableMeta("column 3", "3", "description of c3", "double", "4", "4", "ucd c3", "unit c3"),
            new VOTableMeta("column 4", "4", "description of c4", "something", "4", "4", "ucd c4", "unit c4")
        };

        VOTableMeta resourceMeta = new VOTableMeta("resource 1", "r1", "description of resource 1");
        VOTableMeta tableMeta = new VOTableMeta("table 1", "t1", "description of table 1");
        String table = "r1c1   r1c2   r1c3   r1c4" + FileIO.getLineSeparator();
        table += "r2c1   r2c2   r2c3   r2c4" + FileIO.getLineSeparator();
        table += "r3c1   r3c2   r3c3   r3c4" + FileIO.getLineSeparator();
        table += "r4c1   r4c2   r4c3   r4c4" + FileIO.getLineSeparator();
        SavotVOTable s = VOTable.createVOTable(table, " ", resourceMeta, tableMeta, fieldMeta);
        String votable = VOTable.toString(s);
        System.out.println(votable);
        // Now read it and test it
        SavotVOTable vo = VizierQuery.toVOTable(votable);

        if (!VOTable.toString(vo).equals(votable)) {
            System.out.println("ERROR! Not the same votable");
        }
    }
}
