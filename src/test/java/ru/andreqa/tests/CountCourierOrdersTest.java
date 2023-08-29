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

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static java.net.HttpURLConnection.*;
import static ru.andreqa.client.CourierClient.*;

public class CountCourierOrdersTest extends ScooterRestClient {
    private CourierClient courierClient;
    private OrderClientAndre orderClientAndre;
    private int courierId;
    private int track;

    private int orderId;
    private int wrongCourierId = 999999999;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }
    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClientAndre = new OrderClientAndre();
    }
    @After
    public void cleanData() {
        courierClient.delete(courierId);
    }

    @Test
    @Description("Проверка, что возвращается ошибка если указан несуществующий courierId")
    @Issue("Текст message не соответствует ОР")
    public void getCountOrdersWithWrongCourierId() {
        given()
                .spec(getBaseReqSpec())
                .when()
                .get(COURIER_URI + wrongCourierId + "/ordersCount")
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message",is("Курьер не найден"));
    }

    @Test
    @Description("Проверка, что возвращается ошибка если не указан courierId")
    @Issue("Текст message не соответствует ОР + Некорректный statusCode")
    public void getCountOrdersWithoutCourierId() {
        given()
                .spec(getBaseReqSpec())
                .when()
                .get(COURIER_URI + "ordersCount")
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message",is("Недостаточно данных для поиска"));
    }

    @Test
    @Description("Базовый тест - получение информации о заказах курьера")
    @Issue("Body и statusCode не соответствует ОР")
    public void getCountOrdersOfCourierTest() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");

        Order order = OrderGenerator.getRandom();

        track = orderClientAndre.createOrder(order)
                .extract().path("track");

        orderId = orderClientAndre.getOrderIdFromTrack(track)
                .extract().path("order.id");

        orderClientAndre.acceptOrder(courierId, orderId);

        given()
                .spec(getBaseReqSpec())
                .when()
                .get(COURIER_URI + courierId + "/ordersCount")
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .assertThat()
                .body("ordersCount",is("1"));
    }


}
