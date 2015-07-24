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
package jparsec.graph.chartRendering.frame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JComponent;
import jparsec.ephem.EphemerisElement;
import jparsec.graph.SkyChart;
import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;
import jparsec.graph.chartRendering.SatelliteRenderElement.PLANET_MAP;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;

/**
 * Support class to use Swing/AWT graph components for
 * eclipse rendering.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 * @see SkyChart
 */
public class EclipseRendering implements Serializable
{
	private Frame frame;
	private RenderEclipseMap renderEclipse;

	TimeElement time;
	ObserverElement obs;
	EphemerisElement eph;
	boolean solar;
	int width, height;
	ANAGLYPH_COLOR_MODE colorMode = ANAGLYPH_COLOR_MODE.NO_ANAGLYPH;
	PLANET_MAP map = PLANET_MAP.NO_MAP;

	/**
	 * Sample render constructor.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param solar True for a solar eclipse, false for a lunar one.
	 * @param map The map to use to show the eclipse.
	 * @param w The width in pixels of the chart.
	 * @param h The height in pixels of the chart.
	 * @param colorMode The color mode in case anaglyph output is required.
	 * @param title Window title.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public EclipseRendering(TimeElement time, ObserverElement obs, EphemerisElement eph, boolean solar,
			PLANET_MAP map, int w, int h, ANAGLYPH_COLOR_MODE colorMode, String title)
			throws JPARSECException
	{
		this.time = time.clone();
		this.obs = obs.clone();
		this.eph = eph.clone();
		this.solar = solar;
		if (map != null) this.map = map;
		this.width = w;
		this.height = h;
		if (colorMode != null) this.colorMode = colorMode;
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame(title);
		double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		TimeElement newTime = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		renderEclipse = new RenderEclipseMap(newTime, obs, eph, this);
	}

	/**
	 * To show the rendering.
	 */
	public void showRendering()
	{
		this.showRendering(false);
	}

	/**
	 * To show the rendering.
	 * @param undecorated True to show the Frame without decoration.
	 */
	public void showRendering(boolean undecorated)
	{
		if (frame != null) {
			String t = frame.getTitle();
			frame = new Frame(t);
		}
		frame.setUndecorated(undecorated);

		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				frame.dispose();
			}
		});
		int fac = 1;
		if (colorMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		frame.setSize(width*fac, height);

		final Panel renderGraphPanel = new Panel(new BorderLayout());
		renderGraphPanel.add(renderEclipse, BorderLayout.CENTER);

		frame.add(renderGraphPanel);

		frame.setVisible(true);
	}

	/**
	 * Draws the current chart to a Graphics device.
	 * @param g Graphics object.
	 * @throws JPARSECException If an error occurs.
	 */
	public void paintChart(Graphics2D g)
	throws JPARSECException {
		try {
			renderEclipse.renderize(g);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JPARSECException(e);
		}
	}

	/**
	 * Returns a BufferedImage instance with the current rendering, adequate to
	 * write an image to disk.
	 * @return The image.
	 * @throws JPARSECException Thrown if the method fails.
	 */
	public BufferedImage createBufferedImage() throws JPARSECException
	{
		int fac = 1;
		if (colorMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		BufferedImage buf = new BufferedImage(width*fac, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buf.createGraphics();
		this.paintChart(g);
		g.dispose();
		return buf;
	}

	// Define ID version of the class.
	static final long serialVersionUID = 1L;


	/**
	 * Returns the render panel.
	 * @return The panel.
	 */
	public JComponent getPanel() {
		return this.renderEclipse;
	}

	/**
	 * Returns render object.s
	 * @return Render object.
	 */
	public jparsec.graph.chartRendering.RenderEclipse getRenderEclipseObject()
	{
		return this.renderEclipse.getRenderEclipse();
	}

	/**
	 * Returns time object.
	 * @return Time object.
	 */
	public TimeElement getTimeObject()
	{
		return this.time;
	}
	/**
	 * Returns observer object.
	 * @return Observer object.
	 */
	public ObserverElement getObserverObject()
	{
		return this.obs;
	}
	/**
	 * Returns ephemeris object.
	 * @return Ephemeris object.
	 */
	public EphemerisElement getEphemerisObject()
	{
		return this.eph;
	}

	/**
	 * Writes the object to a binary file.
	 * @param out Output stream.
	 * @throws IOException If an error occurs.
	 */
	private void writeObject(ObjectOutputStream out)
	throws IOException {
		out.writeObject(this.getTimeObject());
		out.writeObject(this.getObserverObject());
		out.writeObject(this.getEphemerisObject());
		out.writeObject(this.map);
		out.writeObject(this.colorMode);
		out.writeBoolean(solar);
		out.writeInt(width);
		out.writeInt(height);
		out.writeObject(frame.getTitle());
	}
	/**
	 * Reads the object.
	 * @param in Input stream.
	 */
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException {
		time = (TimeElement) in.readObject();
		obs = (ObserverElement) in.readObject();
		eph = (EphemerisElement) in.readObject();
		map = (PLANET_MAP) in.readObject();
		colorMode = (ANAGLYPH_COLOR_MODE) in.readObject();
		solar = in.readBoolean();
		width = in.readInt();
		height = in.readInt();
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame((String) in.readObject());
		try {
			if (time.astroDate == null) time.astroDate = new AstroDate();
			double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			TimeElement newTime = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			renderEclipse = new RenderEclipseMap(newTime, obs, eph, this);
		} catch (JPARSECException e) {
			Logger.log(LEVEL.ERROR, "JPARSEC exception: "+e.getMessage());
		}
 	}
}

