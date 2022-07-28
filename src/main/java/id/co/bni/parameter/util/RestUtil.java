package id.co.bni.parameter.util;

public class RestUtil {
    private RestUtil() {
    }
    public enum RESPONSE_ICONS {
        LACK_OF_FUND("0398", "Saldo rekening tidak mencukupi untuk melakukan transaksi. Info BNI Call 1500046");
        private String code;
        private String description;

        RESPONSE_ICONS(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public String getCode() {
            return this.code;
        }
        public String getDescription() {
            return this.description;
        }
    }

    public static String getErrorMessageIcon(String errCode) {
        String errorMessage = "";
        for (RESPONSE_ICONS response_icons: RESPONSE_ICONS.values())  {
            if (response_icons.getCode().equals(errCode)) errorMessage = response_icons.getDescription();
        }
        return errorMessage;
    }

    public enum RESPONSE_SOA {
        SOA_ERROR("9081", "SOA GENERAL ERROR");
        private String code;
        private String description;

        RESPONSE_SOA(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public String getCode() {
            return this.code;
        }
        public String getDescription() {
            return this.description;
        }
    }

    public static String getErrorMessageSoa(String errCode) {
        String errorMessage = "";
        for (RESPONSE_SOA response_soa: RESPONSE_SOA.values())  {
            if (response_soa.getCode().equals(errCode)) errorMessage = response_soa.getDescription();
        }
        return errorMessage;
    }

    public enum RESPONSE_CONNECTOR {
        TIMEOUT("9080", "CONNECTOR ERROR");
        private String code;
        private String description;

        RESPONSE_CONNECTOR(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public String getCode() {
            return this.code;
        }
        public String getDescription() {
            return this.description;
        }
    }

    public static String getErrorMessageConnector(String errCode) {
        String errorMessage = "";
        for (RESPONSE_CONNECTOR response_connector: RESPONSE_CONNECTOR.values())  {
            if (response_connector.getCode().equals(errCode)) errorMessage = response_connector.getDescription();
        }
        return errorMessage;
    }

    public enum CHANNELS {
        ATM("ATM", "997"),
        NEWIBANK("NEWIBANK", "996"),
        NEWMOBILE("NEWMOBILE", "996"),
        SMS("SMS", "992"),
        BNIDIRECT("BNIDIRECT", "989"),
        API("API", "986"),
        AGEN46("AGEN46", "985"),
        TELLER("TELLER", "999");

        private String name;
        private String code;

        CHANNELS(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return this.code;
        }
    }

    public static String getChannelId(String channelName) {
        String channelId = "";
        for (CHANNELS channels: CHANNELS.values())  {
            if (channels.getName().equals(channelName)) channelId = channels.getCode();
        }
        return channelId;
    }

    public static boolean doValidateDateUsingRegex(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        boolean isValidate = false;
        if (date.matches(regex)) {
            isValidate = true;
        }
        return isValidate;
    }

    public static boolean doValidateTimeUsingRegex(String time) {
        String regex = "\\d{2}:\\d{2}:\\d{2}";
        boolean isValidate = false;
        if (time.matches(regex)) {
            isValidate = true;
        }
        return isValidate;
    }
}
