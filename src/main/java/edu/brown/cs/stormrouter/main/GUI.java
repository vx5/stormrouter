package edu.brown.cs.stormrouter.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;


final class GUI {
  private static final Gson GSON = new Gson();

  private GUI() {
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  static void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/stormrouter", new FrontHandler(), freeMarker);
    Spark.get("/stormrouter/demo", new DemoHandler(), freeMarker);
    Spark.post("/stormrouter/route", new RouteHandler());
    Spark.post("/stormrouter/parse", new ParserHandler());
  }

  static void stopSparkServer() {
    Spark.stop();
  }

  /**
   * Handles requests to the front page of our StormRouter website.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables =
          ImmutableMap.of("title", "StormRouter");
      return new ModelAndView(variables, "front-page.ftl");
    }
  }

  /**
   * Handles requests to the front page of our StormRouter website.
   */
  private static class DemoHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables =
          ImmutableMap.of("title", "StormRouter");
      return new ModelAndView(variables, "demo.ftl");
    }
  }

  private class RouteWaypoint {
    double[] waypoint;
    int duration;

    @Override
    public String toString() {
      return "RouteWaypoint{" +
          "waypoint=" + Arrays.toString(waypoint) +
          ", duration=" + duration +
          '}';
    }
  }

  private class RouteRequest {
    double[] start;
    long date;
    double[] destination;
    RouteWaypoint[] waypoints;
  }

  /**
   * Handles requests to '/stormrouter/parse/'.
   */
  private static class ParserHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      /*JsonParser parser = new JsonParser();
      JsonElement params = parser.parse(qm.value("params"));*/
      RouteRequest params =
          GSON.fromJson(qm.value("params"), RouteRequest.class);
      System.out.println(Arrays.toString(params.start));
      System.out.println(params.date);
      System.out.println(Arrays.toString(params.waypoints));
      System.out.println(Arrays.toString(params.destination));
      return "{}";
    }
  }
  
  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
