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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import jparsec.ephem.EphemerisElement;
import jparsec.graph.chartRendering.AWTGraphics;
import jparsec.graph.chartRendering.PlanetRenderElement;
import jparsec.observer.ObserverElement;
import jparsec.time.TimeElement;
import jparsec.util.JPARSECException;
import jparsec.util.Logger;

class RenderPlanet extends JComponent
{
	private static final long serialVersionUID = 1L;
	jparsec.graph.chartRendering.RenderPlanet rp;

	/**
	 * The constructor.
	 * @param time Time object.
	 * @param obs Observer object.
	 * @param eph Ephemeris object.
	 * @param render Render object.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public RenderPlanet(TimeElement time, ObserverElement obs, EphemerisElement eph,
			PlanetRenderElement render) throws JPARSECException {
		super.setDoubleBuffered(false);
		rp = new jparsec.graph.chartRendering.RenderPlanet(time, obs, eph, render);
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

	/**
	 * Renderize a planet.
	 *
	 * @param g Graphics object.
	 * @throws JPARSECException Thrown if the calculation fails.
	 */
	public void renderize(Graphics2D g) throws JPARSECException
	{
		try {
			jparsec.graph.chartRendering.Graphics gj = new AWTGraphics(rp.render.width, rp.render.height, rp.render.anaglyphMode,
					rp.render.telescope.invertHorizontal, rp.render.telescope.invertVertical);
			rp.renderize(gj);
			g.drawImage((BufferedImage) gj.getRendering(), 0, 0, this);
		} catch (Exception exc) {
			if (exc instanceof JPARSECException) throw (JPARSECException) exc;
			exc.printStackTrace();
			throw new JPARSECException("Error during rendering. Details: "+exc.getLocalizedMessage(), exc);
		}
	}

	/**
	 * Returns the {@linkplain RenderPlanet} object.
	 * @return The {@linkplain RenderPlanet} object
	 */
	public jparsec.graph.chartRendering.RenderPlanet getRenderPlanet() {
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
	public final void paintComponent(Graphics g)
	{
		Insets insets = getInsets();
		int sizew = rp.render.width;
		if (rp.render.anaglyphMode == jparsec.graph.chartRendering.Graphics.ANAGLYPH_COLOR_MODE.TRUE_3D_MODE_LEFT_RIGHT) sizew *= 2;
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
	public final void setDoubleBuffered(boolean flag)
	{
		throw new IllegalArgumentException();
	}

	/**
	 * Double buffering is always enabled.
	 *
	 * @return true.
	 */
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
	protected final Graphics getOffscreenGraphics()
	{
		return (buffer != null) ? buffer.getGraphics() : null;
	}
}
