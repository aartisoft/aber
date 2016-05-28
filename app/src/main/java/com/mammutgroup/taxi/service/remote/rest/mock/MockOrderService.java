package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.service.remote.rest.api.order.OrderService;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import retrofit.Callback;
import retrofit.http.Body;

import java.util.UUID;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockOrderService implements OrderService {
    @Override
    public void postOrder(Order order, Callback<Order> callback) {
        order.setId(UUID.randomUUID().toString());
        callback.success(order,null);
    }
}
