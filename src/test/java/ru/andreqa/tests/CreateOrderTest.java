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
import ru.andreqa.model.Order;
import ru.andreqa.model.OrderGenerator;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    private OrderClientAndre orderClientAndre;
    private int track;
    private int orderId;
    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }
    @Before
    public void setUp(){
        orderClientAndre = new OrderClientAndre();
    }
    @After
    public void cleanData(){

        //orderClientAndre.cancelOrder(track);
        orderId = orderClientAndre.getOrderIdFromTrack(track).extract().path("order.id");
        orderClientAndre.finishOrder(orderId);
    }

    @Test
    @Description("Базовый тест создания заказа")
    public void createOrderTest() {
        Order order = OrderGenerator.getRandom();

        track = orderClientAndre.createOrder(order)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("track", notNullValue())
                .extract().path("track");
    }
}
