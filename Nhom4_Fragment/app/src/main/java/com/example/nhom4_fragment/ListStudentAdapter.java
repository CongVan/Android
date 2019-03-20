package com.example.nhom4_fragment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListStudentAdapter extends BaseAdapter {
    private ArrayList<Student> lstStudent;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListStudentAdapter(Context context, ArrayList<Student> lstStudent) {
        this.context = context;
        this.lstStudent = lstStudent;
    }

    @Override
    public int getCount() {
        return lstStudent.size();
    }

    @Override
    public Object getItem(int position) {
        return lstStudent.get(position);
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
            convertView =layoutInflater.inflate(R.layout.layout_item_student,null);

            //LayoutInflater.from(context).inflate(R.layout.list_Student_layout, parent, false); //layoutInflater.inflate(R.layout.list_Student_layout,null);

            viewHolder= new ViewHolder();
            viewHolder.imgAvatar= (ImageView) convertView.findViewById(R.id.imgAvatar);

            viewHolder.fullNameView=(TextView) convertView.findViewById(R.id.txtFullName);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

//        Student currentStudent = (Student) getItem(position);
//        TextView txtFullName = convertView.findViewById(R.id.txtFullName);
//        TextView txtPhoneNumber = convertView.findViewById(R.id.txtPhoneNumber);
//        txtFullName.setText(currentStudent.getFullName());
//        txtPhoneNumber.setText(currentStudent.getPhoneNumber());
//        return convertView;
        Student cus = this.lstStudent.get(position);
        viewHolder.fullNameView.setText(cus.getCode());
//        viewHolder.phoneNumberView.setText(cus.getPhoneNumber());
        int imgAvatarId = this.getImageIdByName(1 );

        viewHolder.imgAvatar.setImageResource(imgAvatarId);

        return convertView;
    }

    public int getImageIdByName(int type) {
        String imgName="user";

//        imgName= type==1?"user";

        String pkgName = context.getPackageName();
        int imgID = context.getResources().getIdentifier(imgName, "mipmap", pkgName);
//        Log.i("CustomListView", "Res Name: " + imgName + "==> Res ID = " + imgID);
        return imgID;
    }

    private class ViewHolder {
        ImageView imgAvatar;

        TextView fullNameView;

    }
}
