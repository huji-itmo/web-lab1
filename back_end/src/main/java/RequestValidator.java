import java.io.IOException;

import com.fastcgi.FCGIInterface;

import exceptions.BadRequestBody;
import exceptions.RequestBodyIsEmpty;
import exceptions.RequestBodyTooLong;

import java.nio.charset.StandardCharsets;

public class RequestValidator {
    public static boolean isValidRequest() {
        if (FCGIInterface.request == null || FCGIInterface.request.params == null) {
            String response = ResponseFactory.createBadResponseErrorJson("Invalid request");

            System.out.println(response);
            return false;
        }

        return true;
    }


    public static String getRequestMethod() {
        return FCGIInterface.request.params.getProperty("REQUEST_METHOD");
    }

    public static String getContentType() {
        return FCGIInterface.request.params.getProperty("CONTENT_TYPE");
    }

    public static String getAcceptType() {
        return FCGIInterface.request.params.getProperty("HTTP_ACCEPT");
    }


    public static final String EMPTY_BODY_EXCEPTION_STRING = "The request body is empty! Expected json with x,y,r parameters.";
    public static final int MAX_BODY_LENGTH = 512;

    public static String readBodyUTF8() throws NumberFormatException, BadRequestBody {
        int bodyLength = Integer.parseInt(FCGIInterface.request.params.getProperty("CONTENT_LENGTH"));

        if (bodyLength == 0) {
            throw new RequestBodyIsEmpty(EMPTY_BODY_EXCEPTION_STRING);
        }

        if (bodyLength > MAX_BODY_LENGTH) {
            throw new RequestBodyTooLong("Body should be less than " + MAX_BODY_LENGTH + " bytes long.");
        }

        byte[] bytes = new byte[bodyLength];

        try {
            if (System.in.read(bytes, 0, bodyLength) == -1) {
                throw new RequestBodyIsEmpty(EMPTY_BODY_EXCEPTION_STRING);
            }
        } catch (IOException e) {
            throw new BadRequestBody(e.getMessage());
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
