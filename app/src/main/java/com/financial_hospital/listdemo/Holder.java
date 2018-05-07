package com.financial_hospital.listdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pradip on 22-06-2017.
 */


class Holder extends RecyclerView.ViewHolder{

    public TextView fNameTxt;
    public TextView lNameTxt;
    public TextView mobTxt;


    public Holder(View itemView) {
        super(itemView);

        fNameTxt = (TextView) itemView.findViewById(R.id.fnameTxt);
        lNameTxt= (TextView) itemView.findViewById(R.id.lnameTxt);
        mobTxt= (TextView) itemView.findViewById(R.id.mobTxt);

    }

}