package ru.andreqa.client.base;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.andreqa.model.Order;

import static io.restassured.RestAssured.given;
import static ru.andreqa.endpoints.Endpoints.*;

public class OrderStepsWrite {

    @Step("Create order")
    public static Response createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract()
                .response();
    }

    @Step("Get orders list")
    public static Response getOrders() {
        return given()
                .when()
                .get(ORDERS)
                .then()
                .extract()
                .response();
    }

    @Step("Cancel order")
    public static void cancelOrder(String track) {
        given()
                .queryParam("track", track)
                .when()
                .put(CANCEL_ORDER);
    }
}
