package com.github.useful_solutions;

import com.github.useful_solutions.api.APIRequest;
import com.github.useful_solutions.api.APIRequestImpl;
import com.github.useful_solutions.api.record.pojo.*;
import com.github.useful_solutions.api.record.request.FindShortestPathRequest;
import com.github.useful_solutions.api.record.response.GetFirstArrivalToStopResponse;
import com.github.useful_solutions.classifier.ClassifierRequest;
import com.github.useful_solutions.classifier.pojo.FullStop;
import com.github.useful_solutions.api.record.pojo.ArrivalTransport;
import com.github.useful_solutions.api.record.pojo.GeoPoint;
import com.github.useful_solutions.api.record.pojo.Link;
import com.github.useful_solutions.api.record.pojo.Message;
import com.github.useful_solutions.classifier.ClassifierRequestImpl;
import com.github.useful_solutions.classifier.pojo.Route;
import com.github.useful_solutions.classifier.pojo.RouteWithStops;
import com.github.useful_solutions.classifier.pojo.Stop;
import com.github.useful_solutions.exception.APIResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class APITest {

    private static final APIRequest API_REQUEST = new APIRequestImpl();
    private static final ClassifierRequest CLASSIFIER_REQUEST = new ClassifierRequestImpl();
    private static final Random RANDOM = new Random();
    private static List<Stop> stops;
    private static List<FullStop> fullStops;
    private static List<RouteWithStops> routesWithStops;
    private static List<Route> routes;
    private static final double SAMARA_LATITUDE = 53.215603;
    private static final double SAMARA_LONGITUDE = 50.148011;

    @BeforeAll
    static void initializeClassifiers() {
        try {
            stops = CLASSIFIER_REQUEST.getStops();
            fullStops = CLASSIFIER_REQUEST.getFullStops();
            routesWithStops = CLASSIFIER_REQUEST.getRoutesWithStops();
            routes = CLASSIFIER_REQUEST.getRoutes();
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @RepeatedTest(10)
    void runAllRandomTests() {
        getFirstArrivalToStopRandomTest();
        findShortestPathRandomTest();
        getRouteArrivalToStopRandomTest();
        getRouteScheduleRandomTest();
        getTransportPositionRandomTest();
        getSurroundingTransportsRandomTest();
        getTransportsOnRouteRandomTest();
        getNearestBuildingTest();
        findBuildingByAddressTest();
        getUserMessagesTest();
        voteForMessageTest();
        sendUserMessageTest();
    }

    @Test
    void runAllFullTests() {
        getFirstArrivalToStopFullTest();
        findShortestPathFullTest();
        getRouteArrivalToStopFullTest();
        getRouteScheduleAllTest();
        getTransportPositionFullTest();
        getSurroundingTransportsFullTest();
        getTransportsOnRouteFullTest();
        getNearestBuildingTest();
        findBuildingByAddressTest();
        getUserMessagesTest();
        voteForMessageTest();
        sendUserMessageTest();
    }

    @Test
    void getFirstArrivalToStopFullTest() {
        try {
            int total = stops.size();
            AtomicInteger current = new AtomicInteger(1);
            stops.forEach(stop -> {
                try {
                    System.out.println("getFirstArrivalToStopTest: " + current.get() + "/" + total);
                    API_REQUEST.getFirstArrivalToStop(stop.ksId, Integer.MAX_VALUE);
                    current.incrementAndGet();
                } catch (APIResponseException | IOException e) {
                    Assertions.fail(e);
                }
            });
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getFirstArrivalToStopRandomTest() {
        try {
            Stop stop = stops.get(RANDOM.nextInt(stops.size()));
            API_REQUEST.getFirstArrivalToStop(stop.ksId, Integer.MAX_VALUE);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void findShortestPathFullTest() {
        try {
            long total = fullStops.size() * fullStops.size();
            int current = 1;
            for (FullStop fullStop1 : fullStops) {
                for (FullStop fullStop2 : fullStops) {
                    System.out.println("findShortestPathTest: " + current + "/" + total);
                    GeoPoint geoPoint1 = new GeoPoint(fullStop1.latitude, fullStop1.longitude);
                    GeoPoint geoPoint2 = new GeoPoint(fullStop2.latitude, fullStop2.longitude);
                    API_REQUEST.findShortestPath(geoPoint1, geoPoint2, FindShortestPathRequest.Criterion.time, TransportType.bus);
                    current++;
                }
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void findShortestPathRandomTest() {
        try {
            FullStop fullStop1 = fullStops.get(RANDOM.nextInt(fullStops.size()));
            FullStop fullStop2 = fullStops.get(RANDOM.nextInt(fullStops.size()));
            GeoPoint geoPoint1 = new GeoPoint(fullStop1.latitude, fullStop1.longitude);
            GeoPoint geoPoint2 = new GeoPoint(fullStop2.latitude, fullStop2.longitude);
            API_REQUEST.findShortestPath(geoPoint1, geoPoint2, FindShortestPathRequest.Criterion.time, TransportType.bus);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getRouteArrivalToStopFullTest() {
        try {
            long total = routesWithStops.stream().mapToInt(value -> value.stops.size()).asLongStream().sum();
            int current = 1;
            for (RouteWithStops routeWithStops : routesWithStops) {
                List<RouteWithStops.Stop> stops = routeWithStops.stops;
                for (RouteWithStops.Stop stop : stops) {
                    System.out.println("getRouteArrivalToStopTest: " + current + "/" + total);
                    API_REQUEST.getRouteArrivalToStop(stop.ksId, routeWithStops.krId);
                    current++;
                }
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getRouteArrivalToStopRandomTest() {
        try {
            RouteWithStops routeWithStops = routesWithStops.get(RANDOM.nextInt(routesWithStops.size()));
            RouteWithStops.Stop stop = routeWithStops.stops.get(RANDOM.nextInt(routeWithStops.stops.size()));
            API_REQUEST.getRouteArrivalToStop(stop.ksId, routeWithStops.krId);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getRouteScheduleAllTest() {
        try {
            int total = routes.size();
            AtomicInteger current = new AtomicInteger(1);
            String day = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            routes.forEach(route -> {
                try {
                    System.out.println("getRouteScheduleTest: " + current.get() + "/" + total);
                    API_REQUEST.getRouteSchedule(route.krId, day);
                    current.incrementAndGet();
                } catch (APIResponseException | IOException e) {
                    Assertions.fail(e);
                }
            });
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getRouteScheduleRandomTest() {
        try {
            Route route = routes.get(RANDOM.nextInt(routes.size()));
            String day = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            API_REQUEST.getRouteSchedule(route.krId, day);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getTransportPositionRandomTest() {
        try {
            Stop stop = stops.get(RANDOM.nextInt(stops.size()));
            GetFirstArrivalToStopResponse response = API_REQUEST.getFirstArrivalToStop(stop.ksId, Integer.MAX_VALUE);
            while (response.arrivalTransports.isEmpty()) {
                stop = stops.get(RANDOM.nextInt(stops.size()));
                response = API_REQUEST.getFirstArrivalToStop(stop.ksId, Integer.MAX_VALUE);
            }
            ArrivalTransport arrivalTransport = response.arrivalTransports.get(RANDOM.nextInt(response.arrivalTransports.size()));
            API_REQUEST.getTransportPosition(arrivalTransport.hullNo);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getTransportPositionFullTest() {
        try {
            int current = 1;
            for (Stop stop : stops) {
                GetFirstArrivalToStopResponse response = API_REQUEST.getFirstArrivalToStop(stop.ksId, Integer.MAX_VALUE);
                for (ArrivalTransport arrivalTransport : response.arrivalTransports) {
                    System.out.println("getTransportPositionTest: " + current);
                    API_REQUEST.getTransportPosition(arrivalTransport.hullNo);
                    current++;
                }
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getSurroundingTransportsRandomTest() {
        try {
            FullStop fullStop = fullStops.get(RANDOM.nextInt(fullStops.size()));
            GeoPoint geoPoint = new GeoPoint(fullStop.latitude, fullStop.longitude);
            API_REQUEST.getSurroundingTransports(geoPoint, 10_000.5D, Integer.MAX_VALUE);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getSurroundingTransportsFullTest() {
        try {
            int total = fullStops.size();
            int current = 1;
            for (FullStop fullStop : fullStops) {
                System.out.println("getSurroundingTransportsTest: " + current + "/" + total);
                GeoPoint geoPoint = new GeoPoint(fullStop.latitude, fullStop.longitude);
                API_REQUEST.getSurroundingTransports(geoPoint, 1_000.5D, Integer.MAX_VALUE);
                current++;
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getTransportsOnRouteRandomTest() {
        try {
            Route route = routes.get(RANDOM.nextInt(routes.size()));
            API_REQUEST.getTransportsOnRoute(route.krId, Integer.MAX_VALUE);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getTransportsOnRouteFullTest() {
        try {
            int total = routes.size();
            int current = 1;
            for (Route route : routes) {
                System.out.println("getTransportsOnRouteTest: " + current + "/" + total);
                API_REQUEST.getTransportsOnRoute(route.krId, Integer.MAX_VALUE);
                current++;
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getNearestBuildingTest() {
        try {
            GeoPoint samara = new GeoPoint(SAMARA_LATITUDE, SAMARA_LONGITUDE);
            API_REQUEST.getNearestBuilding(samara, 50.5D, 10);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void findBuildingByAddressTest() {
        try {
            API_REQUEST.findBuildingByAddress(null, "ТЦ Пирамида", 10);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getUserMessagesTest() {
        try {
            GeoPoint samara = new GeoPoint(SAMARA_LATITUDE, SAMARA_LONGITUDE);
            API_REQUEST.getUserMessages(samara, 100_000.5D, "test");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void voteForMessageTest() {
        try {
            GeoPoint samara = new GeoPoint(SAMARA_LATITUDE, SAMARA_LONGITUDE);
            API_REQUEST.voteForMessage(2045352, Message.Vote.confirm, samara, "test");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void sendUserMessageTest() {
        try {
            List<Link> links = Collections.singletonList(new Link(SAMARA_LATITUDE, SAMARA_LONGITUDE, 200.5D));
            API_REQUEST.sendUserMessage("Тестовое сообщение.", null, null, links, 1, "test");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

}