package jparsec.ephem;

public class Test {
    public static void main (final String[] args) {
        for (Target.TARGET target : Target.TARGET.values()) {
            System.out.println ("{ " + target.name() + ", \"" + target.getName() + "\" },");
        }
    }
}
