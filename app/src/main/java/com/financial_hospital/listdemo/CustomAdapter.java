package com.financial_hospital.listdemo;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pradip on 22-06-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<Holder> {



    private List<ContactDetails> details;





    public void updateData(ArrayList<ContactDetails> details) {
        this.details = details;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist, parent, false);
        Holder viewHolder = new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ContactDetails d = details.get(position);


        holder.fNameTxt.setText(d.getFname());
        holder.lNameTxt.setText(d.getLname());
        holder.mobTxt.setText(d.getMobile());


    }

    public CustomAdapter(List<ContactDetails> details) {
        this.details = details;
    }


    @Override
    public int getItemCount() {
        return details.size();
    }



}


