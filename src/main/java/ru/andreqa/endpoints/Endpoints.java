package ru.andreqa.endpoints;

public class Endpoints {

    public static final String ORDERS = "https://qa-scooter.praktikum-services.ru/api/v1/orders";
    public static final String CANCEL_ORDER = "https://qa-scooter.praktikum-services.ru/api/v1/orders/cancel";



    // Не используемые

    public String generalURI = "qa-scooter.praktikum-services.ru";

    // Эндпоинты для курьера
    public String createCourier = "/api/v1/courier";
    public String loginCourier = "/api/v1/courier/login";
    public String deleteCourier = "/api/v1/courier/:id";

    // Эндпоинт для получения списка заказов курьера
    public String orderCount = "/api/v1/courier/:id/ordersCount";

    // Эндпоинт для заказов
    public String orderFinish = "/api/v1/orders/finish/:id";
    public String orderCancel = "/api/v1/orders/cancel";
    public String orders = "/api/v1/orders";
    public String orderTrack = "/api/v1/orders/track";
    public String orderAccept = "/api/v1/orders/accept/:id";
    public String orderCreate = "/api/v1/orders";

    // Эндпоинты для утилит
    public String utilSearchStation = "/api/v1/stations/search";

}