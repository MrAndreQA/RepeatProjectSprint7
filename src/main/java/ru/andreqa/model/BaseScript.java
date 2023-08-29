package ru.andreqa.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BaseScript {
    protected static final String TEST_LOGIN = "CustomLogin";
    protected static final String TEST_PASSWORD = "123456";
    protected static final String TEST_FIRST_NAME = "CustomFirstName";
    protected static final String TEST_LAST_NAME = "CustomLastName";
    protected static final String TEST_ADDRESS = "CustomAddress";
    protected static final String TEST_METRO_STATION = "Черкизовская";
    protected static final String TEST_PHONE = "+123456789";
    protected static final int TEST_RENT_TIME = 1;
    protected static final String TEST_DELIVERY_DATE = LocalDate.now().plusDays(1).toString();
    protected static final String TEST_COMMENT = "CustomComment";
    protected static final List<String> TEST_COLOR_BLACK = List.of("BLACK");
    protected static final List<String> TEST_COLOR_GREY = List.of("GREY");
    protected static final List<String> TEST_COLORS = Arrays.asList("BLACK", "GREY");
    protected static final String INVALID_COURIER_ID = "0";
    protected static final String INVALID_TRACK_ID = "0";
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
}
