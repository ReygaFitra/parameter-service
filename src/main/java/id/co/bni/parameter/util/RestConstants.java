
package id.co.bni.parameter.util;

public class RestConstants {

    public enum RESPONSE {
        APPROVED("00", "Approved"),
        HTTP_NO_PATH("X4", "There is No Resource Path"),
        HTTP_INTERNAL_ERROR("X5", "Service Internal Error"),
        INVALID_TRANSACTION("12", "Invalid Transaction"),
        INVALID_AMOUNT("13", "Invalid Amount"),
        DATA_NOT_FOUND("404", "Data not found"),
        DATA_BLOCKED("15", "Data blocked or inactive"),
        DATA_ALREADY_EXIST("16", "Data already axis"),
        WRONG_FORMAT_DATA("30", "Incorrect data format"),
        ERROR_LOGIN("31", "User not registered or wrong password"),
        TIMEOUT("68", "Timeout"),
        ALREADY_PAID("88", "The bill has been paid"),
        CUT_OFF_TIME("90", "Cut Off Time"),
        DATABASE_ERROR("95", "The database is having problems"),
        MAINTENANCE("96", "System Maintenance"),
        GENERAL_ERROR("98", "General Error"),
        USER_NOT_FOUND("71", "User not found"),
        USER_DISABLE("72", "User blocked or inactive"),
        PASSWORD_FAILED("73", "Password incorrect"),
        ;
        private final String code;
        private final String description;

        RESPONSE(String code, String description) {
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

    public interface HEADER_NAME {
        String REQUEST_ID = "X-Request-ID";
        String CORRELATION_ID = "X-Correlation-ID";
    }

    public interface CACHE_NAME {
        String GATEWAY_PARAMETER = "gateway-parameter";
        String MCP_PARAMETER = "mcp-parameter";
        String CHANNEL_PARAMETER = "channel-parameter";
        String KEY_PARAMETER = "key-parameter";
        String ACCOUNT_MANAGEMENT = "account-management";
    }
}
