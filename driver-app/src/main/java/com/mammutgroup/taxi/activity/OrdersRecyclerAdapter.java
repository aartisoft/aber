package com.mammutgroup.taxi.activity;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mammutgroup.taxi.commons.service.remote.rest.api.order.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.TripRequestViewHolder>
        implements View.OnClickListener, View.OnLongClickListener{

    private final List<Order> orders;
    private ArrayList<Integer> mSelected;
    private Callback mCallback;


    public OrdersRecyclerAdapter(List<Order> orders,Callback callback) {
        this.orders = orders;
        mSelected = new ArrayList<>();
        mCallback = callback;
    }

    @Override
    public TripRequestViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_request_item_view, parent, false);

        return new TripRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TripRequestViewHolder viewHolder, final int position) {
        final Order order = orders.get(position);
        viewHolder.rootView.setActivated(mSelected.contains(position));
        viewHolder.rootView.setTag("item:" + position);
        viewHolder.rootView.setOnClickListener(this);
        viewHolder.rootView.setOnLongClickListener(this);
        viewHolder.imageView.setTag("icon:" + position);
        viewHolder.imageView.setOnClickListener(this);

        viewHolder.originView.setText(order.getSourceAddress());
        viewHolder.destinationView.setText(order.getDestinationAddress());

        //todo update profile image

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void restoreState(Bundle savedInstanceState) {

    }
    public void toggleSelected(int index) {
        final boolean newState = !mSelected.contains(index);
        if (newState && getSelectedCount() == 0)
            mSelected.add(index);
        else
            mSelected.remove((Integer) index);
        notifyItemChanged(index);
    }

    public int getSelectedCount() {
        return mSelected.size();
    }


    public void clearSelected() {
        mSelected.clear();
        notifyDataSetChanged();
    }

    public Order getSelectedOrder()
    {
        if(getSelectedCount() == 1)
            return orders.get(mSelected.get(0));
        return null;
    }

    @Override
    public void onClick(View v) {
        String[] tag = ((String) v.getTag()).split(":");
        int index = Integer.parseInt(tag[1]);
        if (tag[0].equals("icon")) {
            if (mCallback != null)
                mCallback.onIconClicked(index);
        } else if (mCallback != null) {
            mCallback.onItemClicked(index, false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        String[] tag = ((String) v.getTag()).split(":");
        int index = Integer.parseInt(tag[1]);
        if (mCallback != null)
            mCallback.onItemClicked(index, true);
        return false;
    }

    public class TripRequestViewHolder extends RecyclerView.ViewHolder {

        public TextView originView;
        public TextView destinationView;
        public ImageView imageView;
        public View rootView;

        public TripRequestViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            originView = (TextView)rootView.findViewById(R.id.trip_origin_address);
            destinationView = (TextView)rootView.findViewById(R.id.trip_destination_address);
            imageView = (ImageView)rootView.findViewById(R.id.passenger_profile_picture);
        }
    }

    public interface Callback {
        void onItemClicked(int index, boolean longClick);

        void onIconClicked(int index);
    }
}
