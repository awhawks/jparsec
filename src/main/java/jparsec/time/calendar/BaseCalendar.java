package jparsec.time.calendar;

import java.io.Serializable;
import org.math.plot.plotObjects.Base;

/**
 * Created by carlo on 03.11.15.
 */
public abstract class BaseCalendar implements Serializable {
    protected final long year;
    protected final int month;
    protected final int day;
    protected boolean leapMonth;
    protected final long epoch;
    protected final long fixed;
    protected final double julianDate;

    BaseCalendar(final long epoch, final long year, final int month, final int day) {
        this.epoch = epoch;
        this.year = year;
        this.month = month;
        this.day = day;
        this.fixed = toFixed(year, month, day);
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

    abstract long toFixed(final long year, final int month, final int day);

    double toJulian (final long fixed) {
        return fixed + Gregorian.EPOCH + 0.5D;
    }

    abstract long yearFromFixed();

    abstract int monthFromFixed(final long year);

    abstract int dayFromFixed(final long year, final int month);

    public int getDayOfWeek() {
        return -1;
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
