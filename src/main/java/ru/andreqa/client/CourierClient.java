package ru.andreqa.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.andreqa.client.base.ScooterRestClient;
import ru.andreqa.model.Courier;
import ru.andreqa.model.CourierCredentials;
import static io.restassured.RestAssured.given;


public class CourierClient extends ScooterRestClient {
public static final String COURIER_URI = BASE_URI + "courier/";

@Step("Create courier {courier}")
    public ValidatableResponse create (Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Login as {courierCredentials}")
    public ValidatableResponse login (CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_URI +"login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete (int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }

    @Step("Login as wrongPassword")
    public ValidatableResponse loginWrongPasswordOrLogin (Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI +"login/")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse deleteWithoutId () {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI)
                .then();
    }
}