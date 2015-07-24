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
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jparsec.ephem.EphemerisElement;
import jparsec.graph.SkyChart;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE;
import jparsec.graph.chartRendering.SkyRenderElement;
import jparsec.observer.ObserverElement;
import jparsec.time.AstroDate;
import jparsec.time.TimeElement;
import jparsec.time.TimeElement.SCALE;
import jparsec.time.TimeScale;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;
import jparsec.util.Logger.LEVEL;

/**
 * Support class to use Swing/AWT graph components for sky
 * and planetary rendering. See also {@linkplain SkyChart} class.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 * @see SkyChart
 */
public class SkyRendering implements Serializable
{
	private Frame frame;
	private RenderSky renderSky;
	private TimeElement time;
	private ObserverElement obs;
	private EphemerisElement eph;

	/**
	 * Sample render program.
	 *
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param render SkyRender object.
	 * @param title Window title.
	 * @param graphMarginY Margin in y axis. Set to 0 or negative for an automatic value.
	 * @throws JPARSECException If an error occurs.
	 */
	public SkyRendering(TimeElement time, ObserverElement obs, EphemerisElement eph, SkyRenderElement render,
			String title, int graphMarginY)
	throws JPARSECException {
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame(title);
		//double jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		//TimeElement newTime = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
		renderSky = new RenderSky(time, obs, eph, render);
		if (graphMarginY > 0) renderSky.getRenderSky().setYCenterOffset(graphMarginY);
		this.time = time.clone();
		this.obs = obs.clone();
		this.eph = eph.clone();
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
			@Override
			public void windowClosing(WindowEvent evt)
			{
				frame.dispose();
			}
		});

