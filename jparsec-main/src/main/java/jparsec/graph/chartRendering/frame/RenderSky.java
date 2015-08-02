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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import jparsec.ephem.EphemerisElement;
import jparsec.graph.DataSet;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.graph.chartRendering.Graphics;
import jparsec.graph.chartRendering.SkyRenderElement;
import jparsec.observer.ObserverElement;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;

class RenderSky extends JComponent
{
	private static final long serialVersionUID = 1L;
	jparsec.graph.chartRendering.RenderSky rp;
	BufferedImage img;

	/**
	 * The constructor.
	 * @throws JPARSECException If an error occurs.
	 */
	public RenderSky(TimeElement time, ObserverElement obs, EphemerisElement eph,
			SkyRenderElement sky) throws JPARSECException {
		super.setDoubleBuffered(false);
		rp = new jparsec.graph.chartRendering.RenderSky(time, obs, eph, sky);
		rp.dateChanged(true);
	}

	/**
	 * Paint the graph.
	 */
	protected void offscreenPaint(Graphics2D g)
	{
		try
		{
			renderize(g);
		} catch (Exception ve)
		{
			Logger.log(Logger.LEVEL.ERROR, "Exception when rendering planet. Message was: " + ve.getLocalizedMessage() + ". Trace: " + JPARSECException.getTrace(ve.getStackTrace()));
		}
	}

	jparsec.graph.chartRendering.Graphics gj = null;

