package com.mammutgroup.taxi.service.remote.rest.api.order;

import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface OrderService {

    @POST("/order")
    void postOrder(@Body Order order, Callback<Order> callback);
}
