package com.financial_hospital.listdemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.financial_hospital.listdemo.R.layout.itemdetails;

/**
 * Created by pradip on 23-06-2017.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<ContactDetails> cdList;

    public CustomPagerAdapter(Context context, ArrayList<ContactDetails> list) {
        mContext = context;
        this.cdList = list;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(itemdetails, collection, false);

        TextView firstName = (TextView) layout.findViewById(R.id.fname1);
        TextView lastName = (TextView) layout.findViewById(R.id.lname1);
        TextView mobile = (TextView) layout.findViewById(R.id.mob1);

        ContactDetails cd = cdList.get(position);
        firstName.setText(cd.getFname());
        lastName.setText(cd.getLname());
        mobile.setText(cd.getMobile());

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return cdList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "Title " + position;
    }

}
