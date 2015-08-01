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
package jparsec.astrophysics.gildas;

import java.io.Serializable;
import java.util.GregorianCalendar;

import jparsec.time.AstroDate;
import jparsec.util.JPARSECException;

/**
 * Implementation of the EEEI format for GILDAS files.
 * Write is supported.
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class EEEI2EEEI
    implements Convertible, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
    public EEEI2EEEI() { }

    public short readShort(byte abyte0[], int i)
    {
        return ByteArrayConverter.readShort(abyte0, i);
    }

    public int readInt(byte abyte0[], int i)
    {
        return ByteArrayConverter.readInt(abyte0, i);
    }

    public float readFloat(byte abyte0[], int i)
    {
        float f = ByteArrayConverter.readFloat(abyte0, i);
        return f;
    }

    public double readDouble(byte abyte0[], int i)
    {
        return ByteArrayConverter.readDouble(abyte0, i);
    }

    public GregorianCalendar readDate(byte abyte0[], int i)
    throws JPARSECException {
        return ConverterFactory.getDate(readInt(abyte0, i));
    }  
    
    public void writeShort(byte abyte0[], int i, short value)
    {
        ByteArrayConverter.writeShort(abyte0, i, value);
    }

    public void writeInt(byte abyte0[], int i, int value)
    {
        ByteArrayConverter.writeInt(abyte0, i, value);
    }

    public void writeFloat(byte abyte0[], int i, float value)
    {
        ByteArrayConverter.writeFloat(abyte0, i, value);
    }

    public void writeDouble(byte abyte0[], int i, double value)
    {
        ByteArrayConverter.writeDouble(abyte0, i, value);
    }

    public void writeDate(byte abyte0[], int i, double jd)
    throws JPARSECException {
        int value = ConverterFactory.getGILDASdate(jd);
        ByteArrayConverter.writeInt(abyte0, i, value);
    }
    
    /**
     * Testing program
     * @param args Unused.
     */
    public static void main(String args[])
    {
    	System.out.println("EEEI2EEEI test");
    	
    	try {
    		EEEI2EEEI v = new EEEI2EEEI();
	    	byte[] abyte0 = new byte[4];
	    	byte[] abyte1 = new byte[8];
	    	byte[] abyte2 = new byte[2];
	    	int i = 0;
	    	int valueI = 1234;
	    	float valueF = 1234.1f;
	    	double valueD = 1234.1234;
	    	short valueS = 123;
	    	
	    	v.writeInt(abyte0, i, valueI);
	    	int valueI2 = v.readInt(abyte0, i);
	    	System.out.println(valueI+" = "+valueI2+" / "+abyte0[0]+"/"+abyte0[1]+"/"+abyte0[2]+"/"+abyte0[3]);
	    	
	    	v.writeFloat(abyte0, i, valueF);
	    	float valueF2 = v.readFloat(abyte0, i);
	    	System.out.println(valueF+" = "+valueF2+" / "+abyte0[0]+"/"+abyte0[1]+"/"+abyte0[2]+"/"+abyte0[3]);
	    	
	    	v.writeDouble(abyte1, i, valueD);
	    	double valueD2 = v.readDouble(abyte1, i);
	    	System.out.println(valueD+" = "+valueD2+" / "+abyte1[0]+"/"+abyte1[1]+"/"+abyte1[2]+"/"+abyte1[3]);
	
	    	v.writeShort(abyte2, i, valueS);
	    	Short valueS2 = v.readShort(abyte2, i);
	    	System.out.println(valueS+" = "+valueS2+" / "+abyte2[0]+"/"+abyte2[1]);
	    	
	    	valueD = 2451545.45;
	    	v.writeDate(abyte0, i, valueD);
	    	AstroDate ad =  new AstroDate(v.readDate(abyte0, i));
	    	double valueD3 = ad.jd();
	    	System.out.println(valueD+" = "+valueD3+" / "+ad.toString()+"/"+abyte0[0]+"/"+abyte0[1]+"/"+abyte0[2]+"/"+abyte0[3]);
    	} catch (Exception exc)
    	{
    		exc.printStackTrace();
    	}
    }
}
