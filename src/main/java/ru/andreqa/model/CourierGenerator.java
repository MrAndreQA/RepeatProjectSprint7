package ru.andreqa.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    public static Courier getRandom() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login,password,firstName);
    }

    public static Courier getRandomWithNullLogin() {
        String login = null;
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login,password,firstName);
    }

    public static Courier getRandomWithNullPassword() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = null;
        String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login,password,firstName);

    }

    public static Courier getRandomWithNullFirstName() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = null;
        return new Courier(login,password, firstName);
    }


}
