package jparsec.xml;

import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import java.text.DecimalFormat;

public class JParsecDoubleConverter extends DoubleConverter {
    @Override
    public String toString(Object o) {
        DecimalFormat formater = new DecimalFormat("#.#");
        formater.setMinimumFractionDigits(1);
        formater.setMaximumFractionDigits(5);
        return formater.format(o);
    }
}
