package nextstep.member.infrastructure;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class AuthExtractor {
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN_TYPE = AuthExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    private static final String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (isBearerHeader(header)) {
            return getAuthHeaderValue(request, header);
        }
        return null;
    }

    private static String getAuthHeaderValue(HttpServletRequest request, String headerValue) {
        String authHeaderValue = headerValue.substring(BEARER_TYPE.length()).trim();
        request.setAttribute(ACCESS_TOKEN_TYPE, headerValue.substring(0, BEARER_TYPE.length()).trim());
        return substringBeforeComma(authHeaderValue);
    }

    private static String substringBeforeComma(String authHeaderValue) {
        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
            return authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    private static boolean isBearerHeader(String headerValue) {
        return headerValue.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }
}