package com.example.tuan04_nhom4;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListCustomerAdapter extends BaseAdapter {
    private ArrayList<Customer> lstCustomer;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListCustomerAdapter(Context context, ArrayList<Customer> lstCustomer) {
        this.context = context;
        this.lstCustomer = lstCustomer;
    }

    @Override
    public int getCount() {
        return lstCustomer.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCustomer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView =layoutInflater.inflate(R.layout.list_customer_layout,null);

                    //LayoutInflater.from(context).inflate(R.layout.list_customer_layout, parent, false); //layoutInflater.inflate(R.layout.list_customer_layout,null);

            viewHolder= new ViewHolder();
            viewHolder.imgAvatar= (ImageView) convertView.findViewById(R.id.imgAvatar);
            viewHolder.imgRole= (ImageView) convertView.findViewById(R.id.imgRole);
            viewHolder.fullNameView=(TextView) convertView.findViewById(R.id.txtFullName);
            viewHolder.phoneNumberView=(TextView) convertView.findViewById(R.id.txtPhoneNumber);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

//        Customer currentCustomer = (Customer) getItem(position);
//        TextView txtFullName = convertView.findViewById(R.id.txtFullName);
//        TextView txtPhoneNumber = convertView.findViewById(R.id.txtPhoneNumber);
//        txtFullName.setText(currentCustomer.getFullName());
//        txtPhoneNumber.setText(currentCustomer.getPhoneNumber());
//        return convertView;
        Customer cus = this.lstCustomer.get(position);
        viewHolder.fullNameView.setText(cus.getFullName());
        viewHolder.phoneNumberView.setText(cus.getPhoneNumber());
        int imgAvatarId = this.getImageIdByName(1 );
        int imgRoleId = this.getImageIdByName(2);
        viewHolder.imgAvatar.setImageResource(imgAvatarId);
        viewHolder.imgRole.setImageResource(imgRoleId);
        return convertView;
    }

    public int getImageIdByName(int type) {
        String imgName="";

        imgName= type==1?"user_avatar":"user_role";

        String pkgName = context.getPackageName();
        int imgID = context.getResources().getIdentifier(imgName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + imgName + "==> Res ID = " + imgID);
        return imgID;
    }

    private class ViewHolder {
        ImageView imgAvatar;
        ImageView imgRole;
        TextView fullNameView;
        TextView phoneNumberView;
    }
}
