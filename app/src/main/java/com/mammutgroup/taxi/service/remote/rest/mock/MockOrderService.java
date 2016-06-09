package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.service.remote.rest.api.order.OrderService;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.PriceResponse;

import java.util.Random;
import java.util.UUID;

import retrofit.Callback;
import retrofit.http.Body;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockOrderService implements OrderService {
    @Override
    public void createOrder(Order order, Callback<Order> callback) {
        order.setId(UUID.randomUUID().toString());
        callback.success(order, null);
    }

    @Override
    public void calculatePrice(@Body Order order, Callback<PriceResponse> callback) {
        order.setId(UUID.randomUUID().toString());
        PriceResponse priceResponse = new PriceResponse();
        Random r = new Random();
        double firstVal = Math.round((100 + (200) * r.nextDouble()) * 100);
        double secondVal = Math.round((100 + (200) * r.nextDouble()) * 100);
        priceResponse.setMaxPrice(firstVal);
        priceResponse.setMinPrice(secondVal );
        if (firstVal < secondVal) {
            priceResponse.setMaxPrice(secondVal);
            priceResponse.setMinPrice(firstVal);
        }
        callback.success(priceResponse, null);
    }
}
