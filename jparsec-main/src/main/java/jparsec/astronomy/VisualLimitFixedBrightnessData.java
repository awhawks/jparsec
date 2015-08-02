package jparsec.astronomy;

class VisualLimitFixedBrightnessData
{
	/**
	 * Constructor of an empty object.
	 */
	public VisualLimitFixedBrightnessData()
	{
	}

	/**
	 * Constructor of an explicit defined object.
	 *
	 * @param zm Lunar zenith angle.
	 * @param zs Solar zenith angle.
	 * @param me Moon elongation.
	 * @param h Height above sea level.
	 * @param lat Latitude.
	 * @param t Temperature.
	 * @param rh Relative humidity.
	 * @param y Year.
	 * @param m Month.
	 */
	public VisualLimitFixedBrightnessData(double zm, double zs, double me, double h, double lat, double t, double rh,
			double y, double m)
	{
		moonZenithAngle = zm;
		sunZenithAngle = zs;
		moonElongation = me;
		heightAboveSeaLevel = h;
		latitude = lat;
		temperature = t;
		relativeHumidity = rh;
		year = y;
		month = m;
	}

	/**
	 * The lunar zenith angle in radians.
	 */
	public double moonZenithAngle;

	/**
	 * The solar zenith angle in radians.
	 */
	public double sunZenithAngle;

	/**
	 * The lunar elongation in radians.
	 */
	public double moonElongation;

	/**
	 * Altitude (above sea level) in meters.
	 */
	public double heightAboveSeaLevel;

	/**
	 * Latitude in radians.
	 */
	public double latitude;

	/**
	 * Temperature in degrees Celsius.
	 */
	public double temperature;

	/**
	 * Relative humidity, from 0 to 100.
	 */
	public double relativeHumidity;

	/**
	 * Year.
	 */
	public double year;

	/**
	 * Month.
	 */
	public double month;
}
