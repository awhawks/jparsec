/*
 * This file is part of JPARSEC library.
 * 
 * (C) Copyright 2006-2015 by T. Alonso Albi - OAN (Spain).
 *  
 * Project Info:  http://conga.oan.es/~alonso/jparsec/jparsec.html
 * 
 * JPARSEC library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JPARSEC library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */					
package jparsec.graph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jzy3d.colors.Color;
import org.math.plot.Plot3DPanel;
import org.math.plot.canvas.Plot3DCanvas;
import org.math.plot.canvas.PlotCanvas;
import org.math.plot.plotObjects.BaseLabel;

import jparsec.astronomy.Difraction;
import jparsec.astronomy.TelescopeElement;
import jparsec.io.FileIO;
import jparsec.io.image.ImageSplineTransform;
import jparsec.io.image.Picture;
import jparsec.util.JPARSECException;

/**
 * A class to create 3d charts using JMathPlot visualization library. 
 * Some features implemented in the other charts are not available here, but it is
 * possible to use the 3d features of the library. You may consider using 
 * {@linkplain CreateSurface3D} class instead, since visualization is much better.
 * <P>
 * A 3d chart created with this class can be rotated and zoomed with the mouse. The
 * chart can be moved inside the window showing it by dragging with both buttons of
 * the mouse.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class CreateChart3D implements Serializable, MouseWheelListener, MouseListener {
	static final long serialVersionUID = 1L;

	private JPanel panel;
	private ChartElement3D chart_elem;
	
    private Plot3DPanel plot;

	/**
	 * Constructor for a given chart.
	 * @param chart The chart object.
	 * @throws JPARSECException If an error occurs.
	 */
	public CreateChart3D(ChartElement3D chart)
	throws JPARSECException {
		init(chart);
	}
	
	/**
	 * Returns the component of this chart.
	 * @return The JPanel.
	 */
	public JPanel getComponent() {
		return panel;
	}
  /**
   * Convert a Color instance to a java.awt.Color equivalent.
   * @return the converted Color.
   */
  private java.awt.Color convert(Color color) {
      float rgb[] = color.toArray();
      return new java.awt.Color((int) (rgb[0] * 255), (int) (rgb[0] * 255), (int) (rgb[0] * 255));
  }

  private void init(ChartElement3D chart)
	throws JPARSECException {
		try {
			this.chart_elem = chart.clone();

			plot = new Plot3DPanel();
			if (chart_elem.showLegend)
				plot = new Plot3DPanel("SOUTH");
	
			for (int i=0; i<chart_elem.series.length; i++)
			{
			  ChartSeriesElement3D series = chart_elem.series[i];
			  java.awt.Color convertedColor = convert(series.color);
			  double[] xValues = DataSet.toDoubleValues(series.xValues);
			  double[] yValues = DataSet.toDoubleValues(series.yValues);

			  if (chart_elem.series[i].isSurface) {
					plot.addGridPlot(series.legend, convertedColor, xValues, yValues, (double[][]) series.zValues);
				} else {
					if (series.drawLines) {
						try {
							plot.addLinePlot(series.legend, convertedColor, xValues, yValues, (double[]) series.zValues);
						} catch (Exception exc)
						{
							plot.addLinePlot(series.legend, convertedColor, xValues, yValues, DataSet.toDoubleValues((String[]) series.zValues));
						}
					} else {
						if (series.isBarPlot) {
							try {
								plot.addBarPlot(series.legend, convertedColor, xValues, yValues, (double[]) series.zValues);
							} catch (Exception exc)
							{
								plot.addBarPlot(series.legend, convertedColor, xValues, yValues, DataSet.toDoubleValues((String[]) series.zValues));
							}
						} else {
							try {
								plot.addScatterPlot(series.legend, convertedColor, xValues, yValues, (double[]) series.zValues);
							} catch (Exception exc)
							{
								plot.addScatterPlot(series.legend, convertedColor,  xValues, yValues, DataSet.toDoubleValues((String[]) series.zValues));
							}
						}
					}
					if (series.pointers.length > 0) {
						for (int ix = 0; ix<series.pointers.length; ix++)
						{
							int p = Integer.parseInt(FileIO.getField(1, series.pointers[ix], " ", true))-1;
							plot.addLabel(FileIO.getRestAfterField(1, series.pointers[ix], " ", true), 
									series.color, 
									new double[] {DataSet.getDoubleValueWithoutLimit(series.xValues[p]), 
								DataSet.getDoubleValueWithoutLimit(series.yValues[p]), ((double[]) series.zValues)[p]});
						}
					}
				}

				if (!series.isSurface) {
					boolean limits = false;
					double l = (chart_elem.getxMax() - chart_elem.getxMin()) / 10.0;
					double lim[][] = new double[series.xValues.length][3];
					for (int j = 0; j<series.xValues.length; j++)
					{
						lim[j] = new double[] {0.0, 0.0, 0.0};
						String lx = DataSet.getLimit(series.xValues[j]);
						String ly = DataSet.getLimit(series.yValues[j]);
						String lz = "";
						try {
							lz = DataSet.getLimit(((String[]) series.zValues)[j]);
						} catch (Exception exc) {}
						if (lx.equals("<")) {
							lim[j][0] = -l;
							limits = true;
						}
						if (lx.equals(">")) {
							lim[j][0] = l;
							limits = true;
						}
						if (ly.equals("<")) {
							lim[j][1] = -l;
							limits = true;
						}
						if (ly.equals(">")) {
							lim[j][1] = l;
							limits = true;
						}
						if (lz.equals("<")) {
							lim[j][2] = -l;
							limits = true;
						}
						if (lz.equals(">")) {
							lim[j][2] = l;
							limits = true;
						}
					}
					if (limits) plot.addVectortoPlot(i, lim);
				}

				if (!series.isSurface && (series.dxValues != null ||
						series.dyValues != null || series.dzValues != null)) {
					double box[][] = new double[series.xValues.length][6];
					boolean ok = true;
					for (int j = 0; j<series.xValues.length; j++)
					{
						String pz = "";
						try {
							pz = ((String[]) series.zValues)[j];
						} catch (Exception exc1) {
							try {
								pz = ""+((double[]) series.zValues)[j];
							} catch (Exception exc2) {
								ok = false;
								break;
							}
						}
						box[j] = new double[] {
								DataSet.getDoubleValueWithoutLimit(series.xValues[j]), 
								DataSet.getDoubleValueWithoutLimit(series.yValues[j]), 
								DataSet.getDoubleValueWithoutLimit(pz), 
								0.0, 0.0, 0.0};
						if (series.dxValues != null) box[j][3] = 2.0 * series.dxValues[j];
						if (series.dyValues != null) box[j][4] = 2.0 * series.dyValues[j];
						if (series.dzValues != null) box[j][5] = 2.0 * series.dzValues[j];
					}
					if (ok) plot.addBoxPlot("", convertedColor, box);
				}
			}
	
			if (!chart_elem.showGridX) plot.getAxis(0).setGridVisible(false);
			if (!chart_elem.showGridY) plot.getAxis(1).setGridVisible(false);
			if (!chart_elem.showGridZ) plot.getAxis(2).setGridVisible(false);
	
			plot.setAxisLabels(new String[] {chart_elem.xLabel, chart_elem.yLabel, chart_elem.zLabel});
			if (chart_elem.xAxisInLogScale) plot.setAxisScale(0, "LOG");
			if (chart_elem.yAxisInLogScale) plot.setAxisScale(1, "LOG");
			if (chart_elem.zAxisInLogScale) plot.setAxisScale(2, "LOG");
			
            // add a title
			if (chart_elem.showTitle) {
				try {
					double[][] data = (double[][]) chart_elem.series[0].zValues;
					double max = -1E99;
					for (int i=0; i<data.length; i++)
					{
						double m = DataSet.getMaximumValue(data[i]);
						if (m > max || max == -1E99) max = m;
					}
		            BaseLabel title = new BaseLabel(chart.title, Color.BLACK, new double[] {0, 0, max * 1.3});
		            title.setFont(new Font("Courier", Font.BOLD, 20));
		            plot.addPlotable(title);
				} catch (Exception exc) {}
			}
			
			// put the PlotPanel in a JFrame like a JPanel
			panel = new JPanel();
			panel.setSize(chart_elem.imageWidth, chart_elem.imageHeight);
			plot.setPreferredSize(new Dimension(chart_elem.imageWidth, chart_elem.imageHeight));
			if (!chart_elem.showToolbar) plot.plotToolBar.setVisible(false);
			panel.add(plot);
			plot.plotCanvas.addMouseListener(this);
			plot.plotCanvas.addMouseWheelListener(this);
			plot.plotCanvas.setEditable(true);
			plot.plotCanvas.setNotable(true);
			plot.plotCanvas.setNoteCoords(true);
		} catch (Exception exc)
		{
			throw new JPARSECException("Error initializing chart.", exc);
		}
	}

	/**
	 * Sets the same relative scale for the 3 axes.
	 * @throws JPARSECException If an error occurs.
	 */
	public void setSameScale() throws JPARSECException {
		double xmax = this.chart_elem.getxMax(), xmin = this.chart_elem.getxMin();
		double ymax = this.chart_elem.getyMax(), ymin = this.chart_elem.getyMin();
		double zmax = this.chart_elem.getzMax(), zmin = this.chart_elem.getzMin();
		
		double dx = xmax - xmin, dy = ymax - ymin, dz = zmax - zmin;
		if (dx > dy && dx > dz) {
			double off = dx / 8;
			if (off == 0) off = 0.5;
			xmax += off;
			xmin -= off;
			dx = xmax - xmin;

			dx /= 2;
			ymax = ymin + dy * 0.5 + dx;
			ymin = ymin + dy * 0.5 - dx;
			zmax = zmin + dz * 0.5 + dx;
			zmin = zmin + dz * 0.5 - dx;
			plot.plotCanvas.setFixedBounds(0, xmin, xmax);
			plot.plotCanvas.setFixedBounds(1, ymin, ymax);
			plot.plotCanvas.setFixedBounds(2, zmin, zmax);
			return;
		}
		if (dy > dx && dy > dz) {
			double off = dy / 8;
			if (off == 0) off = 0.5;
			ymax += off;
			ymin -= off;
			dy = ymax - ymin;

			dy /= 2;
			xmax = xmin + dx * 0.5 + dy;
			xmin = xmin + dx * 0.5 - dy;
			zmax = zmin + dz * 0.5 + dy;
			zmin = zmin + dz * 0.5 - dy;
			plot.plotCanvas.setFixedBounds(0, xmin, xmax);
			plot.plotCanvas.setFixedBounds(1, ymin, ymax);
			plot.plotCanvas.setFixedBounds(2, zmin, zmax);
			return;
		}
		if (dz > dy && dz > dx) {
			double off = dz / 8;
			if (off == 0) off = 0.5;
			zmax += off;
			zmin -= off;
			dz = zmax - zmin;

			dz /= 2;
			ymax = ymin + dy * 0.5 + dz;
			ymin = ymin + dy * 0.5 - dz;
			xmax = xmin + dx * 0.5 + dz;
			xmin = xmin + dx * 0.5 - dz;
			plot.plotCanvas.setFixedBounds(1, ymin, ymax);
			plot.plotCanvas.setFixedBounds(0, xmin, xmax);
			plot.plotCanvas.setFixedBounds(2, zmin, zmax);
			return;
		}
	}
	
	/**
	 * To make the chart visible.
	 * @param width Width.
	 * @param height Height.
	 * @return The frame if you prefer to set 
	 * a different size or value for other property.
	 */
	public JFrame showChart(int width, int height)
	{
		JFrame frame = new JFrame(TextLabel.getSimplifiedString(chart_elem.title));
		panel.setPreferredSize(new Dimension(width, height));
		frame.setSize(width, height);
		frame.setContentPane(this.panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			frame.setIconImage(this.chartAsBufferedImage());
		} catch (JPARSECException e) {
		}
		frame.setVisible(true);
		return frame;
	}
	
	/**
	 * Returns the chart object of this instance.
	 * @return Chart.
	 */
	public ChartElement3D getChartElement()
	{
		return this.chart_elem;
	}
	
	private void writeObject(ObjectOutputStream out)
	throws IOException {
		out.writeObject(this.chart_elem);
	}
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException {
		ChartElement3D chart_elem = (ChartElement3D) in.readObject();
		try {
			init(chart_elem);
		} catch (Exception exc) {}
 	}

	

	
	/**
	 * Exports the chart as a SVG file.
	 * 
	 * @param file_name File name without extension.
	 * @throws JPARSECException If an error occurs.
	 */	
	public void chartAsSVGFile(String file_name) throws JPARSECException
	{
		int ext = file_name.toLowerCase().lastIndexOf(".svg");
		if (ext > 0) file_name = file_name.substring(0, ext);

		File plotFile = new File(file_name+".svg");

		final Dimension size = new Dimension(chart_elem.imageWidth, chart_elem.imageHeight);
		try
		{
			// Using reflection so that everything will work without freehep in classpath
			Class c = Class.forName("org.freehep.graphicsio.svg.SVGGraphics2D");
			Constructor cc = c.getConstructor(new Class[] {plotFile.getClass(), size.getClass()});
			Object svgGraphics = cc.newInstance(new Object[] {plotFile, size});
			Method m = c.getMethod("startExport", null);
			m.invoke(svgGraphics, null);
			this.paintChart((Graphics2D) svgGraphics);
			Method mm = c.getMethod("endExport", null);
			mm.invoke(svgGraphics, null);

		} catch (Exception e)
		{
			throw new JPARSECException("cannot write to file.", e);
		}
	}
	
	/**
	 * Exports the chart as an EPS file.
	 * 
	 * @param file_name File name without extension.
	 * @throws JPARSECException If an error occurs.
	 */
	public void chartAsEPSFile(String file_name) throws JPARSECException
	{
		int ext = file_name.toLowerCase().lastIndexOf(".eps");
		if (ext > 0) file_name = file_name.substring(0, ext);

		File plotFile = new File(file_name+".eps");

		final Dimension size = new Dimension(chart_elem.imageWidth, chart_elem.imageHeight);
		try
		{
			// Using reflection so that everything will work without freehep in classpath
			Class c = Class.forName("org.freehep.graphicsio.ps.PSGraphics2D");
			Constructor cc = c.getConstructor(new Class[] {plotFile.getClass(), size.getClass()});
			Object psGraphics = cc.newInstance(new Object[] {plotFile, size});
			Method m = c.getMethod("startExport", null);
			m.invoke(psGraphics, null);
			this.paintChart((Graphics2D) psGraphics);
			Method mm = c.getMethod("endExport", null);
			mm.invoke(psGraphics, null);

		} catch (Exception e)
		{
			throw new JPARSECException("cannot write to file.", e);
		}
	}

	/**
	 * Exports the chart as a PDF file.
	 * 
	 * @param file_name File name without extension.
	 * @throws JPARSECException If an error occurs.
	 */
	public void chartAsPDFFile(String file_name) throws JPARSECException
	{
		int ext = file_name.toLowerCase().lastIndexOf(".pdf");
		if (ext > 0) file_name = file_name.substring(0, ext);

		File plotFile = new File(file_name+".pdf");

		final Dimension size = new Dimension(chart_elem.imageWidth, chart_elem.imageHeight);
		try
		{
			// Using reflection so that everything will work without freehep in classpath
			Class c = Class.forName("org.freehep.graphicsio.pdf.PDFGraphics2D");
			Constructor cc = c.getConstructor(new Class[] {plotFile.getClass(), size.getClass()});
			Object pdfGraphics = cc.newInstance(new Object[] {plotFile, size});
			Method m = c.getMethod("startExport", null);
			m.invoke(pdfGraphics, null);
			this.paintChart((Graphics2D) pdfGraphics);
			Method mm = c.getMethod("endExport", null);
			mm.invoke(pdfGraphics, null);

		} catch (Exception e)
		{
			throw new JPARSECException("cannot write to file.", e);
		}
	}

	
	/**
	 * Exports the chart as an PNG file.
	 * 
	 * @param file_name File name without extension.
	 * @throws JPARSECException If an error occurs.
	 */
	public void chartAsPNGFile(String file_name)
	throws JPARSECException {
		int ext = file_name.toLowerCase().lastIndexOf(".png");
		if (ext > 0) file_name = file_name.substring(0, ext);
		file_name+=".png";

		Picture p = new Picture(this.chartAsBufferedImage());
		p.write(file_name);
	}

	/**
	 * Draws the current chart to a Graphics device.
	 * @param g Graphics object.
	 * @throws JPARSECException If an error occurs.
	 */
	public void paintChart(Graphics g)
	throws JPARSECException {
		plot.plotCanvas.setSize(chart_elem.imageWidth, chart_elem.imageHeight);
		plot.plotCanvas.paint(g);
	}
	
	/**
	 * Returns a BufferedImage instance with the current chart, adequate to
	 * write an image to disk.
	 * @return The image.
	 * @throws JPARSECException Thrown if the method fails.
	 */
	public BufferedImage chartAsBufferedImage() throws JPARSECException
	{
		BufferedImage buf = new BufferedImage(this.chart_elem.imageWidth,
				this.chart_elem.imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = buf.createGraphics();
		this.paintChart(g);
		return buf;
	}

	/**
	 * Returns the intensity at certain position using 2d spline interpolation.
	 * @param x X position in physical units.
	 * @param y Y position in physical units.
	 * @param legend The legend of the series, must be a surface.
	 * @return The intensity.
	 * @throws JPARSECException If an error occurs.
	 */
	public double getIntensityAt(double x, double y, String legend)
	throws JPARSECException 
	{
		int index = -1;
		if (legend == null && chart_elem.series.length == 1) {
			index = 0;
		} else {
			for (int i=0; i<this.chart_elem.series.length; i++)
			{
				if (this.chart_elem.series[i].legend.equals(legend)) {
					index = i;
				}
			}
		}
		if (index < 0) throw new JPARSECException("series "+legend+" not found.");
		
		GridChartElement gridChart;
		try {
		gridChart = new GridChartElement("", "", "", "", 
				GridChartElement.COLOR_MODEL.BLACK_TO_WHITE,
				GridChartElement.getLimitsFromDataSet(
						DataSet.getDoubleValuesExcludingLimits(this.chart_elem.series[index].xValues), 
						DataSet.getDoubleValuesExcludingLimits(this.chart_elem.series[index].yValues)), 
						(double[][]) this.chart_elem.series[index].zValues,
				null, 600
				);
		} catch (Exception exc) {
			throw new JPARSECException("error while parsing the series to a surface. Is indeed a surface?", exc);
		}

		ImageSplineTransform t = new ImageSplineTransform(GridChartElement.ObjectToDoubleArray(gridChart.data));
		int pointsX = gridChart.data.length;
		int pointsY = gridChart.data[0].length;			
		double px = (x - gridChart.limits[0]) * ((double) (pointsX - 1.0)) / (gridChart.limits[1] - gridChart.limits[0]);
		double py = (y - gridChart.limits[2]) * ((double) (pointsY - 1.0)) / (gridChart.limits[3] - gridChart.limits[2]);
		double data = t.interpolate(px, py);

		return data;
	}

	/**
	 * Returns the chart object.
	 * @return Chart object.
	 */
	public ChartElement3D getChart()
	{
		return chart_elem;
	}

	/**
	 * Returns the plot object.
	 * @return Plot object.
	 */
	public Plot3DPanel getPlot()
	{
		return this.plot;
	}

	/**
	 * Zoom operation.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		plot.plotCanvas.ActionMode = PlotCanvas.ZOOM;		
	}

	/**
	 * Nothing.
	 */
	public void mouseClicked(MouseEvent arg0) {
	}

	/**
	 * Nothing.
	 */
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * Nothing.
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Rotation and translation operations. Translation is done by
	 * moving the mouse with button 3 down, and rotation is the
	 * same but with button 1.
	 */
	public void mousePressed(MouseEvent e) {
		int b = e.getButton();
		if (b == MouseEvent.BUTTON1) 
			plot.plotCanvas.ActionMode = Plot3DCanvas.ROTATION;
		if (b == MouseEvent.BUTTON3)
			plot.plotCanvas.ActionMode = PlotCanvas.TRANSLATION;
	}

	/**
	 * Nothing.
	 */
	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * Updates the panel.
	 */
	public void update() {
		Dimension d = panel.getSize();
		this.chart_elem.imageWidth = d.width;
		this.chart_elem.imageHeight = d.height;
		plot.setPreferredSize(d);
    	panel.revalidate();
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	panel.repaint();
	        }
		});
	}
	
	/**
	 * Test program.
	 * @param args Not used.
	 */
	public static void main(String args[])
	{
		System.out.println("CreateChart3D test");

		try {
			TelescopeElement telescope = TelescopeElement.NEWTON_20cm;
			int field = 3;
			double data[][] = Difraction.pattern(telescope, field);
			
			GridChartElement gridChart = new GridChartElement("Difraction pattern",
					"offsetX", "offsetY", "RelativeIntensity", GridChartElement.COLOR_MODEL.RED_TO_BLUE, 
					new double[] {-field, field, -field, field}, data, 
					new double[] {0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0}, 400);
			//gridChart.resample(data.length*5, data[0].length*5);

			ChartSeriesElement3D series = new ChartSeriesElement3D(gridChart);
			//GridChartElement.createData(-field, field, 1.0 / 5.0),
//					GridChartElement.createData(-field, field, 1.0 / 5.0), data, "Relative intensity (|@PSI|^{2})");
			series.color = Color.RED;

/*			// This is only to test GridChartElement.getDataFromDataSet
			DoubleVector stupidData[] = new DoubleVector[] {
					new DoubleVector(1-2, 1, 0E-10),
					new DoubleVector(2-2, 1, 1E-10),
					new DoubleVector(3-2, 1, 0E-10),
					new DoubleVector(1-2, 2, 1E-10),
					new DoubleVector(2-2, 2, 4E-10),
					new DoubleVector(3-2, 2, 1E-10),
					new DoubleVector(1-2, 3, 0E-10),
					new DoubleVector(2-2, 3, 1E-10),
					new DoubleVector(3-2, 3, 0E-10)
			};
			double x[] = DoubleVector.getAGivenComponent(stupidData, 0);
			double y[] = DoubleVector.getAGivenComponent(stupidData, 1);
			double z[] = DoubleVector.getAGivenComponent(stupidData, 2);
			gridChart.data = GridChartElement.getDataFromDataSet(x, y, z);
			gridChart.limits = GridChartElement.getLimitsFromDataSet(x, y);
			series = new ChartSeriesElement3D(gridChart);
			series.color = Color.RED;
*/
			
/*			series = ChartSeriesElement3D.getSurfaceFromPoints(new DoubleVector[] {
					new DoubleVector(1-2, 1, 0E-10),
					new DoubleVector(2-2, 1, 1E-10),
					new DoubleVector(3-2, 1, 0E-10),
					new DoubleVector(1-2, 2, 1E-10),
					new DoubleVector(2-2, 2, 4E-10),
					new DoubleVector(3-2, 2, 1E-10),
					new DoubleVector(1-2, 3, 0E-10),
					new DoubleVector(2-2, 3, 1E-10),
					new DoubleVector(3-2, 3, 0E-10)
			}, 20);
*/
/*			series = new ChartSeriesElement3D(
					GridChartElement.createData(-field, field, field / 2.0),
					GridChartElement.createData(-field, field, field / 2.0), 
					GridChartElement.createData(-field*10, field*10, field*10 / 2.0), "Relative intensity (|@PSI|^{2})");
			series.zValues = new String[] {"<"+series.xValues[0], ">"+series.xValues[1], 
					series.xValues[2],  ">"+series.xValues[3], "<"+series.xValues[4]};
			series.dxValues = new double[] {1, 2, 0, 2, 1};
			series.dyValues = new double[] {1, 2, 0, 2, 1};
			series.dzValues = new double[] {1, 2, 0, 2, 1};
*/
			
			ChartElement3D chart = new ChartElement3D(new ChartSeriesElement3D[] {series}, 
					"Difraction pattern", "@DELTAx (\")", "@DELTAy (\")", "@SIZE20I_{relative} (|@PSI|^{2})");
			chart.showToolbar = false;
			chart.showLegend = false;
			chart.showTitle = false;
			//chart.showGridX = chart.showGridY = chart.showGridZ = false;
			CreateChart3D c = new CreateChart3D(chart);
			c.showChart(500, 500);
			
//			Serialization.writeObject(chart, "/home/alonso/chart3dTest");
//			Serialization.writeObject(c, "/home/alonso/createChart3dTest");
			System.out.println(c.getIntensityAt(2, 2, series.legend));
			System.out.println(c.getIntensityAt(0, 0, series.legend));
		} catch (JPARSECException e)
		{
			e.showException();
		}
	}
}