	/**
	 * Renderize the sky.
	 *
	 * @param g Graphics object.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public void renderize(Graphics2D g) throws JPARSECException
	{
		try {
			// Support for vector graphics. FIXME: Not good idea to use sun.java2d.SunGraphics2D, but works on Java 6 & 7
			if (rp.render.anaglyphMode == Graphics.ANAGLYPH_COLOR_MODE.NO_ANAGLYPH && g.getClass().toString().indexOf("sun.java2d.SunGraphics2D") < 0) {
				boolean createImg = true;
				Graphics gj = new AWTGraphics(rp.render.width, rp.render.height,
						rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical, g, createImg);
				rp.renderize(gj);
				if (createImg) {
					img = (BufferedImage) gj.getRendering();
				} else {
					img = null;
				}
			} else {
				if (gj == null) {
					gj = new AWTGraphics(rp.render.width, rp.render.height, rp.render.anaglyphMode,
						rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical);
				} else {
					((AWTGraphics) gj).regenerate(rp.render.width, rp.render.height, rp.render.anaglyphMode,
							rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical);
				}
				rp.renderize(gj);
				img = (BufferedImage) gj.getRendering();
				g.drawImage(img, 0, 0, null);

				// draw image to clipped rectangle for better performance:
				// performance increase is negligible (3%), and this can cause problems when rendering only images
/*				int clip[] = gj.getClip();
				if (clip[0] != 0) {
					int fsx = rp.graphMarginY - 1; // (40*rp.render.drawCoordinateGridFont.getSize())/15;
					int fsy = rp.leyendMargin - 1; //(51*rp.render.drawCoordinateGridFont.getSize())/15;
					int fsy2 = (rp.graphMarginX - 1)/2; //(25*rp.render.drawCoordinateGridFont.getSize())/15;
					if (clip[0] >= fsx) {
						clip[2] += fsx;
						clip[0] -= fsx;
					} else {
						clip[2] += clip[0];
						clip[0] = 0;
					}
					if (clip[1] < fsy) {
						clip[3] += clip[1];
						clip[1] = 0;
					}
					clip[3] += fsy2;
					if (clip[0]+clip[2] > rp.render.width) clip[2] = rp.render.width - clip[0];
					if (clip[1]+clip[3] > rp.render.height) clip[3] = rp.render.height - clip[1];
				}
				if (clip[1] < 0) {
					clip[3] += clip[1];
					clip[1] = 0;
				}
				g.drawImage(img.getSubimage(clip[0], clip[1], clip[2], clip[3]), clip[0], clip[1], null);
*/
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			if (exc instanceof JPARSECException) throw (JPARSECException) exc;
			throw new JPARSECException("Error during rendering. Details: "+ DataSet.stringArrayToString(JPARSECException.toStringArray(exc.getStackTrace())), exc);
		}
	}

	/**
	 * Renderize the sky.
	 *
	 * @param image The image to render to.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public synchronized void renderize(BufferedImage image) throws JPARSECException
	{
		try {
			if (image != null && rp.render.anaglyphMode == Graphics.ANAGLYPH_COLOR_MODE.NO_ANAGLYPH) {
				if (gj == null) {
					gj = new AWTGraphics(rp.render.width, rp.render.height,
							rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical, image);
				} else {
					// Force external graphics for testing
/*					jparsec.graph.chartRendering.Graphics gj = new AWTGraphics(rp.render.width, rp.render.height,
							rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical, image.createGraphics(), true);
					rp.renderize(gj);
					img = (BufferedImage) gj.getRendering();
					image.createGraphics().drawImage(img, 0, 0, null);
					return;
*/
					((AWTGraphics) gj).regenerate(rp.render.width, rp.render.height,
							rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical, image);
				}
				rp.renderize(gj);
				img = image;
				return;
			}

			if (gj == null) {
				gj = new AWTGraphics(rp.render.width, rp.render.height, rp.render.anaglyphMode,
					rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical);
			} else {
				((AWTGraphics) gj).regenerate(rp.render.width, rp.render.height, rp.render.anaglyphMode,
						rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical);
			}
			rp.renderize(gj);
			img = (BufferedImage) gj.getRendering();
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  		  	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  		  	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
  		  	g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.drawImage(img, 0, 0, null);
		} catch (Exception exc) {
			exc.printStackTrace();
			if (exc instanceof JPARSECException) throw (JPARSECException) exc;
			throw new JPARSECException("Error during rendering. Details: "+exc.getLocalizedMessage(), exc);
		}
	}

	/**
	 * Returns the {@linkplain RenderSky} object.
	 * @return The {@linkplain RenderSky} object
	 */
	public jparsec.graph.chartRendering.RenderSky getRenderSky() {
		return rp;
	}

	/**
	 * The buffer image
	 */
	public Image buffer = null;
	private boolean doRedraw = true;

	/**
	 * Paints the canvas using double buffering.
	 *
	 * @see #offscreenPaint
	 */
	@Override
	public final void paintComponent(java.awt.Graphics g)
	{
		Insets insets = getInsets();
		int sizew = rp.render.width;
		if (rp.render.anaglyphMode == Graphics.ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) sizew *= 2;
		if (doRedraw)
		{
			doRedraw = false;
			final int width = sizew; //getWidth() - insets.left - insets.right;
			final int height = rp.render.height; //getHeight() - insets.top - insets.bottom;
			buffer = createImage(width, height);
			if (buffer == null)
				return;
			final Graphics2D graphics = (Graphics2D) buffer.getGraphics();
			/* save original color */
			Color oldColor = graphics.getColor();
			graphics.setColor(getBackground());
			graphics.fillRect(insets.left, insets.top, width, height);
			/* restore original color */
			graphics.setColor(oldColor);
			offscreenPaint(graphics);
		}
		g.drawImage(buffer, insets.left, insets.top, null);

		// Fix component's background in case its size is greater than the rendering
		Color c = g.getColor();
		g.setColor(new Color(rp.render.background));
		if (getWidth() > Math.min(sizew, buffer.getWidth(null))) g.fillRect(Math.min(sizew, buffer.getWidth(null))+insets.left, insets.top, getWidth(), getHeight());
		if (getHeight() > buffer.getHeight(null)) g.fillRect(0, buffer.getHeight(null)+insets.top, getWidth(), getHeight());
		g.setColor(c);
	}

	/**
	 * Updates the canvas.
	 */
	public final void update(Graphics2D g)
	{
		paint(g);
	}

	/**
	 * Prints the canvas.
	 */
	public final void printComponent(Graphics2D g)
	{
		offscreenPaint(g);
	}

	/**
	 * Double buffering cannot be controlled for this component. This method
	 * always throws an exception.
	 */
	@Override
	public final void setDoubleBuffered(boolean flag)
	{
		throw new IllegalArgumentException();
	}

	/**
	 * Double buffering is always enabled.
	 *
	 * @return true.
	 */
	@Override
	public final boolean isDoubleBuffered()
	{
		return true;
	}

	/**
	 * Redraws the canvas. This method may safely be called from outside the
	 * event-dispatching thread.
	 */
	public final void redraw()
	{
		doRedraw = true;
		repaint();
	}

	/**
	 * Returns the offscreen graphics context or <code>null</code> if not
	 * available.
	 */
	protected final java.awt.Graphics getOffscreenGraphics()
	{
		return (buffer != null) ? buffer.getGraphics() : null;
	}
}
