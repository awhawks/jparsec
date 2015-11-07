/**
 * This file is part of JPARSEC library.
 * <p/>
 * (C) Copyright 2006-2015 by T. Alonso Albi - OAN (Spain).
 * <p/>
 * Project Info:  http://conga.oan.es/~alonso/jparsec/jparsec.html
 * <p/>
 * JPARSEC library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * JPARSEC library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jparsec.time.calendar;

/**
 * Implements the Egyptian calendar. See Calendrical Calculations for reference.
 *
 * @author T. Alonso Albi - OAN (Spain)
 * @version 1.0
 */
public class Egyptian extends BaseCalendar {
    /**
     * Calendar epoch.
     * The Egyptian calendar year 1 started on Julian date 747/02/26 BCE
     */
    public static final long EPOCH = new Julian(-747, 2, 26).fixed;

    /**
     * Month names.
     */
    public static final String MONTH_NAMES[] = {
            "Thoth", "Phaophi", "Athyr", "Choiak", "Tybi", "Mechir", "Phamenoth",
            "Pharmuthi", "Pachon", "Payni", "Epiphi", "Mesori", "Epagomenai"
    };

    private static final long serialVersionUID = 6136242459594971142L;

    /**
     * Fixed date constructor.
     *
     * @param fixedDate fixed date.
     */
    public Egyptian(final long fixedDate) {
        super(EPOCH, fixedDate);
    }

    Egyptian(final long epoch, final long fixedDate) {
        super(epoch, fixedDate);
    }

    /**
     * Julian day constructor.
     *
     * @param julianDay Julian day.
     */
    public Egyptian(final double julianDay) {
        super(EPOCH, julianDay);
    }

    Egyptian(final long epoch, final double julianDay) {
        super(epoch, julianDay);
    }

    /**
     * Constructor with the date.
     *
     * @param year Year.
     * @param month Month.
     * @param day Day.
     */
    public Egyptian(final long year, final int month, final int day) {
        super(EPOCH, year, month, day);
    }

    Egyptian(final long epoch, final long year, final int month, final int day) {
        super(epoch, year, month, day);
    }

    /**
     * To fixed date...
     *
     * @param year Year.
     * @param month Month.
     * @param day Day.
     * @return Fixed day.
     */
    @Override
    long toFixed(final long year, final int month, final int day) {
        return (this.epoch + 365L * (year - 1L) + (long) (30 * (month - 1)) + (long) day) - 1L;
    }

    @Override
    long yearFromFixed() {
        return 1L + Calendar.quotient(this.fixed - this.epoch, 365D);
    }

    @Override
    int monthFromFixed(final long year) {
        return (int) (1L + Calendar.quotient(Calendar.mod(this.fixed - this.epoch, 365L), 30D));
    }

    @Override
    int dayFromFixed(final long year, final int month) {
        return (int) ((this.fixed - this.epoch - 365L * (year - 1L) - (long) (30 * (month - 1))) + 1L);
    }
}
