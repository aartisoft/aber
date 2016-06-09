package com.mammutgroup.taxi.service.remote.rest.api.order;

import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.PriceResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface OrderService {

    @POST("/order")
    void createOrder(@Body Order order, Callback<Order> callback);

    @POST("/order/price")
    void calculatePrice(@Body Order order, Callback<PriceResponse> callback);
}
