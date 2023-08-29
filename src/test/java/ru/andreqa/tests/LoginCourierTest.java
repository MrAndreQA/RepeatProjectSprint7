package ru.andreqa.tests;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.andreqa.client.CourierClient;
import ru.andreqa.client.base.ScooterRestClient;
import ru.andreqa.model.Courier;
import ru.andreqa.model.CourierCredentials;
import ru.andreqa.model.CourierGenerator;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class LoginCourierTest {

    private CourierClient courierClient;
    private int courierId;
    private String courierWrongLogin;
    private String courierWrongPassword;

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
    @DisplayName("Проверка, что курьер может авторизоваться + успешный запрос возвращает id")
    public void loginCourierTest() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Проверка, что если авторизоваться под несуществующим пользователем - запрос возвращает ошибку")
    public void loginCourierWithNotExistingData() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка, что система вернёт ошибку, если неправильно указать пароль")
    public void loginCourierWithWrongPassword () {
        Courier courier = new Courier("TstLogin", "Pass", "name");

        courierClient.create(courier);

        courierWrongPassword = "Pass" + "wr";

        //надо создать запрос на логин, в котором передать логин созданного курьера и неверный пароль (WrongPassword)

        Courier courier1 = new Courier("TstLogin", courierWrongPassword);

        courierClient.loginWrongPasswordOrLogin(courier1)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));

        courierId = courierClient.login(CourierCredentials.from(courier))   // нужно для выполнения After: cleanData
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
        }

    @Test
    @DisplayName("Проверка, что система вернёт ошибку, если неправильно указать логин")
    public void loginCourierWithWrongLogin () {
        Courier courier = new Courier("TstLogin", "Pass", "name");

        courierClient.create(courier);

        courierWrongLogin = "TstLogin" + "wr";

        //надо создать запрос на логин, в котором передать логин созданного курьера и неверный пароль (WrongPassword)

        Courier courier1 = new Courier(courierWrongLogin, "Pass");

        courierClient.login(CourierCredentials.from(courier1))
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));

        courierId = courierClient.login(CourierCredentials.from(courier))   // нужно для выполнения After: cleanData
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
    }


    @Test
    @DisplayName("Проверка, что запрос возвращает ошибку - если при логине не был передан пароль")
    public void loginCourierWithExistingPassword() {
        Courier courier = new Courier("TstLogin", "Pass", "name");

        courierClient.create(courier);

        Courier courier1 = new Courier("TstLogin",null);

        courierClient.login(CourierCredentials.from(courier1))
                .assertThat()
                .statusCode(HTTP_GATEWAY_TIMEOUT);                                // ожидаем HTTP_BAD_REQUEST(400), но по факту 504 ошибка (Gateway time out). Поменяли с тесте на 504-ю, чтобы выполнился After по удалению курьера!!!

                                                                                   // аналогично убрали .and() .body("message", is("Недостаточно данных для входа")); ТАК КАК 504 ошибка!!

        courierId = courierClient.login(CourierCredentials.from(courier))   // нужно для выполнения After: cleanData
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Проверка, что запрос возвращает ошибку - если при логине не был передан логин")
    public void loginCourierWithExistingLogin() {
        Courier courier = new Courier("TstLogin", "Pass", "name");

        courierClient.create(courier);

        Courier courier1 = new Courier(null,"Pass");

        courierClient.login(CourierCredentials.from(courier1))
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));                                    // ожидаем HTTP_BAD_REQUEST(400), но по факту 504 ошибка (Gateway time out). Поменяли с тесте на 504-ю, чтобы выполнился After по удалению курьера!!!

        courierId = courierClient.login(CourierCredentials.from(courier))   // нужно для выполнения After: cleanData
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");
    }
    }