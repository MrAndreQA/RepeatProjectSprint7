package ru.andreqa.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.andreqa.client.base.ScooterRestClient;
import ru.andreqa.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClientAndre extends ScooterRestClient {
    public static final String ORDER_URI = BASE_URI + "orders/";

    @Step("Create order {Order}")
    public ValidatableResponse createOrder(Order order) {
     return given()
             .spec(getBaseReqSpec())
             .body(order)
             .when()
             .post(ORDER_URI)
             .then();
    }
    @Step("Cancel order {id}")                       // Отмена заказа не работает. Полезный опыт на будущее (передача в body значений, когда при Validatable Response нельзя использовать метод .as(class)
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("track", track)
                .when()
                .put(ORDER_URI + "cancel/")
                .then();
    }

    @Step("Finish order {id}")
    public ValidatableResponse finishOrder(int orderId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .put(ORDER_URI + "finish/" + orderId)
                .then();
    }

    @Step("Get All Orders")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI)
                .then();
    }

    @Step("Accept order{courierId, orderId}")
    public ValidatableResponse acceptOrder(Integer courierId, int orderId) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_URI +"accept/" + orderId)
                .then();
    }

    @Step("Get order in track number {track}")
    public ValidatableResponse getOrderIdFromTrack(int track) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("t", track)
                .when()
                .get(ORDER_URI + "track/")
                .then();
    }

    @Step("Accept order without courierId {orderId}")
    public ValidatableResponse acceptOrderWithoutCourierId(int orderId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .put(ORDER_URI +"accept/" + orderId)
                .then();
    }

    @Step("Accept order {courierId}")
    public ValidatableResponse acceptOrderWithoutOrderId(Integer courierId) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_URI +"accept/")
                .then();
    }


}
