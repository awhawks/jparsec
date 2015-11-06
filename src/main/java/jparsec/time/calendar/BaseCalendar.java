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
package jparsec.time.calendar;

import java.io.Serializable;
import org.math.plot.plotObjects.Base;

/**
 * Implements a base Calendar.
 * <P>
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public abstract class BaseCalendar implements Serializable {
    protected final long year;
    protected final int month;
    protected final int day;
    protected boolean leapMonth;
    protected final long epoch;
    protected final long fixed;
    protected final double julianDate;

    BaseCalendar(final long epoch, final long y, final int month, final int day) {
        this.epoch = epoch;
        this.year = toYear(y);
        this.month = month;
        this.day = day;
        this.fixed = toFixed(y, month, day);
        this.julianDate = toJulian(this.fixed);
    }

    BaseCalendar(final long epoch, final long fromFixed) {
        this.epoch = epoch;
        this.fixed = fromFixed;
        this.julianDate = toJulian(fromFixed);
        this.year = yearFromFixed();
        this.month = monthFromFixed(this.year);
        this.day = dayFromFixed(this.year, this.month);
    }

    BaseCalendar(final long epoch, final double fromJulianDate) {
        this(epoch, (long) fromJulianDate - Gregorian.EPOCH);
    }

    double toJulian (final long fixed) {
        return fixed + Gregorian.EPOCH + 0.5D;
    }

    long toYear(final long year) {
        return year;
    }

    abstract long toFixed(final long year, final int month, final int day);

    abstract long yearFromFixed();

    abstract int monthFromFixed(final long year);

    abstract int dayFromFixed(final long year, final int month);

    public int getDayOfWeek() {
        return -1;
    }

    public int getDay() {
        return day;
    }

    public long getFixed() {
        return fixed;
    }

    public double getJulianDate() {
        return julianDate;
    }

    public int getMonth() {
        return month;
    }

    public long getYear() {
        return year;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName())
               .append(' ')
               .append(year)
               .append('/')
               .append(month)
               .append('/')
               .append(day);

        return builder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof BaseCalendar)) {
            return false;
        }

        BaseCalendar that = (BaseCalendar) o;

        return fixed == that.fixed;
    }

    @Override
    public int hashCode() {
        return (int) (fixed ^ (fixed >>> 32));
    }
}
