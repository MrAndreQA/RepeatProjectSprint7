package ru.andreqa.tests;

import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.andreqa.client.OrderClientAndre;
import ru.andreqa.client.base.ScooterRestClient;
import ru.andreqa.model.Order;
import ru.andreqa.model.OrderGenerator;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;
import static ru.andreqa.client.OrderClientAndre.ORDER_URI;

public class OrderListTest extends ScooterRestClient {
private OrderClientAndre orderClientAndre;
private int track;

    @BeforeClass
    public static void globalSetUp() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
}

    @Before
    public void setUp(){
    orderClientAndre = new OrderClientAndre();
}

    @Test
    @Description("Базовый тест - получение списка всех заказов")
    public void getAllOrdersTest() {
    orderClientAndre.getAllOrders()
            .assertThat()
            .statusCode(HTTP_OK)
            .assertThat()
            .body(notNullValue());
    }

    @Test
    @Description("Базовый тест - получение заказа по его номеру.")
    public void  getOrderFromTrackTest() {
        Order order = OrderGenerator.getRandom();

        track = orderClientAndre.createOrder(order)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");

        orderClientAndre.getOrderIdFromTrack(track)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .assertThat()
                .body("order.firstName", equalTo(order.getFirstName()));
        // Проверка, что возвращается объект: сопоставляем запрос с классом Order используя ObjectMapper (jackson)
    }

    @Test
    @Description("Проверка - запрос без номера заказа возвращает ошибку")
    public void getOrderWithoutTrackTest() {
        given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "track")
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для поиска"));
    }

    @Test
    @Description("Проверка - запрос с несуществующим заказом возвращает ошибку")
    public void getOrderWithInvalidTrackTest() {
        given()
                .spec(getBaseReqSpec())
                .queryParam("t", 999999999)
                .when()
                .get(ORDER_URI + "track")
                .then()
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Заказ не найден"));
    }
}
