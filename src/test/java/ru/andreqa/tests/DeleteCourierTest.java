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
import ru.andreqa.model.Courier;
import ru.andreqa.model.CourierCredentials;
import ru.andreqa.model.CourierGenerator;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DeleteCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void cleanData() {
        courierClient.delete(courierId);
    }

    @Test
    @Description("Базовый тест удаления курьера")
    public void deleteCourierTest() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);

        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");

        courierClient.delete(courierId)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @Description("Запрос без id возвращает соответствующую ошибку")
    @Issue("Здесь ошибка, т.к. возвращает 404 ошибку вместо 400-й + текст message не соответствует ApiDoc")
    public void deleteCourierWithoutIdTest() {
        courierClient.deleteWithoutId()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для удаления курьера"));
    }

    @Test
    @Description("Проверка, что если отправить запрос с несуществующим id, вернётся ошибка")
    @Issue("В ApiDoc нужно добавить точку после слова \"нет\")")
    public void deleteCourierWithExistingId() {
        courierClient.delete(0)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Курьера с таким id нет."));
    }





    }

