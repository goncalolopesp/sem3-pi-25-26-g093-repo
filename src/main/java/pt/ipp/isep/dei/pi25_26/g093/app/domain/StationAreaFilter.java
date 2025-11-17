package pt.ipp.isep.dei.pi25_26.g093.app.domain;
    /**
     * Represents the optional filtering criteria used in USEI08
     * (Search by Geographical Area).
     *
     * This class is intentionally independent of the rest of the system
     * so it can be implemented before USEI06/USEI07.
     *
     * Each field may be:
     *  - a Boolean value (true/false) to apply the filter, or
     *  - null, meaning "no filter" for that field.
     *
     * The "country" filter may be:
     *  - "PT"
     *  - "ES"
     *  - "ALL" (or null → treated as no filter)
     */
    public class StationAreaFilter {

        private final Boolean isCity;
        private final Boolean isMainStation;
        private final String country;

        /**
         * Constructs a filter with the given options.
         *
         * @param isCity          filter only city stations (true), only non-city (false), or no filter (null)
         * @param isMainStation   filter only main stations (true), only non-main (false), or no filter (null)
         * @param country         "PT", "ES", "ALL", or null (null behaves as "ALL")
         */
        public StationAreaFilter(Boolean isCity, Boolean isMainStation, String country) {
            this.isCity = isCity;
            this.isMainStation = isMainStation;
            this.country = (country == null || country.isBlank()) ? "ALL" : country.toUpperCase();
        }

        // Getters
        public Boolean getIsCity() {
            return isCity;
        }

        public Boolean getIsMainStation() {
            return isMainStation;
        }

        public String getCountry() {
            return country;
        }

        @Override
        public String toString() {
            return "StationAreaFilter{" +
                    "isCity=" + isCity +
                    ", isMainStation=" + isMainStation +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
    
