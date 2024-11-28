import java.nio.charset.StandardCharsets;

public class ResponseFactory {
    public static String createOkResponseString(String content, String content_type) {
        String httpResponse = """
HTTP/1.1 200 OK
Content-Type: %s
Content-Length: %d

%s
""".formatted(content_type, content.getBytes(StandardCharsets.UTF_8).length, content);

        return httpResponse;
    }

    public static String createNotFoundResponseString() {
        String httpResponse = """
HTTP/1.1 404 Not Found
""";
        return httpResponse;
    }

    public static String createOkResponseStringNoBody() {
        return """
HTTP/1.1 200 OK
""";
    }

    public static String createBadResponseErrorJson(String content) {
        content = content.replace("\"", "\\\"");
        content = "{ \"error\": \"" + content + "\" }";

        String httpResponseError = """
HTTP/1.1 400 Bad Request
Content-Type: application/json
Content-Length: %d

%s
""".formatted(content.getBytes(StandardCharsets.UTF_8).length, content);
        return httpResponseError;
    }
}