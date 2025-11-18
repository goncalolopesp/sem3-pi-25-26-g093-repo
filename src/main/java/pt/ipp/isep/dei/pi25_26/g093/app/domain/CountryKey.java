package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class CountryKey implements Comparable<CountryKey> {

    private final CompleteStation s;

    public CountryKey(CompleteStation s) { this.s = s; }

    public String getCountry() { return s.getCountry(); }
    public CompleteStation getStation() { return s; }

    @Override
    public int compareTo(CountryKey o) {
        int cmp = getCountry().compareToIgnoreCase(o.getCountry());
        if (cmp != 0) return cmp;

        return s.getStation().compareToIgnoreCase(o.s.getStation());
    }
}
