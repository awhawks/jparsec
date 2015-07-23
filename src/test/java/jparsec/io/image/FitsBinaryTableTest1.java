package jparsec.io.image;

import nom.tam.fits.BasicHDU;
import nom.tam.fits.BinaryTableHDU;

public class FitsBinaryTableTest1 {
    /**
     * Test program: writing .fits files that conforms to ALMA
     * Test Interferometer Raw Data Format.
     *
     * @param args Unused.
     */
    public static void main(String args[]) throws Exception {
        System.out.println("FitsBinaryTable test 1");

        // Define the primary header and the binary tables to write. The primary header is a
        // set of string values to be added to the default header. A binary table consists on a
        // header with a set of strings, booleans, doubles, and long values,
        // and the data of the binary table, which is an structure with certain named columns
        // and data for each column (previous data-types as well as arrays of them). A binary
        // table consists on a set of repeated column structures, one for each row. Note the
        // special way developed here to insert arrays of different datatypes. The values for
        // each record must be numerical/string values without '/'
        ImageHeaderElement primaryHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "SIMPLE  L  T",
                "NAXIS  I  0",
                "BITPIX  I  32",
                "EXTEND  L  T",
                "TELESCOP 13A OAN-ARIESY", // format is entry name, entry format (fits convention), value, / comment (optional)
                "ORIGIN A CAY/OAN/IGN",
                "CREATOR A T. Alonso Albi - OAN, JPARSEC-package",
                "COMMENT A Testing phase",
                "OBSERVER  A  Pepe"
        });
        // More possible keywords:  DISTANCE, CALMODE
        ImageHeaderElement dataparHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A DATAPAR-ALMATI",
                "TABLEREV J 1",
                "TELESCOP 13A OAN-ARIESY",
                "SCAN-NUM J 1",
                "OBS-NUM J 1", // distinguish observations within a given scan
                "DATE-OBS A 2000-01-20T10:00:00.000",
                "TIMESYS A TAI",
                "LST D 0.0 / apparent sidereal time in seconds of time",
                "OBSMODE 4A CORR / scan type",
                "PROJID   4A HOLO               / Project ID",
                "AZIMUTH  D -1.0945473084575E+02 / Azimuth (degrees)",
                "ELEVATIO  D 4.2869955533152E+01 / Elevation (degrees)",
                "LATSYS   8A ELEV-SIN           / type of latitude-like offsets",
                "LONGSYS  8A AZIM-SIN           / type of longitude-like offsets",
                "EXPOSURE  E    5.00000000E+00 / Total integration time (s)",
                "NO_ANT      J                1 / Number of antennas",
                "NO_BAND    J                 1 / Number of basebands",
                "NO_CHAN    J               0 / Total number of channels",
                "NO_POL      J                1 / Number of pols",
                "NO_FEED    J                 1 / Number of feeds",
                "NO_SIDE     J                1 / Number of side bands",
                "VFRAME  D  0.0 / radial vel. corr. (m/s)",
                "NO_PHCOR  J                  0 / Number of phase corr. data",
                "NO_CORR    J                 0 / Number of CORRDATA Tables per baseband",
                "NO_AUTO    J                 0 / Number of AUTODATA Tables per baseband",
                "NO_HOLO    J                 1 / Number of HOLODATA Tables per baseband",
                "FRONTEND  J                  -1 / number ID of front end used",
                "OBS-LAT   D 40.5 / Observatory Latitude (degrees)",
                "OBS-LONG  D -3.09 / Observatory Latitude (degrees)",
                "OBS-ELEV   E    931 / Observatory Elevation (m)",
                "SOURCE  12A  Hispasat 1C             / Source Name",
                "CALCODE  4A PHAS               / Calibrator code",
                "RADESYS   4A FK4                / Equatorial Coordinate system",
                "RA      D  3.0773687500000E+02 / Right Ascension (ICRF)",
                "DEC    D   4.0488972222222E+01 / Declination (ICRF)",
                "PMRA  D    0.0000000000000E+00 / RA Pr.Motion (deg./Jul.y.) [dRA*cos(Dec)/dt]",
                "PMDEC D    0.0000000000000E+00 / DEC Pr.Motion (deg./Jul.y.)",
                "EQUINOX  E      1.95000000E+03 / Equinox",
                "GLON      D  0  / Galactic longitude in deg, optional",
                "GLAT    D   0 / Galatic latitude in deg, optional",
                "ELON      D  0  / Ecliptic longitude in deg, optional",
                "ELAT    D   0 / Ecliptic latitude in deg, optional",
                "VELTYP   A VELO-EAR           / Velocity type",
                "UT1UTC  D  3.8090100000000E-01 / UT1-UTC",
                "TAIUTC  D  3.2000000000000E+01 / TAI-UTC",
                "POLARX  D  1.0300000000000E-04 / x coordinate of North Pole",
                "POLARY  D  1.0700000000000E-04 / x coordinate of North Pole",
                "AZIM-FIX  D -1.0945473084575E+02 / Fixed Azimuth (degrees)",
                "ELEV-FIX  D 4.2869955533152E+01 / Fixed Elevation (degrees)",
                "NO_SWITC    J                 0 / num. of switch phases in a switch cycle"
        });
        // In the tables (not table header) the TUNIT keyword is taken from the comment,
        // if it ends with <space>in>space><unit>.
        ImageHeaderElement dataparTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM J 1 /   Integration point number",
                        "INTEGTIM E 1 /   Integration time in s",
                        "MJD    D  51545.0 /  Observing date/time (Modified Julian Date) in day",
                        "UUVVWW D(3,1) double[][]{new double[] {1.0, 2.0},new double[] {1.0, 2.0},new double[] {1.0, 2.0}}   /     u,v,w antenna coord. projected on source vector in s", // D(3,1) 1 = No antennas
                        "AZELERR  E(2,1)  float[][]{new float[] {0.0},new float[] {0.0}}  /   Az,El pointing errors in deg",
                        "SOURDIR  D(3,1)  double[][]{new double[] {0.0},new double[] {0.0},new double[] {0.0}}  /     Source direction cosines",
                        "DELAYGEO D(1)   1110.0          /     Geometrical Delay in s",
                        "DELAYOFF D(1,1)  double[][]{new double[] {0.0}}     /     Delay offset in s", // D(1,1) = D(No base-bands, no antennas)
                        "PHASEGEO D(1)   0.0          /   Geometrical Phase in rad", // No antennas
                        "PHASEOFF D(1,1) double[][]{new double[] {0.0}}      /   Phase Offset in rad", // = DELAYOFF
                        "RATEGEO  D(1)    0.0         / Geometrical Phase Rate in rad/s", // = PHASEGEO
                        "RATEOFF  D(1,1)  double[][]{new double[] {0.0}}     / Phase Rate Offset in rad/s", // = DELAYOFF
                        "FOCUSOFF E(1)   1110.0          /     Focus offset in m", // = PHASEGEO
                        "LATOFF   E(1)      1110.0       /   lat.like offset (dLat) in deg", // = PHASEGEO
                        "LONGOFF  E(1)    110.0         /   long.like offset (dLong)*cos(lat) in deg", // = PHASEGEO
                        "TOTPOWER E(1,1)  float[][]{new float[]{0.0}}     /   Total Power in each baseband in adu", // = DELAYOFF
                        "WINDSPEE E     1.0            /   Wind speed in m/s",
                        "WINDDIRE E      1.0           /  Wind direction (E from N) in deg",
                        "FLAG     J(1,1,1)   int[][][]{new int[][]{new int[]{0}}} /     Flag words", // J(no polar products=1/2/4, no base-bands, no antennas)
                        "ISWITCH  4A   0000                 /   ID of phase in switch cycle",
                        "WSWITCH  E    0.0              /     weight of phase in switch cycle",
                        "AUTO     L(1)    boolean[] {true}         /     Integration present in AUTODATA-ALMATI Tables", // No autocorrelation tables per baseband
                        "CORR     L(1)    boolean[] {true}         /     Integration present in CORRDATA-ALMATI Tables", // No correlation tables per baseband
                        "HOLO     L      T              /   Integration present in HOLODATA-ALMATI Table"
                }),
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM J 2 /   Integration point number",
                        "INTEGTIM E 1 /   Integration time in s",
                        "MJD    D  51545.0 /  Observing date/time (Modified Julian Date) in day",
                        "UUVVWW D(3,1) double[][]{new double[] {0.0},new double[] {0.0},new double[] {0.0}}   /     u,v,w antenna coord. projected on source vector in s", // D(3,1) 1 = No antennas
                        "AZELERR  E(2,1)  float[][]{new float[] {0.0},new float[] {0.0}}  /   Az,El pointing errors in deg",
                        "SOURDIR  D(3,1)  double[][]{new double[] {0.0},new double[] {0.0},new double[] {0.0}}  /     Source direction cosines",
                        "DELAYGEO D(1)   1110.0          /     Geometrical Delay in s",
                        "DELAYOFF D(1,1)  double[][]{new double[] {0.0}}     /     Delay offset in s", // D(1,1) = D(No base-bands, no antennas)
                        "PHASEGEO D(1)   0.0          /   Geometrical Phase in rad", // No antennas
                        "PHASEOFF D(1,1) double[][]{new double[] {0.0}}      /   Phase Offset in rad", // = DELAYOFF
                        "RATEGEO  D(1)    0.0         / Geometrical Phase Rate in rad/s", // = PHASEGEO
                        "RATEOFF  D(1,1)  double[][]{new double[] {0.0}}     / Phase Rate Offset in rad/s", // = DELAYOFF
                        "FOCUSOFF E(1)    111.0          /     Focus offset in m", // = PHASEGEO
                        "LATOFF   E(1)       110.0       /   lat.like offset (dLat) in deg", // = PHASEGEO
                        "LONGOFF  E(1)     110.0         /   long.like offset (dLong)*cos(lat) in deg", // = PHASEGEO
                        "TOTPOWER E(1,1)   float[][]{new float[]{0.0}}     /   Total Power in each baseband in adu", // = DELAYOFF
                        "WINDSPEE E     1.0            /   Wind speed in m/s",
                        "WINDDIRE E      1.0           /  Wind direction (E from N) in deg",
                        "FLAG     J(1,1,1)  int[][][]{new int[][]{new int[]{0}}} /     Flag words", // J(no polar products=1/2/4, no base-bands, no antennas)
                        "ISWITCH  4A   0000                 /   ID of phase in switch cycle",
                        "WSWITCH  E    0.0              /     weight of phase in switch cycle",
                        "AUTO     L(1)     boolean[] {true}         /     Integration present in AUTODATA-ALMATI Tables", // No autocorrelation tables per baseband
                        "CORR     L(1)     boolean[] {true}         /     Integration present in CORRDATA-ALMATI Tables", // No correlation tables per baseband
                        "HOLO     L      true              /   Integration present in HOLODATA-ALMATI Table"
                }),
        };

        ImageHeaderElement calibrHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A    CALIBR-ALMATI",
                "TABLEREV J    1           /     Table format revision number",
                "SCAN-NUM J 1            /      Scan number",
                "OBS-NUM  J 1             /     Observation number",
                "DATE-OBS A 2000-12-31T23:59:59.999            /      observing date (ISO format)",
                "NO_ANT  J    1          /   number of antennas",
                "NO_BAND  J    1    /         number of base-bands",
                "NO_FEED  J    1     /        number of feeds (1/2)",
                "NO_POL   J    1     /        number of pols. products",
                "NO_SIDE  J    1     /        number of sidebands",
                "FREQUSRD E 2E11       /          [PHC] radiometer signal frequency in Hz",
                "FREQUIRD E 2E11        /         [PHC] radiometer image frequency in Hz"
        });
        ImageHeaderElement calibrTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "ANTENNID 1J     0       /    Antenna number ID",
                        "STATIOID 1J       0     /    Station number ID",
                        "ANTENAME 12A  OAN40m   /    Antenna name",
                        "STATNAME 12A   Yebes /    Station name",
                        "STABXYZ  3D       0.0     /    Coordinates in m",
                        "STAXOF   E      0      /    Axis offset in m",
                        "POLTY    1A(1)    String[]{\"X\"}   /    Feed type (X, Y, L, R)", // A(no polar feeds)
                        "POLA     E(1)     float[]{1.0}   /  Feed orientation in deg",  // E(no polar feeds)
                        "APEREFF  E(1,1)   float[][]{new float[]{1.0}}  /    Aperture efficiency",  // E(no polar feeds, no basebands)
                        "BEAMEFF  E(1,1)  float[][]{new float[]{1.0}}  /    Beam efficiency", // E(no polar feeds, no basebands)
                        "ETAFSS   E(1,1)   float[][]{new float[]{1.0}}   /   Forward efficiency", // E(no polar feeds, no basebands)
                        "ANTGAIN  E(1,1)   float[][]{new float[]{1.0}}  / K/Jy Antenna Gain", // E(no polar feeds, no basebands)
                        "HUMIDITY E    0.8         /    rel humidity (0.-1.)",
                        "TAMBIENT E      273       /    Ambient temperature in K",
                        "PRESSURE E     1E5       /   Ambient pressure in Pa",
                        "THOT     E(1,1)  float[][]{new float[]{1.0}} /    Chopper temperature in K", // E(no polar feeds, no basebands)
                        "TCOLD    E(1,1)  float[][]{new float[]{1.0}} /    Cold Load temperature in K", // E(no polar feeds, no basebands)
                        "PHOT     E(1,1)  float[][]{new float[]{1.0}} /  Total power on Chopper in adu", // E(no polar feeds, no basebands)
                        "PCOLD    E(1,1)  float[][]{new float[]{1.0}} /  Total power on Cold Load in adu", // E(no polar feeds, no basebands)
                        "PSKY     E(1,1)  float[][]{new float[]{1.0}}  /  Total power on Sky in adu", // E(no polar feeds, no basebands)
                        "GAINIMAG E(1,1)  float[][]{new float[]{1.0}}  /    Gain ratio image/signal", // E(no polar feeds, no basebands)
                        "TRX      E(1,1)    float[][]{new float[]{1.0}} /    Receiver temperature in K", // E(no polar feeds, no basebands)
                        "TSYS     E(1,1)   float[][]{new float[]{1.0}} /    System temperature in K", // E(no polar feeds, no basebands)
                        "TSYSIMAG E(1,1)   float[][]{new float[]{1.0}} /    System temperature (Image) in K", // E(no polar feeds, no basebands)
                        "TAU      E(1,1)   float[][]{new float[]{1.0}} /    Opacity", // E(no polar feeds, no basebands)
                        "TAUIMAG  E(1,1)   float[][]{new float[]{1.0}} /    Opacity (Image)", // E(no polar feeds, no basebands)
                        "TCABIN   E    273 /    Receiver Cabin temp.",
                        "TDEWAR   E  20 /    Receiver Dewar temp.",
                        "IA       E       0.1     /  Pointing Coefficient in deg",
                        "CA       E      0.1      /  Pointing Coefficient in deg",
                        "NPAE     E    0.1        /  Pointing Coefficient in deg",
                        "AN       E     0.1       /  Pointing Coefficient in deg",
                        "AW       E    0.1        /  Pointing Coefficient in deg",
                        "IE       E      0.1      /  Pointing Coefficient in deg",
                        "ECEC     E     0.1       /  Pointing Coefficient in deg",
                        "IA-R      E       0.1    / Pointing Coefficient (receiver) in deg",
                        "CA-R      E    0.1       / Pointing Coefficient (receiver) in deg",
                        "IE-R      E      0.1     / Pointing Coefficient (receiver) in deg",
                        "ECEC-R    E    0.1       / Pointing Coefficient (receiver) in deg",
                        "A-OBS     E   0.1        / Pointing correction [dAz*cos(El)] in deg",
                        "E-OBS     E   0.1        / Pointing correction in deg",
                        "REFRACTIO E   0.1        / Refraction correction (current) in deg",
                        "PREWATER  E(1,1)  float[][]{new float[]{1.0}}  /   Precipitable water vapor (meter!)", // E(no polar feeds, no basebands)
                        "PREWATRD  E     0      /   [PHC] Prec. water vapor from radiometer in m",
                        "ETAFSRD   E      0     /   [PHC] Forward efficiency of radiometer in m",
                        "THOTRD    E      0     /   [PHC] Chopper temperature of radiometer in K",
                        "TCOLDRD   E     0      /   [PHC] Cold Load temperature of radiometer in K",
                        "PHOTRD    E      0     / [PHC] radiometer power on Chopper in adu",
                        "PCOLDRD   E     0      / [PHC] radiometer power on Cold Load in adu",
                        "PSKYRD    E       0    / [PHC] radiometer power on Sky in adu",
                        "TSYSRD    E       0    /   [PHC] radiometer system temperature in K",
                        "TRXRD     E       0    /   [PHC] radiometer receiver temperature in K",
                        "GAINIMRD  E     0      /   [PHC] radiometer Gain ratio image/signal",
                        "PATHRD    E      0     /   [PHC] water vapor pathlength at (obs. freq) in m",
                        "DPATHRD   E     0      /   [PHC] increment per K of radiometric emission in m",
                        "VALIDRD   L       0    /   [PHC] validity of radiometric correction"
                })
        };

        ImageHeaderElement corrdataHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A   CORRDATA-ALMATI",
                "TABLEREV J   1         /         Table format revision number",
                "SCAN-NUM J  0           /        Scan number",
                "OBS-NUM  J  0           /       Observation number",
                "DATE-OBS A   2001-12-31T23:59:59.999         /     observing date (ISO format)",
                "NO_POL   J      1  /      number of pol. products",
                "NO_SIDE  J      1  /      number of sidebands",
                "NO_LO    J       1        /   number of LOs",
                "BASEBAND J   1        /      Baseband number",
                "TABLEID  J   1          /   Baseband table number",
                "FREQLO1  D   1E5     /        LO1 Frequency in Hz",
                "SIDEBLO1 J   1          /    side band LO1",
                "FREQLO2  D   1E5      /       LO2 Frequency in Hz",
                "SIDEBLO2 J   1          /    side band LO2",
                "INTERFRE D   0      /       Intermediate frequency at ref. channel in Hz",
                "FREQRES  D   0     /        Frequency resolution in Hz",
                "IFLUX    E  0          /   I flux in Jy",
                "QFLUX    E  0        /     Q flux in Jy",
                "UFLUX    E  0        /     U flux in Jy",
                "VFLUX    E  0        /     V flux in Jy",
                "RESTFRQA D  0   /         Rest frequency A in m/s",
                "TRANSITA 12A  0         /     line identifier for A",
                "RESTFRQB D  0   /         Rest frequency B in m/s",
                "TRANSITB 12A  0       /      line identifier for B",
                "1CTYP4   8A      COMPLEX / Complex axis for col.4 (USB1)",
                "1CRPX4   E     1.0   /     Ref. pixel",
                "1CRVL4   E     1.0   /     Value at ref. pixel",
                "11CD4    E     1.0   /     Increment per pixel",
                "2CTYP4   8A     FREQ-FRQ  / Frequency axis for col.4 (USB1)",
                "2CRPX4   E  1E5        /     Ref. channel in Hz",
                "2CRVL4   D  0.0       /     Observed frequency in Hz",
                "22CD4    E  0.0         /    Channel Separation in Hz",
                "2CUNI4   A  Hz    /   Unit in Hz",
                "2CTYP4A  8A  VRAD-FRQ / Velocity axis for col.4 (USB1)",
                "2CRPX4A  E  0     /       Ref. channel in m/s",
                "2CRVL4A  D  0     /       Velocity at ref. channel in m/s",
                "22CD4A   E  0      /      Velocity Channel Separation in m/s",
                "2CUNI4A  A  m/s  /    Unit in m/s",
                "2VSOU4A  E  0     /       Source Velocity in m/s",
                "2SPEC4A  A LSRK-TOP / Velocity System in m/s",
                "3CTYP4   8A     STOKES  /   Stokes axis for col.4 (USB1)",
                "3CRPX4   E     1.0   /     Ref. pixel",
                "3CRVL4   E     -5.0  /     Value at ref. pixel",
                "33CD4    E     1.0   /     Increment per pixel",
                "4CTYP4   8A     PHASCORR / Phase Corr.axis for col.4 (USB1)",
                "4CRPX4   E     1.0    /    Ref. pixel",
                "4CRVL4   E     0.0    /    Value at ref. pixel",
                "44CD4    E     1.0    /    Increment per pixel",
                "1CTYP5   8A     COMPLEX / Complex axis for col.5 (LSB1)",
                "1CRPX5   E     1.0    /    Ref. pixel",
                "1CRVL5   E     1.0    /    Value at ref. pixel",
                "11CD5    E     1.0    /    Increment per pixel",
                "2CTYP5   8A     FREQ-FRQ / Frequency axis for col.5 (LSB1)",
                "2CRPX5   E  0        /     Ref. channel in Hz",
                "2CRVL5  D   0       /   Observed frequency in Hz",
                "22CD5   E    0        /  Channel Separation in Hz",
                "2CUNI5  A Hz     /  Unit in Hz",
                "2CTYP5B 8A    VRAD-FRQ / Velocity axis for col.5 (LSB1)",
                "2CRPX5B E 0       /     Ref. channel in m/s",
                "2CRVL5B D 0       /     Velocity at ref. channel in m/s",
                "22CD5B  E 0        /    Velocity Channel Separation in m/s",
                "2CUNI5B E 0    /  Unit in m/s",
                "2VSOU5B E 0      /      Source Velocity in m/s",
                "2SPEC5B A LSRK-TOP / Velocity System",
                "3CTYP5  8A    STOKES  / Stokes axis for col.5 (LSB1)",
                "3CRPX5  E    1.0    /    Ref. pixel",
                "3CRVL5  E    -5.0   /    Value at ref. pixel",
                "33CD5   E    -1.0   /    Increment per pixel",
                "4CTYP5  8A    PHASCORR  /  Phase Corr.axis for col.5 (LSB1)",
                "4CRPX5  E    1.0    /    Ref. pixel",
                "4CRVL5  E    0.0    /    Value at ref. pixel",
                "44CD5   E    1.0    /    Increment per pixel"
        });
        ImageHeaderElement corrdataTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM I  0  / Integration point number",
                        "STARTANT I   0 / Start Antenna",
                        "ENDANTEN I  0  / End Antenna",
                        "DATAUSB1 E  0.0  / Data for USB of LO1",
                        "DATALSB1 E  0.0  / Data for LSB of LO1"
                })
        };

        ImageHeaderElement autodataHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A       AUTODATA-ALMATI",
                "TABLEREV J   1                /    Table format revision number",
                "NO_POL   J       NPO         /      number of pol. products",
                "SCAN-NUM J   0                /     Scan number",
                "OBS-NUM  J   0                 /    Observation number",
                "DATE-OBS A   2001-12-31T00:00:00.000                /     observing date (ISO format)",
                "BASEBAND J   0                /     Baseband number",
                "TABLEID  J   0                 /   Baseband table number",
                "FREQLO1  D   1E5            /        LO1 Frequency in Hz",
                "SIDEBLO1 J   0                 /    side band LO1",
                "FREQLO2  D   1E5            /        LO2 Frequency in Hz",
                "SIDEBLO2 J   0                /     side band LO2",
                "IF_VAL   D   1E5                  /   IF frequency of ref. channels",
                "FREQRES  D   1E3               /      Frequency resolution",
                "RESTFRQA D  0        /           Rest frequency A in m/s",
                "TRANSITA 12A CO              /       line identifier for A",
                "RESTFRQB D  0        /           Rest frequency B in m/s",
                "TRANSITB 12A CO             /        line identifier for B",
                "IFLUX    E  0                /    I flux in Jy",
                "QFLUX    E  0              /      Q flux in Jy",
                "UFLUX    E  0              /      U flux in Jy",
                "VFLUX    E  0              /      V flux in Jy",
                "1CTYP3   8A     FREQ-FRQ  /     Frequency axis for col.3",
                "1CRPX3   E  0                /    Ref. channel in Hz",
                "1CRVL3   D  0              /      Observed frequency in Hz",
                "11CD3    E  0            /        Channel Separation in Hz",
                "1CUNI3   A    Hz       /      Unit in Hz",
                "1CTYP3A  8A    VRAD-FRQ   /   Velocity axis for col.3",
                "1CRPX3A  E  0              /     Ref. channel in m/s",
                "1CRVL3A D 0         /   Velocity at ref. channel in m/s",
                "11CD3A  E 0         /   Velocity Channel Separation in m/s",
                "1CUNI3A E   0  /  Unit in m/s",
                "1VSOU3A E 0       /     Source Velocity in m/s",
                "1SPEC3A A   LSRK-TOP  /  Velocity System",
                "2CTYP3  8A     STOKES  /  Stokes axis for col.3",
                "2CRPX3  E    1.0    /    Ref. pixel",
                "2CRVL3  E    -5.0   /    Value at ref. pixel",
                "22CD3   E    -1.0   /    Increment per pixel"
        });
        ImageHeaderElement autodataTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM I 0 / Integration point number",
                        "ANTENNA  I 0 / Antenna",
                        "DATA     E 0.0 / Data"
                })
        };

        ImageHeaderElement holodataHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A   HOLODATA-ALMATI",
                "TABLEREV J   1               /    Table format revision number",
                "SCAN-NUM J  0              /     Scan number",
                "OBS-NUM  J   0              /    Observation number",
                "DATE-OBS A  2000-01-31T00:00:00.000              /     observing date (ISO format)",
                "BASEBAND J   1           /      Baseband number",
                "CHANNELS J   1           /      Number of channels in baseband",
                "TABLEID J   1           /      Baseband table number",
                "TRANDIST E    38000000          /      transmitter distance in m",
                "TRANFREQ D   12750000000         /        transmitter frequency in Hz",
                "TRANFOCU D    0        /         offset from prime focus in m",
                "NO_POL  J   1           /      Number of polarization products"
        });
        ImageHeaderElement holodataTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM J  0 / Integration point number",
                        "HOLOSS   E 0.0 / Data S*S",
                        "HOLORR   E 0.0 / Data R*R",
                        "HOLOQQ   E 0.0 / Data Q*Q",
                        "HOLOSR   E 0.0 / Data S*R",
                        "HOLOSQ   E 0.0 / Data S*Q",
                        "HOLOQR   E 0.0 / Data Q*R"
                })
        };

        ImageHeaderElement monitorHeader[] = FitsBinaryTable.parseHeader(new String[] {
                "EXTNAME  A   MONITOR-ALMATI",
                "TABLEREV J   1            /   Table format revision number", // Could be also format A, same error as before?
                "SCAN-NUM J   0            /    Scan number",
                "OBS-NUM  J    0           /    Observation number",
                "DATE-OBS A   2000-12-31T00:00:00.000        /     observing date (ISO format)"
        });
        ImageHeaderElement monitorTable[][] = new ImageHeaderElement[][] {
                FitsBinaryTable.parseHeader(new String[] {
                        "INTEGNUM        1J  0  /   Integration point number"
                })
        };

        // Create the fits file starting with a header from scratch
        FitsIO f = new FitsIO(FitsIO.createHDU(null, primaryHeader)); // Without image data, or
        //f.addHDU(FitsBinaryTable.createBinaryTable(primaryHeader, null)); // Without image data

        // Add binary tables in the adequate order
        BasicHDU dataparHDU = FitsBinaryTable.createBinaryTable(dataparHeader, dataparTable);
        BasicHDU calibrHDU = FitsBinaryTable.createBinaryTable(calibrHeader, calibrTable);
        BasicHDU corrdataHDU = FitsBinaryTable.createBinaryTable(corrdataHeader, corrdataTable);
        BasicHDU autodataHDU = FitsBinaryTable.createBinaryTable(autodataHeader, autodataTable);
        BasicHDU holodataHDU = FitsBinaryTable.createBinaryTable(holodataHeader, holodataTable);
        BasicHDU monitorHDU = FitsBinaryTable.createBinaryTable(monitorHeader, monitorTable);
        f.addHDU(dataparHDU);
        //f.addHDU(calibrHDU);
        //f.addHDU(corrdataHDU);
        //f.addHDU(autodataHDU);
        f.addHDU(holodataHDU);
        //f.addHDU(monitorHDU);

        // Generate output file
        String outputFile = "/home/alonso/colaboraciones/Pablo/2008/fitsALMA/testFitNew.fits";
        f.writeEntireFits(outputFile);

        main2(args);
    }

    /**
     * Test program 2: file reading.
     *
     * @param args Unused.
     */
    private static void main2(String args[]) throws Exception {
        System.out.println("FitsBinaryTable Test 2");
        // Scan TEST0700.FITS with 433 HDUs = primary header + 2 (datapar + holodata) * 216 observations
        String path = "/home/alonso/colaboraciones/Pablo/2008/fitsALMA/testFitNew.fits"; // 7 HDUs
        FitsIO f = new FitsIO(path);
        int n = f.getNumberOfPlains();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Found " + n + " HDUs");
        System.out.println(f.toString());

        for (int i = 0; i < n; i++) {
            System.out.println("HDU # " + (i + 1));

            if (f.isBinaryTable(i)) {
                BinaryTableHDU bintable = (BinaryTableHDU) f.getHDU(i);
                for (int j = 0; j < bintable.getNCols(); j++) {
                    System.out.println("* " + bintable.getColumnName(j) + " / " + bintable.getColumn(j).getClass().getSimpleName());
                }
            }

            ImageHeaderElement header[] = f.getHeader(i);
            System.out.println(ImageHeaderElement.toString(header));
        }
    }
}
