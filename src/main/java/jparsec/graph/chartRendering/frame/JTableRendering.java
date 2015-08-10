package jparsec.graph.chartRendering.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import jparsec.ephem.Functions;
import jparsec.graph.DataSet;
import jparsec.io.FileIO;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;

/**
 * A class to create and manage tables. Implementation and support is just for
 * basic tables, but anyway useful to avoid code duplication.
 * @author T. Alonso Albi - OAN (Spain)
 */
public class JTableRendering implements PropertyChangeListener, MouseListener {

	/** The table. */
	private JTable table;
	private boolean ascending[], editable[];
	private String lineTable[][] = null, lineTableOriginal[][];
	private boolean valueChanging = false;
    private JTableHeader tableHeader;
	private final String SEPARATOR = "@<>@"; // A strange separator that is never used in the fields 
	private String selectedRow;
	private int colColumn = -1;
	private String[] colVal;
	private Color[] colCol;
	private String columnNames[];
	private Class<?> columnClasses[];

	/**
	 * Constructor for a table.
	 * @param columns The names of the columns.
	 * @param classes The classes in each column. Valid values are numbers (Integer, ...), Boolean
	 * and String classes. Set the entire array to null to use String for all columns, or any of the elements
	 * to null to use String but letting the value to be parsed to an angle formatted by the methods in 
	 * {@linkplain Functions} or a date formatted in {@linkplain TimeElement}.
	 * @param editable True or false to allow or not to edit columns. Set to
	 * null to set all to false for String and true for Boolean.
	 * @param table The table, ordered as [rows][columns].
	 * @throws JPARSECException If an error occurs.
	 */
	public JTableRendering(String columns[], Class<?> classes[], boolean editable[], String table[][]) throws JPARSECException  {
		columnNames = columns;
		columnClasses = classes;
		this.editable = editable;
		updateData(table);
		if (columnClasses == null) {
			columnClasses = new Class<?>[columns.length];
			for (int i=0; i<columns.length; i++) {
				columnClasses[i] = String.class;
			}
		}
		createTable();
	}

