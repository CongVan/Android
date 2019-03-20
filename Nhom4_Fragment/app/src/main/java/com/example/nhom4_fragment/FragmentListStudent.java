package com.example.nhom4_fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class FragmentListStudent extends Fragment implements FragmentCallbacks {
    MainActivity main;
    Context context;
    LinearLayout layoutStudentList;
    ListStudentAdapter listCustomerAdapter;
    Student currentStudent;
    int currentPosition;
    private ArrayList<Student> lstStudent = null;

    private ArrayList<Student> getListStudent() {
        ArrayList<Student> lst = new ArrayList<Student>();

        for (Integer i = 0; i < 100; i++) {
            String codesRandom="VAN";
            String code= codesRandom.charAt(new Random().nextInt(codesRandom.length()))+"_"+ (new Random().nextInt(1000)+1000);
             String fullName="Sinh viên "+i.toString();
             String className= ((Integer)(new Random().nextInt(9)+1)).toString();
            lst.add(new Student(code, fullName, className, ((Integer)(new Random().nextInt(9)+1)).toString()));
        }
        return lst;
    }

    public static FragmentListStudent newInstance() {
        FragmentListStudent fragmentListStudent = new FragmentListStudent();
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentListStudent.setArguments(args);
        return fragmentListStudent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lstStudent = getListStudent();
        layoutStudentList = (LinearLayout) inflater.inflate(R.layout.layout_list_student, null);

        ListView listView = (ListView) layoutStudentList.findViewById(R.id.lsvStudent);
        listCustomerAdapter = new ListStudentAdapter(context, lstStudent);
        listView.setAdapter(listCustomerAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();

                setDataOnChangeItem(position);
            }
        });

        return layoutStudentList;

    }

    private void setDataOnChangeItem(int position) {
        currentStudent = lstStudent.get(position);
        currentPosition = position;
        //send message to main
        main.onMsgFromFragmentToMain("LIST_FRAG", "Chọn " + position);
        main.onChangeSelectionFromFragmentToMain("LIST_FRAG", position, currentStudent);
        //
        TextView txtCurrentChose = layoutStudentList.findViewById(R.id.txtCurrentChose);
        txtCurrentChose.setText("Mã số: " + currentStudent.getCode());
    }

    @Override
    public void onMsgFromMainToFragment(String msg) {

    }

    @Override
    public void onChangeSelectionFromMainToFragment(int position, Student student) {

    }

    @Override
    public void onControlListFromMainToFragment(String controlCode) {
        final ListView lsvStudent = (ListView) layoutStudentList.findViewById(R.id.lsvStudent);
//        Toast toastMsg= new Toast();
//        toastMsg.setGravity(Gravity.TOP,0,0);
//        toastMsg.setText(controlCode +" " +currentPosition);
//        toastMsg.show();
//        Toast.makeText(main, controlCode + " " + currentPosition, Toast.LENGTH_LONG).show();
        switch (controlCode) {
            case "CONTROL_FIRST": {
                currentPosition = 0;
                break;
            }
            case "CONTROL_LAST": {
                currentPosition = lstStudent.size() - 1;
//                lsvStudent.setSelection(lstStudent.size());
                break;
            }
            case "CONTROL_PREV": {
                currentPosition = currentPosition - 1 < 0 ? 0 : currentPosition - 1;
//                 lsvStudent.setSelection(currentPosition);
                break;
            }
            case "CONTROL_NEXT": {
                currentPosition = currentPosition + 1 > (lstStudent.size() - 1) ? (lstStudent.size() - 1) : currentPosition + 1;
//                lsvStudent.setSelection(currentPosition);
                break;
            }
            default:
                break;
        }
//        currentPosition=10;
//        lsvStudent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lsvStudent.clearFocus();
//        lsvStudent.requestFocus();
        lsvStudent.requestFocusFromTouch();
//        lsvStudent.clearFocus();
        lsvStudent.post(new Runnable() {
            @Override
            public void run() {
//                lsvStudent.performItemClick(lsvStudent.getAdapter().getView(currentPosition,null,null),
//                        currentPosition,lsvStudent.getAdapter().getItemId(currentPosition));


//                lsvStudent.clearFocus();
//                lsvStudent.requestFocusFromTouch();
                lsvStudent.setItemChecked(currentPosition, true);
                lsvStudent.setSelection(currentPosition);
                lsvStudent.smoothScrollToPositionFromTop(currentPosition,0,0);
                listCustomerAdapter.notifyDataSetChanged();
//
                setDataOnChangeItem(currentPosition);
//                lsvStudent.requestFocusFromTouch();
//                lsvStudent.clearFocus();
            }
        });

//        lsvStudent.setItemChecked(currentPosition, true);
//        Toast.makeText(main, "Current  " + currentPosition, Toast.LENGTH_LONG).show();
    }
}
