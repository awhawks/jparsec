package jparsec.astronomy;

// A RotateFrom term
class RotateFrom
{
	RotateFrom(double alfa0, double delta0, double l0, double alfa, double delta)
	{
		ALFA0 = alfa0;
		DELTA0 = delta0;
		LON0 = l0;
		ALFA = alfa;
		DELTA = delta;
	}

	double ALFA0;
	double DELTA0;
	double LON0;
	double ALFA;
	double DELTA;
}
