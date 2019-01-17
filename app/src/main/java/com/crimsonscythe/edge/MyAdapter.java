package com.crimsonscythe.edge;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crimsonscythe.edge.helper.ItemTouchHelperAdapter;
import com.crimsonscythe.edge.helper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    public CardView[] mDataset;
//    public List<CardView> mDataset;
    public String[] names;
    public Drawable[] imgs;
    public String[] tags;
    public OnStartDragListener dragListenerhere;
    public RecyclerView.LayoutManager layoutManagers;




    @Override
    public void onItemDismiss(int position) {

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ImageView imageView;
        public CardView cardView;



        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txtView);
            imageView = view.findViewById(R.id.imgView);
            cardView = view.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(CardView[] myDataset, String[] name, Drawable[] img, OnStartDragListener dragListener, String[] tag, RecyclerView.LayoutManager layoutManager) {
        mDataset = myDataset;
        names = name;
        imgs= img;
        dragListenerhere = dragListener;
        tags = tag;
        layoutManagers=layoutManager;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basic_card, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        holder.textView.setText(names[position]);
        holder.imageView.setImageDrawable(imgs[position]);
        holder.cardView.setTag(tags[position]);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragListenerhere.onStartDrag(holder);
                return false;
            }
        });


    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        mDataset[fromPosition] = mDataset[toPosition];
        mDataset[toPosition] = mDataset[fromPosition];

        notifyItemMoved(fromPosition, toPosition);

//        Log.i("fous", ""+layoutManagers.findViewByPosition(fromPosition).getTag());

        return false;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
