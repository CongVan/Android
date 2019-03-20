package com.example.nhom4_fragment;

public interface MainCallbacks {
    void onMsgFromFragmentToMain(String sender,String msg);
    void onChangeSelectionFromFragmentToMain(String sender,int position, Student student);
    void onControlListFromFragmentToMain (String sender,String controlCode);
}
