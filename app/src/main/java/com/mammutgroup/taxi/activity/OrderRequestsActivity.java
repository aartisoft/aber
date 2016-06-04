package com.mammutgroup.taxi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.afollestad.materialcab.MaterialCab;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRequestsActivity extends AppCompatActivity implements OrdersRecyclerAdapter.Callback, MaterialCab.Callback{

    List<Order> orders;
    MaterialCab mCab;
    OrdersRecyclerAdapter orderAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_requests);
        setupOrders();
        setupLayout(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_requests, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupLayout(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrdersRecyclerAdapter(orders,this);
        recyclerView.setAdapter(orderAdapter);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (savedInstanceState != null) {
            mCab = MaterialCab.restoreState(savedInstanceState, this, this);
            orderAdapter.restoreState(savedInstanceState);
        }

    }

    private void setupOrders() {
        orders = new ArrayList<Order>();
        for(int i=0;i<100;i++){
            Order order = new Order();
            order.setSourceAddress(UUID.randomUUID().toString());
            order.setDestinationAddress(UUID.randomUUID().toString());
            orders.add(order);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCab != null && mCab.isActive()) {
            mCab.finish();
            mCab = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCabCreated(MaterialCab materialCab, Menu menu) {
        // Makes the icons in the overflow menu visible
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Field field = menu.getClass().
                        getDeclaredField("mOptionalIconsVisible");
                field.setAccessible(true);
                field.setBoolean(menu, true);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return true; // allow creation
    }

    @Override
    public boolean onCabItemClicked(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.mit_take_passenger:
                //showTakePassengerConfirmationDialog();
                sendTripAcceptedByDriver();
                break;
            case R.id.mit_remove:
                //showItemRemovalConfirmationDialog();
                removeTripItem();
                break;
        }
        return true;
    }

//
//    private void showTakePassengerConfirmationDialog() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(R.string.message_dialog_take_passenger_confirmation)
//                .setCancelable(false)
//                .setPositiveButton("Send",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                sendTripAcceptedByDriver();
//                            }
//                        });
//        alertDialogBuilder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        //TODO
//                    }
//                });
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }

//    private void showItemRemovalConfirmationDialog()
//    {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage(R.string.message_dialog_remove_order_from_list_confirmation)
//                .setCancelable(false)
//                .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //TODO
//                            }
//                        });
//        alertDialogBuilder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        //TODO
//                    }
//                });
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//
//    }

    private void sendTripAcceptedByDriver()
    {


    }

    private void removeTripItem()
    {

    }


    @Override
    public boolean onCabFinished(MaterialCab materialCab) {
        orderAdapter.clearSelected();
        return true; // allow destruction
    }

    @Override
    public void onItemClicked(int index, boolean longClick) {
        if (longClick || (mCab != null && mCab.isActive())) {
            onIconClicked(index);
            return;
        }
        transitToMap(index);
    }

    @Override
    public void onIconClicked(int index) {
        orderAdapter.toggleSelected(index);
        if (orderAdapter.getSelectedCount() == 0) {
            mCab.finish();
            return;
        }
        if (mCab == null)
            mCab = new MaterialCab(this, R.id.cab_stub).start(this);
        else if (!mCab.isActive())
            mCab.reset().start(this);
        //mCab.setTitle(getString(R.string.x_selected, orderAdapter.getSelectedCount()));
    }

    private void transitToMap(int index)
    {
        Order order = orders.get(index);
        if(order != null) {
            Intent intent = new Intent(this, PassengerOrderMapActivity.class);
            order.setSourceCoordinateLat(35.755896);
            order.setSourceCoordinateLong(51.376748);
            order.setDestinationCoordinateLat(35.745448);
            order.setDestinationCoordinateLong(51.393142);
            //todo remove setting lat & long

            intent.putExtra("order",order);
            startActivity(intent);
        }
    }
}
