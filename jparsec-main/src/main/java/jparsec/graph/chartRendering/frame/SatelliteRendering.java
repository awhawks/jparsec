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
import java.awt.Color;
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
import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;
import jparsec.graph.chartRendering.SatelliteRenderElement;
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
 * satellite rendering.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class SatelliteRendering implements Serializable
{
	private Frame frame;
	private RenderSatellite renderSatellite;
	private TimeElement time;
	private ObserverElement obs;
	private EphemerisElement eph;

	/**
	 * Sample render program.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param render Render object.
	 * @param title Window title.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public SatelliteRendering(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SatelliteRenderElement render, String title)
			throws JPARSECException
	{
		this.time = time.clone();
		this.obs = obs.clone();
		this.eph = eph.clone();
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame(title);
		renderSatellite = new RenderSatellite(time, obs, eph, render);
		renderSatellite.getRenderSatellite().setSatelliteRenderElement(render, time, obs, eph);
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

		frame.setBackground(Color.WHITE);
		int fac = 1;
		if (renderSatellite.getRenderSatellite().render.anaglyphMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		frame.setSize(renderSatellite.getRenderSatellite().render.width*fac, renderSatellite.getRenderSatellite().render.height);
		final Panel renderGraphPanel = new Panel(new BorderLayout());
		renderGraphPanel.add(renderSatellite, BorderLayout.CENTER);
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
		renderSatellite.renderize(g);
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
		if (renderSatellite.getRenderSatellite().render.anaglyphMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		BufferedImage buf = new BufferedImage(renderSatellite.getRenderSatellite().render.width*fac, renderSatellite.getRenderSatellite().render.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buf.createGraphics();
		this.paintChart(g);
		return buf;
	}

	// Define ID version of the class.
	static final long serialVersionUID = 1L;

	/**
	 * Returns the render panel.
	 * @return The panel.
	 */
	public JComponent getPanel() {
		return this.renderSatellite;
	}

	/**
	 * Returns render object.
	 * @return Render object.
	 */
	public jparsec.graph.chartRendering.RenderSatellite getRenderSatelliteObject()
	{
		return this.renderSatellite.getRenderSatellite();
	}

	/**
	 * Adds a satellite to render another one.
	 * @param name The name of the satellite.
	 * @return True if it was added correctly, false otherwise.
	 * @throws JPARSECException If an error occurs.
	 */
	public boolean addSatellite(String name) throws JPARSECException {
		return this.renderSatellite.getRenderSatellite().addSatellite(name);
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
		out.writeObject(this.renderSatellite.getRenderSatellite().render);
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
		SatelliteRenderElement render = (SatelliteRenderElement) in.readObject();
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame((String) in.readObject());
		try {
			if (time.astroDate == null) time.astroDate = new AstroDate();
			double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			TimeElement newTime = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			renderSatellite = new RenderSatellite(newTime, obs, eph, render);
		} catch (JPARSECException e) {
			Logger.log(LEVEL.ERROR, "JPARSEC exception: "+e.getMessage());
		}
 	}
}