/*		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;

		if (renderSky.getRenderSky().render.width > w)
			renderSky.getRenderSky().render.width = w;
		if (renderSky.getRenderSky().render.height > h)
			renderSky.getRenderSky().render.height = h;
*/
		frame.setBackground(Color.WHITE);
		int fac = 1;
		if (renderSky.getRenderSky().render.anaglyphMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		frame.setSize(renderSky.getRenderSky().render.width*fac, renderSky.getRenderSky().render.height);

		//final Panel
		final Panel renderGraphPanel = new Panel(new BorderLayout());
		renderGraphPanel.add(renderSky, BorderLayout.CENTER);

		frame.add(renderGraphPanel);

		frame.setVisible(true);
	}

	/**
	 * Updates the sky, time and/or observer objects for the rendering.
	 * @param sky A new sky object, or null to keep the current one.
	 * @param time A new time object, or null to keep the current one.
	 * @param obs A new observer, or null to keep the current one.
	 * @param eph A new ephemeris object, or null to keep the current one.
	 * @throws JPARSECException
	 */
	public void update(SkyRenderElement sky, TimeElement time, ObserverElement obs, EphemerisElement eph) throws JPARSECException {
		if (time != null || obs != null || eph != null) {
			if (time != null) this.time = time;
			if (obs != null) this.obs = obs;
			if (eph != null) this.eph = eph;
			renderSky.getRenderSky().setSkyRenderElement(
				sky == null ? renderSky.getRenderSky().render : sky, this.time, this.obs, this.eph);
		} else {
			renderSky.getRenderSky().setSkyRenderElement(sky == null ? renderSky.getRenderSky().render : sky);
		}
	}

	/**
	 * Resets the graphics object used internally to render the sky.
	 */
	public void resetGraphics() {
		renderSky.gj = null;
	}

	/**
	 * Draws the current chart to a Graphics device.
	 * @param g Graphics object.
	 * @throws JPARSECException If an error occurs.
	 */
	public void paintChart(Graphics2D g)
	throws JPARSECException {
		renderSky.renderize(g);
	}

	/**
	 * Draws the current chart to an image supplied. This method
	 * has better performance compared to the one using a Graphics2D
	 * object.
	 * @param image The image.
	 * @throws JPARSECException If an error occurs.
	 */
	public void paintChart(BufferedImage image)
	throws JPARSECException {
		renderSky.renderize(image);
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
		if (renderSky.getRenderSky().render.anaglyphMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		BufferedImage buf = new BufferedImage(renderSky.getRenderSky().render.width*fac, renderSky.getRenderSky().render.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buf.createGraphics();
		this.paintChart(g);
		g.dispose();
		return buf;
	}

	/**
	 * Returns a BufferedImage instance with the current rendering, adequate to
	 * write an image to disk. The returned image has a transparent (alpha) channel.
	 * To use transparency in sky rendering you should also call {@link AWTGraphics#setBufferedImageType(int)}.
	 * @return The image.
	 * @throws JPARSECException Thrown if the method fails.
	 */
	public BufferedImage createTransparentBufferedImage() throws JPARSECException
	{
		int fac = 1;
		if (renderSky.getRenderSky().render.anaglyphMode == ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) fac = 2;
		BufferedImage buf = new BufferedImage(renderSky.getRenderSky().render.width*fac, renderSky.getRenderSky().render.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buf.createGraphics();
		this.paintChart(g);
		g.dispose();
		return buf;
	}

	/**
	 * Returns a BufferedImage instance with the last rendering done.
	 * @return The last rendering.
	 * @throws Exception Thrown if the method fails.
	 */
	public BufferedImage getLastRenderedImage() throws Exception
	{
		return renderSky.img;
	}

	/**
	 * Sets the longitude of Jupiter's Great Red Spot.
	 * <P>
	 * Please note that the rendering will use the default value of the
	 * longitude of central meridian for rendering. This value is refered to
	 * System III for giant planets (rotation of the magnetic field), so the
	 * apparent rotation will not match that of the observed equatorial nor
	 * tropical belts in these planets. This function is intended to adjust
	 * specifically the apparent sight of Jupiter's disk.
	 *
	 * @param GRS_lon Observed longitude in radians.
	 * @param system System of coordinates of GRS_lon, 1, 2, or 3. Will be
	 *        ussually 2, since the Great Red Spot is in the tropical belt (1
	 *        refers to equatorial belt, and 3 to the rotation of the magnetic
	 *        field).
	 */
	public void setJupiterGRSLongitude(double GRS_lon, int system)
	{
		renderSky.getRenderSky().setJupiterGRSLongitude(GRS_lon, system);
	}

	// Define ID version of the class.
	static final long serialVersionUID = 1L;


	/**
	 * Sets the background color.
	 * @param background Background color.
	 */
	public void setBackgroundColor(Color background)
	{
		renderSky.getRenderSky().render.background = background.getRGB();
	}

	/**
	 * Returns the render panel.
	 * @return The panel.
	 */
	public JComponent getPanel() {
		return this.renderSky;
	}

	/**
	 * Returns render object.
	 * @return Render object.
	 */
	public jparsec.graph.chartRendering.RenderSky getRenderSkyObject()
	{
		return this.renderSky.getRenderSky();
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
		out.writeInt(renderSky.getRenderSky().getYMargin());
		out.writeObject(this.renderSky.getRenderSky().render);
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
		int graphMarginY = in.readInt();
		SkyRenderElement render = (SkyRenderElement) in.readObject();
		if (!GraphicsEnvironment.isHeadless()) frame = new Frame((String) in.readObject());
		double jd;
		try {
			if (time.astroDate == null) time.astroDate = new AstroDate();
			jd = TimeScale.getJD(time, obs, eph, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			TimeElement newTime = new TimeElement(jd, SCALE.BARYCENTRIC_DYNAMICAL_TIME);
			renderSky = new RenderSky(newTime, obs, eph, render);
			if (graphMarginY > 0) renderSky.getRenderSky().setYMargin(graphMarginY);
		} catch (JPARSECException e) {
			Logger.log(LEVEL.ERROR, "JPARSEC exception: "+e.getMessage());
		}
 	}

	/**
	 * Returns a JFrame with the provided component.
	 * @param w Width in pixels.
	 * @param h Height in pixels.
	 * @param c The component of the JFrame.
	 * @return The JFrame.
	 */
	public static JFrame showJFrameWithComponent(int w, int h, Component c) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//f.setUndecorated(true);
		f.setPreferredSize(new Dimension(w, h));
		f.add(c);
		f.pack();
		f.setVisible(true);
		return f;
	}
}
