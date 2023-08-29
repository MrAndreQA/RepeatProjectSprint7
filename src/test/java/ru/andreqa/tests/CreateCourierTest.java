package ru.andreqa.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.andreqa.client.CourierClient;
import ru.andreqa.model.Courier;
import ru.andreqa.model.CourierCredentials;
import ru.andreqa.model.CourierGenerator;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {

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
    public void cleanData(){
        courierClient.delete(courierId);
    }

    @Test
    public void courierCanBeCreatedWithValidDataWithJUnit () {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        assertEquals("Status code is incorrect",201,statusCode);
        assertTrue("Courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertTrue("Courier ID is not created", courierId != 0);
    }

    @Test
    public void courierCanBeCreatedWithValidData2WithRestAssured () {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void courierNotBeCreatedWithoutLogin() {
        Courier courier = CourierGenerator.getRandomWithNullLogin();

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("code", is(400))
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void courierNotBeCreatedWithoutPassword() {
        Courier courier = CourierGenerator.getRandomWithNullPassword();

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("code", is(400))
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void courierNotBeCreatedWithoutFirstName() {
        Courier courier = CourierGenerator.getRandomWithNullFirstName();  // Согласно заданию Практикума: если одного из полей нет, запрос возвращает ошибку; Но по ТЗ(АпиДок - firstName не обязательно!

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    public void createTwoEqualCouriersTest() {
        Courier courier = new Courier("EqualCourier43", "Passw", "ОдинаковыйКурьер");

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.create(courier)
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой.")); // В ApiDoc сообщение = "message": "Этот логин уже используется"
    }
}


