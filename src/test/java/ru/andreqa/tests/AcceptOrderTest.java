package ru.andreqa.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.andreqa.client.CourierClient;
import ru.andreqa.client.OrderClientAndre;
import ru.andreqa.client.base.ScooterRestClient;
import ru.andreqa.model.*;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AcceptOrderTest extends ScooterRestClient {
    private CourierClient courierClient;
    private OrderClientAndre orderClientAndre;
    private int courierId;
    private int track;
    private int orderId;


    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClientAndre = new OrderClientAndre();


        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);

        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");

        Order order = OrderGenerator.getRandom();

        track = orderClientAndre.createOrder(order)
                .extract().path("track");

        orderId = orderClientAndre.getOrderIdFromTrack(track)
                .extract().path("order.id");
    }

    @After //дополни отменой или завершением заказа
    public void cleanData() {
        courierClient.delete(courierId);
        orderClientAndre.cancelOrder(track);
    }

    @Test
    @Description("Accept order - basic positive test")
    public void acceptOrderTest() {
        orderClientAndre.acceptOrder(courierId, orderId)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @Description("Accept order - negative: without courierId")
    public void acceptOrderWithoutCourierIdTest() {
        orderClientAndre.acceptOrderWithoutCourierId(orderId)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));
    }

    @Test
    @Description("Accept order - negative: without orderId")
    @Issue("ОР = 400, Bad Request + messadge Недостаточно данных для поиска ")
    public void acceptOrderWithoutOrderIdTest() {
        orderClientAndre.acceptOrderWithoutOrderId(courierId)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));

    }

    @Test
    @Description("Accept order - negative: with wrong courierId")
    public void acceptOrderWithWrongCourierIdTest() {
        orderClientAndre.acceptOrder(777777, orderId)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Курьера с таким id не существует"));
    }

    @Test
    @Description("Accept order - negative: with wrong orderId")
    public void acceptOrderWithWrongOrderIdTest() {
        orderClientAndre.acceptOrder(courierId, 289312199)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Заказа с таким id не существует"));
    }

    @Test
    @Description("Accept order - negative: when order was accepted earlier")
    public void accepOrderWhenOrderWasAcceptedEarlierTest() {
        orderClientAndre.acceptOrder(courierId,orderId);
        orderClientAndre.acceptOrder(courierId,orderId)
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message",is("Этот заказ уже в работе"));
    }
}