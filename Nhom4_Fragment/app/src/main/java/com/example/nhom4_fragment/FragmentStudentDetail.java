package com.example.nhom4_fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentStudentDetail  extends Fragment  implements  FragmentCallbacks{
    MainActivity mainActivity;
    Context context;
    LinearLayout layoutDetail;
    int mCurrentPosition;
    Student mCurrentStudent;
    public  static  FragmentStudentDetail newInstance(){
        FragmentStudentDetail fragmentStudentDetail=new FragmentStudentDetail();
        Bundle args= new Bundle();
        args.putString("Key1","OK");
        fragmentStudentDetail.setArguments(args);
        return  fragmentStudentDetail;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            context=getActivity();
            mainActivity =(MainActivity) getActivity();

        }catch (IllegalStateException e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutDetail= (LinearLayout) inflater.inflate(R.layout.layout_student_detail,null);
        TextView txtCode=layoutDetail.findViewById(R.id.txtCode);
//        txtCode.setText("OKOKOKO");

        final Button btnFirst =(Button) layoutDetail.findViewById(R.id.btnFirst);
        Button btnPrev =(Button) layoutDetail.findViewById(R.id.btnPrev);
        Button btnNext =(Button) layoutDetail.findViewById(R.id.btnNext);
        Button btnLast =(Button) layoutDetail.findViewById(R.id.btnLast);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mainActivity,"CLick FIRST",Toast.LENGTH_SHORT).show();
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_FIRST");

                btnFirst.setEnabled(false);
            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_LAST");
                btnFirst.setEnabled(true);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_PREV");
                btnFirst.setEnabled(true);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onControlListFromFragmentToMain("DETAIL_FRAG","CONTROL_NEXT");
                btnFirst.setEnabled(true);
            }
        });
        return layoutDetail;

    }

    @Override
    public void onMsgFromMainToFragment(String msg) {

    }

    @Override
    public void onChangeSelectionFromMainToFragment(int position, Student student) {
        mCurrentPosition=position;
        mCurrentStudent=student;
        TextView txtCode= layoutDetail.findViewById(R.id.txtCode);
        TextView txtFullName =layoutDetail.findViewById(R.id.txtFullName);
        TextView txtClassName = layoutDetail.findViewById(R.id.txtClass);
        TextView txtPointAgv= layoutDetail.findViewById(R.id.txtPointAgv);
        txtCode.setText(mCurrentStudent.getCode());
        txtFullName.setText("Họ tên: " +mCurrentStudent.getFullName());
        txtClassName.setText("Lớp: "+mCurrentStudent.getClassName());
        txtPointAgv.setText("Điểm trung bình: "+mCurrentStudent.getPointAgv());

//        Toast.makeText(mainActivity,"Nhận "+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onControlListFromMainToFragment(String controlCode) {

    }


}
