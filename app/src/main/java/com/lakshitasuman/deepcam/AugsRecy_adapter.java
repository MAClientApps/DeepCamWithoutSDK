package com.lakshitasuman.deepcam;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by eNIX on 22-Aug-17.
 */

public class AugsRecy_adapter extends RecyclerView.Adapter<AugsRecy_adapter.Myviewholder> {

    Context context;
    String[] option_menu;
    int selctitem;

    public AugsRecy_adapter(Context context, String[] option_menu, int select) {

        this.context = context;
        this.option_menu = option_menu;
        this.selctitem = select;

    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);
        Myviewholder viewHolder = new Myviewholder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {

        try {
        //    if (position == 0) {
        //        holder.txt.setText("orignal");
        //    } else {
        //        holder.txt.setText("E" + position);
        //    }


            Log.e("selctitem", "" + selctitem);


            Drawable d = Drawable.createFromStream(context.getAssets().open("filter/filter" + position + ".jpg"), null);

            holder.imFilter.setImageDrawable(d);

            holder.imFilter1.setImageResource(R.color.transparentblur);

            if (selctitem == position) {
                holder.imFilter1.setVisibility(View.VISIBLE);
            } else {
                holder.imFilter1.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return option_menu.length;
    }

    public class Myviewholder extends RecyclerView.ViewHolder {

        public CircleImageView imFilter;
        public CircleImageView imFilter1;
        public TextView txt;

        public Myviewholder(View itemView) {
            super(itemView);

            imFilter = (CircleImageView) itemView.findViewById(R.id.image);

            imFilter1 = (CircleImageView) itemView.findViewById(R.id.image1);
            txt = (TextView) itemView.findViewById(R.id.txt);

        }
    }
}