	private void updateData(String table[][]) {
		lineTable = new String[table.length][table[0].length+1];
		lineTableOriginal = new String[table.length][table[0].length+1];
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[i].length; j++) {
				lineTable[i][j] = table[i][j];
				lineTableOriginal[i][j] = table[i][j];
			}			
			lineTable[i][lineTable[0].length-1] = ""+i;
			lineTableOriginal[i][lineTable[0].length-1] = ""+i;
		}
	}
	
	/**
	 * Returns the table component.
	 * @return The table.
	 */
	public JTable getComponent() {
		return table;
	}
	
	/**
	 * Returns a copy of the original data in the table.
	 * @return The data, ordered as [rows][columns].
	 */
	public String[][] getOriginalTableData() {
		String ltOriginal[][] = new String[lineTable.length][lineTable[0].length-1];
		for (int i=0; i<lineTable.length; i++) {
			for (int j=0; j<lineTable[i].length-1; j++) {
				ltOriginal[i][j] = lineTableOriginal[i][j];
				ltOriginal[i][j] = lineTableOriginal[i][j];
			}			
		}
		return ltOriginal;
	}
	
	/**
	 * Returns a copy of the current data in the table.
	 * @return The data, ordered as [rows][columns].
	 * @throws JPARSECException If an error occurs.
	 */
	public String[][] getTableData() throws JPARSECException {
		String lt[][] = new String[lineTable.length][lineTable[0].length-1];
		double index[] = new double[lineTable.length];
		for (int i=0; i<lineTable.length; i++) {
			for (int j=0; j<lineTable[i].length-1; j++) {
				lt[i][j] = lineTable[i][j];
				lt[i][j] = lineTable[i][j];
			}	
			index[i] = Integer.parseInt(lineTable[i][lineTable[0].length-1]);
		}
		
		lt = (String[][]) DataSet.sortInCrescent(lt, index);
		return lt;
	}
	
	/**
	 * Converts the row index from view to model. The underlying methods
	 * of the table component should not be used in this class to convert
	 * row indexes, use the methods for this instance.
	 * @param viewRowIndex Row index in the visible table.
	 * @return Row index in the table model.
	 */
	public int convertRowIndexToModel(int viewRowIndex) {
		return Integer.parseInt(lineTable[viewRowIndex][lineTable[0].length-1]);
	}

	/**
	 * Converts the row index from model to view. The underlying methods
	 * of the table component should not be used in this class to convert
	 * row indexes, use the methods for this instance.
	 * @param modelRowIndex Row index in the table model.
	 * @return Row index in the visible table.
	 */
	public int convertRowIndexToView(int modelRowIndex) {
		for (int i=0; i<lineTable.length; i++) {
			if (convertRowIndexToModel(i) == modelRowIndex) return i;
		}
		return modelRowIndex;
	}
	
	/**
	 * Sets the colors for the rows.
	 * @param column The column to use to select the color based on its value.
	 * @param values A set of possible values to select a different color from default.
	 * @param col The set of colors so that in row 'row' col[i] is used when table[row][column] = values[i].
	 */
	public void setRowColor(int column, String values[], Color col[]) {
		this.colColumn = column;
		this.colVal = values;
		this.colCol = col;
	}

	/**
	 * Sets the width in pixels for a given column at startup.
	 * @param width The set of widths for each column.
	 */
	public void setColumnWidth(int width[]) {
		for (int i=0; i<width.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(width[i]);			
		}
	}

	/**
	 * Sets the name of the columns. No check is done.
	 * @param names The new names.
	 */
	public void setColumnNames(String names[]) {
		this.columnNames = names.clone();
	}

	/**
	 * Returns the selected row after a mouse click event.
	 * @param separator Separator to use for the items in the row.
	 * @return Selected row.
	 */
	public String getSelectedRow(String separator) {
		return DataSet.replaceAll(selectedRow, SEPARATOR, separator, true);
	}
	
	private void createTable() throws JPARSECException {
		final int columns = columnNames.length;
		ascending = new boolean[columns];
		for (int i=0; i<ascending.length; i++)
		{
			ascending[i] = true;
		}
		
	    TableModel dataModel = new AbstractTableModel() {
	    	static final long serialVersionUID = 1L;
	         public int getColumnCount() { return columns; }
	         public int getRowCount() { return lineTable.length;}
	         public String getColumnName(int i) {
	        	 return columnNames[i];
	         }
	         public Class<?> getColumnClass(int i)
	         {
	        	 if (columnClasses[i] == null) return String.class;
	        	 return columnClasses[i];
	         }
	         public Object getValueAt(int row, int col) {
	        	 if (columnClasses[col] == Boolean.class) return Boolean.parseBoolean(lineTable[row][col]);
	        	 //if (columnClasses[col] == Integer.class) return Integer.parseInt(lineTable[row][col]);
	        	 String out = lineTable[row][col];
	        	 return out;
	         }
	         public void setValueAt(Object b, int row, int col) {
	        	 if (!isCellEditable(row, col) || valueChanging) return;
	        	 if (columnClasses[col] == Boolean.class) {
	        		 boolean a = (Boolean) b;
        			 String lt = lineTable[row][col];
	        		 if (a) {
	        			 if (lt.equals("false")) {
	        				 lineTable[row][col] = "true";
	    	        		 valueChanging = true;
	        			 }
	        		 } else {
	        			 if (lt.equals("true")) {
	        				 lineTable[row][col] = "false";
	    	        		 valueChanging = true;
	        			 }
	        		 }
	        		 return;
	        	 }
	        	 lineTable[row][col] = (String) b;
        		 valueChanging = true;
	         }
	         public boolean isCellEditable(int row, int column)
	         {
	        	boolean edit = false;
	        	if (columnClasses[column] == Boolean.class) edit = true;
	        	if (editable != null) edit = editable[column];
	        	return edit;
	         }
	    };
	      
		table = new JTable(dataModel) 
		{
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer,
                    int rowIndex, int vColIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
				int row = this.convertRowIndexToModel(rowIndex);
				c.setForeground(null);
				if (colColumn >= 0 && colCol != null && colVal != null) {
					String f = lineTable[row][colColumn];
					int index = DataSet.getIndex(colVal, f);
					if (index >= 0) {
						c.setBackground(colCol[index]);
						return c;
					}
				}
				if (isCellSelected(rowIndex, vColIndex)) {
					c.setBackground(getSelectionBackground());
				} else {
					// If not shaded, match the table's background
					c.setBackground(getBackground());
				}		
				return c;
			}
		};
		table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setAutoCreateColumnsFromModel(false);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.addPropertyChangeListener(this);
		table.addMouseListener(this);
		tableHeader = table.getTableHeader();
		tableHeader.addMouseListener(this);
		Font h = tableHeader.getFont();
		tableHeader.setFont(new Font(h.getFontName(), Font.BOLD, h.getSize()));		
		updateTable(null, true);
	}

	/**
	 * Updates the contents of the table.
	 * @param stable The new table.
	 * @param show True to update the view.
	 */
	public void updateTable(String stable[][], boolean show) {
		if (stable != null) {
			updateData(stable);
		}
		int index = -1;
        if (tableSorted >= 0) sortColumn(table.getModel(), tableSorted, tableSortAscending);
        if (show) {
	    	table.revalidate();
			if (selectedRow != null) {
		    	  for (int i=0; i<lineTable.length; i++) {
		    		  String li = DataSet.toString(lineTable[i], SEPARATOR);
		    		  if (li.equals(selectedRow)) {
		    			  index = i;
		    			  break;
		    		  }
		    	  }
			}
    		if (index >= 0) {
    			table.setRowSelectionInterval(index, index);
    		} else {
    			table.clearSelection();
    		}
	    	for (int i=0; i<columnNames.length; i++) {
	    		tableHeader.getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
	    	}
	    	table.repaint();
	    	tableHeader.repaint();
        }
	}
	
    //  Regardless of sort order (ascending or descending), null values always appear last.
    // colIndex specifies a column in model.
	private int tableSorted = -1;
	private boolean tableSortAscending = false;
    private void sortColumn(TableModel model, int colIndex, boolean ascending) {
    	// Sort the array of column data
    	String table[] = new String[lineTable.length];
    	for (int i=0; i<lineTable.length; i++) {
    		table[i] = DataSet.toString(lineTable[i], SEPARATOR);
    	}
        Arrays.sort(table, new ColumnSorter(ascending, colIndex));
    	for (int i=0; i<lineTable.length; i++) {
    		lineTable[i] = DataSet.toStringArray(table[i], SEPARATOR, false);
    	}        
        tableSorted = colIndex;
        tableSortAscending = ascending;
    }

    private void sortByColumn(int column) {
    	boolean asc = true;
    	if (this.ascending != null) {
    		asc = this.ascending[column];
    		if (asc) {
    			asc = false;
    		} else {
    			asc = true;
    		}
    		this.ascending[column] = asc;
    	}
    	
    	this.sortColumn(table.getModel(), column, this.ascending[column]);
    	table.revalidate();
    }

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
			valueChanging = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
       	if (e.getSource() == this.tableHeader) {
    		TableColumnModel columnModel = table.getColumnModel();
    		int viewColumn = columnModel.getColumnIndexAtX(e.getX());
    		if (viewColumn != -1) {
	    		int column = table.convertColumnIndexToModel(viewColumn);
	    		if (e.getClickCount() == 1 && column != -1) {
	    		      this.sortByColumn(column);
	    		      if (selectedRow != null) {
	    		    	  for (int i=0; i<lineTable.length; i++) {
	    		    		  String li = DataSet.toString(lineTable[i], SEPARATOR);
	    		    		  if (selectedRow.equals(li)) {
	    		    			  table.setRowSelectionInterval(i, i);
	    		    			  return;
	    		    		  }
	    		    	  }
	    		      }
	    		}
    		}
    		return;
    	}
  		if (e.getSource() == table) {
			int row = table.getSelectedRow();
			if (row < 0) return;
       		this.selectedRow = DataSet.toString(lineTable[row], SEPARATOR);
       		return;
  		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	class ColumnSorter implements Comparator<Object> {
	    boolean ascending;
	    int column;
	    ColumnSorter(boolean ascending, int column) {
	        this.ascending = ascending;
	        this.column = column;
	    }
	    public int compare(Object a, Object b) {
	        // Treat empty strains like nulls
	        if (a instanceof String && ((String)a).length() == 0) {
	            a = null;
	        }
	        if (b instanceof String && ((String)b).length() == 0) {
	            b = null;
	        }

	        // Sort nulls so they appear last, regardless
	        // of sort order
	        if (a == null && b == null) {
	            return 0;
	        } else if (a == null) {
	            return 1;
	        } else if (b == null) {
	            return -1;
	        } else {
	        	String fa = FileIO.getField(column + 1, (String) a, SEPARATOR, false);
	        	String fb = FileIO.getField(column + 1, (String) b, SEPARATOR, false);
	        	if (columnClasses[column] == Integer.class || columnClasses[column] == Double.class 
	        			|| columnClasses[column] == Long.class) {
	        		Double va = Double.parseDouble(fa);
	        		Double vb = Double.parseDouble(fb);
		            if (ascending) {
		                return va.compareTo(vb);
		            } else {
		                return vb.compareTo(va);
		       		}	        		
	        	} else {
	        		if (columnClasses[column] == null) {
	        			try {
	        				Double ffa = Functions.parseRightAscension(fa);
	        				Double ffb = Functions.parseRightAscension(fb);
	    		            if (ascending) {
	    		                return ffa.compareTo(ffb);
	    		            } else {
	    		                return ffb.compareTo(ffa);
	    		       		}	        		
	        			} catch (Exception exc) {
	        				try {
		        				Double ffa = Functions.parseDeclination(fa);
		        				Double ffb = Functions.parseDeclination(fb);
		    		            if (ascending) {
		    		                return ffa.compareTo(ffb);
		    		            } else {
		    		                return ffb.compareTo(ffa);
		    		       		}	        		
	        				} catch (Exception exc2) {
	        					try {
	        						if (!DataSet.isDoubleOrMathOperationFastCheck(fa) ||
	        								!DataSet.isDoubleOrMathOperationFastCheck(fb)) throw new Exception("is not an operation");
	        		        		Double va = DataSet.getDoubleValueWithoutLimit(fa);
	        		        		Double vb = DataSet.getDoubleValueWithoutLimit(fb);
	        			            if (ascending) {
	        			                return va.compareTo(vb);
	        			            } else {
	        			                return vb.compareTo(va);
	        			       		}	      
	        					} catch (Exception exc3) {
	        						try {
		    	        				Double ffa = (new TimeElement(fa)).astroDate.jd();
		    	        				Double ffb = (new TimeElement(fb)).astroDate.jd();
				    		            if (ascending) {
				    		                return ffa.compareTo(ffb);
				    		            } else {
				    		                return ffb.compareTo(ffa);
				    		       		}	 
	        						} catch (Exception exc4) {}
	        					}
	        				}
	        			}
	        		}
		            if (ascending) {
		                return fa.compareTo(fb);
		            } else {
		                return fb.compareTo(fa);
		       		}
	        	}
	        }
	    }
	}
}
