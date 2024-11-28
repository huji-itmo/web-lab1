
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.atomic.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fastcgi.FCGIInterface;

import exceptions.BadRequestBody;

public class Main {

    public static void main(String[] args) {

        var fcgiInterface = new FCGIInterface();
        List<HitData> hitHistory = new ArrayList<>();
        
        while (fcgiInterface.FCGIaccept() >= 0) {
            try {

                handleRequest(hitHistory);

            } catch(Exception e) {
                String response = ResponseFactory.createBadResponseErrorJson(e.getMessage());
                System.out.println(response);
            }
        }
    }

    private static void handleRequest(List<HitData> hitHistory) {
        String uri = FCGIInterface.request.params.getProperty("REQUEST_URI");

        if (uri == null) {
            uri = "/fcgi-bin/lab1.jar";
        }

        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (!"/fcgi-bin/lab1.jar".equals(uri)) {
            System.out.println(ResponseFactory.createNotFoundResponseString());
            return;
        }

        if (!RequestValidator.isValidRequest()) {
            return;
        }

        switch (RequestValidator.getRequestMethod()) {
            case "GET":
                sendHitTable(hitHistory);
                break;

            case "POST":
                addHitDataToHitTable(hitHistory);
                break;
            default:
                wrongRequestMethod();
        }
    }

    private static void wrongRequestMethod() {
        String errString = ResponseFactory.createBadResponseErrorJson("Wrong request method: use GET or POST");
        System.out.println(errString);
    }

    private static void addHitDataToHitTable(List<HitData> hitHistory) {
        long startTime = System.nanoTime();

        String json = "";

        try {
            json = RequestValidator.readBodyUTF8();
        } catch(NumberFormatException e) {
            String errString = ResponseFactory.createBadResponseErrorJson("Bad request. Content length is not a number.");
            System.out.println(errString);
            return;
        } catch(BadRequestBody e) {
            System.out.println(ResponseFactory.createBadResponseErrorJson(e.getMessage()));
            return;
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Double.class, new SmartDouble())
            .setPrettyPrinting()
            .create();
        RequestData requestData;

        try {
            requestData = gson.fromJson(json, RequestData.class);
            requestData.throwIfBadData();
        } catch(Exception e) {
            System.out.println(ResponseFactory.createBadResponseErrorJson(e.getMessage()));
            return;
        }

        boolean hitResult = CoordinateSpace.testHit(requestData);

        long durationNanoSeconds = System.nanoTime() - startTime;

        HitData hitData = new HitData(requestData, hitResult, durationNanoSeconds);

        sendHitResult(hitData);

        hitHistory.add(hitData);
    }

    public static void sendHitResult(HitData data) {
        String accept = Objects.requireNonNullElse(RequestValidator.getAcceptType(), "application/json") ;

        String responseString = "";

        switch (accept) {

            case "text/html":
                responseString = data.toHTMLTable();
                break;
            default:
                responseString = "{" + data.toJsonFields() + "}";
                break;
        }

        System.out.println(ResponseFactory.createOkResponseString(responseString, accept));
    }

    public static void sendHitTable(List<HitData> hitHistory) {

        String accept = Objects.requireNonNullElse(RequestValidator.getAcceptType(), "application/json") ;

        switch (accept) {
            case "text/html":
                String html = hitHistory.stream()
                .map(data -> data.toHTMLTable())
                .collect(Collectors.joining("\n"));

                String responseHTML = ResponseFactory.createOkResponseString(html, "text/html");
                System.out.println(responseHTML);
                break;

            default:
                AtomicInteger counter = new AtomicInteger(0);

                String json = hitHistory.stream()
                    .map(data -> "\"%d\": {%s}\n".formatted(counter.getAndIncrement(), data.toJsonFields()))
                    .collect(Collectors.joining(", ", "{", "}"));

                String responseJSON = ResponseFactory.createOkResponseString(json, "application/json");
                System.out.println(responseJSON);
                break;
        }
    }
}