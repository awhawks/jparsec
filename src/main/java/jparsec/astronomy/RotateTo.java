package jparsec.astronomy;

//A RotateTo term
class RotateTo
{
	RotateTo(double alfa0, double delta0, double l0, double x, double y)
	{
		ALFA0 = alfa0;
		DELTA0 = delta0;
		LON0 = l0;
		X = x;
		Y = y;
	}

	double ALFA0;
	double DELTA0;
	double LON0;
	double X;
	double Y;
}
