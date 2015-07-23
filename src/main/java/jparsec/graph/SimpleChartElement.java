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

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;

import jparsec.graph.ChartElement.SUBTYPE;
import jparsec.graph.ChartElement.TYPE;
import jparsec.util.JPARSECException;

/**
 * Creates a simple chart element for JFreeChart.<P>
 * 
 * In this object the labels for x and y axes, as well as the title can be
 * encoded following the instructions given in {@linkplain TextLabel} class. This
 * provides some possibilities like to dynamically change color/size, include superscript
 * or subscript text, or Greek letters.
 * 
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SimpleChartElement implements Serializable
{
	static final long serialVersionUID = 1L;

	/**
	 * Default empty constructor.
	 */
	public SimpleChartElement() {	}

	/**
	 * Constructor adequate for a simple scatter chart with a default image size of 600x400
	 * pixels.
	 * 
	 * @param x X values.
	 * @param y Y values.
	 * @param title Title.
	 * @param x_label Label x axis.
	 * @param y_label Label y axis.
	 * @param legend Legend. Set to null to hide it.
	 */
	public SimpleChartElement(double x[], double y[], String title, String x_label,
			String y_label, String legend)
	{
		this.chartType = TYPE.XY_CHART;
		this.subType = SUBTYPE.XY_SCATTER;
		
		this.xValues = x;
		this.yValues = y;
		this.xLabel = x_label;
		this.yLabel = y_label;
		this.legend = legend;
		this.title = title;
		this.showLegend = true;
		if (legend == null) showLegend = false;
		this.changeOrientationToHorizontal = false;
		this.imageWidth = 600;
		this.imageHeight = 400;
		this.xValuesForPieAndCategoryCharts = DataSet.toStringValues(x);
	}

	/**
	 * Constructor adequate for an x-y chart with a default image size of 600x400
	 * pixels.
	 * 
	 * @param charttype Type.
	 * @param chartsubtype Subtype.
	 * @param x X values.
	 * @param y Y values.
	 * @param title Title.
	 * @param x_label Label x axis.
	 * @param y_label Label y axis.
	 * @param legend Legend.
	 * @param show_legend True to show legend.
	 * @param change_orientation True to change orientation.
	 * @throws JPARSECException If the chart type and subtype are incompatible.
	 */
	public SimpleChartElement(ChartElement.TYPE charttype, ChartElement.SUBTYPE chartsubtype, double x[], double y[], String title, String x_label,
			String y_label, String legend, boolean show_legend, boolean change_orientation) throws JPARSECException
	{
		this.chartType = charttype;
		this.subType = chartsubtype;
		
		if ((chartType == TYPE.XY_CHART && subType.name().indexOf("XY") < 0) ||
				(chartType == TYPE.CATEGORY_CHART && subType.name().indexOf("CATEGORY") < 0) ||
				(chartType == TYPE.PIE_CHART && subType.name().indexOf("PIE") < 0))
				throw new JPARSECException("Subtype "+subType.name()+" is not acceptable for chart type "+chartType.name());
		this.xValues = x;
		this.yValues = y;
		this.xLabel = x_label;
		this.yLabel = y_label;
		this.legend = legend;
		this.title = title;
		this.showLegend = show_legend;
		this.changeOrientationToHorizontal = change_orientation;
		this.imageWidth = 600;
		this.imageHeight = 400;
		this.xValuesForPieAndCategoryCharts = DataSet.toStringValues(x);
	}

	/**
	 * Constructor adequate for an x-y chart.
	 * 
	 * @param charttype Type.
	 * @param chartsubtype Subtype.
	 * @param x X values.
	 * @param y Y values.
	 * @param title Title.
	 * @param x_label Label x axis.
	 * @param y_label Label y axis.
	 * @param legend Legend.
	 * @param show_legend True to show legend.
	 * @param change_orientation True to change orientation.
	 * @param size_x Width.
	 * @param size_y Height.
	 * @throws JPARSECException If the chart type and subtype are incompatible.
	 */
	public SimpleChartElement(ChartElement.TYPE charttype, ChartElement.SUBTYPE chartsubtype, double x[], double y[], String title, String x_label,
			String y_label, String legend, boolean show_legend, boolean change_orientation, int size_x, int size_y) throws JPARSECException
	{
		this.chartType = charttype;
		this.subType = chartsubtype;
		if ((chartType == TYPE.XY_CHART && subType.name().indexOf("XY") < 0) ||
				(chartType == TYPE.CATEGORY_CHART && subType.name().indexOf("CATEGORY") < 0) ||
				(chartType == TYPE.PIE_CHART && subType.name().indexOf("PIE") < 0))
				throw new JPARSECException("Subtype "+subType.name()+" is not acceptable for chart type "+chartType.name());
		this.xValues = x;
		this.yValues = y;
		this.xLabel = x_label;
		this.yLabel = y_label;
		this.legend = legend;
		this.title = title;
		this.showLegend = show_legend;
		this.changeOrientationToHorizontal = change_orientation;
		this.imageWidth = size_x;
		this.imageHeight = size_y;
		this.xValuesForPieAndCategoryCharts = DataSet.toStringValues(x);
	}

	/**
	 * Holds the chart type.
	 */
	public ChartElement.TYPE chartType;

	/**
	 * Array of x values.
	 */
	public double[] xValues;

	/**
	 * Array of x values, only for pie charts.
	 */
	public String[] xValuesForPieAndCategoryCharts;

	/**
	 * Array of y values.
	 */
	public double[] yValues;

	/**
	 * Label for x axis. Ignored in pie charts.
	 */
	public String xLabel;

	/**
	 * Label for y axis. Ignored in pie charts.
	 */
	public String yLabel;

	/**
	 * Title.
	 */
	public String title;

	/**
	 * Legend.
	 */
	public String legend;

	/**
	 * True to show a legend. In pie charts is ignored, legend is always shown.
	 */
	public boolean showLegend;

	/**
	 * Change chart orientation to vertical. Ignored in pie charts.
	 */
	public boolean changeOrientationToHorizontal;

	/**
	 * The chart subtype.
	 */
	public ChartElement.SUBTYPE subType;

	/**
	 * X size of the chart in pixels.
	 */
	public int imageWidth;

	/**
	 * Y size of the chart in pixels.
	 */
	public int imageHeight;

	/**
	 * Show x axis in log scale.
	 */
	public boolean xAxisInLogScale;

	/**
	 * Show y axis in log scale.
	 */
	public boolean yAxisInLogScale;

	/**
	 * Selects the color/gradient of the background. It is recommended
	 * to use just a color instead a gradient for better compatibility
	 * with output libraries (eps, pdf, svg formats).
	 */
	public Paint backgroundGradient = Color.WHITE;

	/**
	 * To clone the object.
	 */
	public SimpleChartElement clone()
	{
		if (this == null) return null;
		SimpleChartElement s = null;
		try { 
			s = new SimpleChartElement(this.chartType, this.subType, this.xValues.clone(), this.yValues.clone(),
				this.title, this.xLabel, this.yLabel, this.legend, this.showLegend,
				this.changeOrientationToHorizontal);
		} catch (Exception exc) { }
		s.backgroundGradient = this.backgroundGradient;
		s.imageWidth = this.imageWidth;
		s.imageHeight = this.imageHeight;
		s.xAxisInLogScale = this.xAxisInLogScale;
		s.yAxisInLogScale = this.yAxisInLogScale;
		s.xValuesForPieAndCategoryCharts = this.xValuesForPieAndCategoryCharts.clone();
		return s;
	}
	/**
	 * Returns true if the input object is equals to this chart object.
	 */
	public boolean equals(Object c)
	{
		if (c == null) {
			if (this == null) return true;
			return false;
		}
		if (this == null) {
			return false;
		}
		SimpleChartElement chart = (SimpleChartElement) c;
		boolean equals = true;
		if (this.changeOrientationToHorizontal != chart.changeOrientationToHorizontal) equals = false;
		if (this.xAxisInLogScale != chart.xAxisInLogScale) equals = false;
		if (this.yAxisInLogScale != chart.yAxisInLogScale) equals = false;
		if (this.showLegend != chart.showLegend) equals = false;
		if (this.imageWidth != chart.imageWidth) equals = false;
		if (this.imageHeight != chart.imageHeight) equals = false;
		if (this.chartType != chart.chartType) equals = false;
		if (this.subType != chart.subType) equals = false;
		if (!this.legend.equals(chart.legend)) equals = false;
		if (!this.xLabel.equals(chart.xLabel)) equals = false;
		if (!this.yLabel.equals(chart.yLabel)) equals = false;
		if (!this.title.equals(chart.title)) equals = false;
		
		if (this.backgroundGradient.hashCode() != chart.backgroundGradient.hashCode()) equals = false;

		if (this.xValues.length == chart.xValues.length)
		{
			for (int i=0; i<this.xValues.length; i++)
			{
				if (this.xValues[i] != chart.xValues[i]) equals = false;
			}
		} else {
			equals = false;
		}
		if (this.yValues.length == chart.yValues.length)
		{
			for (int i=0; i<this.yValues.length; i++)
			{
				if (this.yValues[i] != chart.yValues[i]) equals = false;
			}
		} else {
			equals = false;
		}
		if (this.xValuesForPieAndCategoryCharts.length == chart.xValuesForPieAndCategoryCharts.length)
		{
			for (int i=0; i<this.xValuesForPieAndCategoryCharts.length; i++)
			{
				if (this.xValuesForPieAndCategoryCharts[i] != chart.xValuesForPieAndCategoryCharts[i]) equals = false;
			}
		} else {
			equals = false;
		}
		return equals;
	}
}
