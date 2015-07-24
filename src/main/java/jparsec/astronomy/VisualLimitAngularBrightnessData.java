package jparsec.astronomy;

/**
 * A support class for VisualLimit.
 * <P>
 * Holds values which vary across the sky.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @author Mark Huss
 * @version 1.0
 */
class VisualLimitAngularBrightnessData
{
	/**
	 * Constructor for an empty object.
	 */
	public VisualLimitAngularBrightnessData()
	{
	}

	/**
	 * Constructor of an explicit defined object.
	 *
	 * @param za Zenith angle.
	 * @param dm Angular distance of the Moon.
	 * @param ds Angular distance of the Sun.
	 */
	public VisualLimitAngularBrightnessData(double za, double dm, double ds)
	{
		zenithAngle = za;
		moonAngularDistance = dm;
		sunAngularDistance = ds;
	}

	/**
	 * The zenith angle in radians.
	 */
	public double zenithAngle;

	/**
	 * The lunar angular distance in radians.
	 */
	public double moonAngularDistance;

	/**
	 * The solar angular distance in radians.
	 */
	public double sunAngularDistance;
}
