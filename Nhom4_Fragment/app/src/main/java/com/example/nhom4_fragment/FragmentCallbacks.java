package com.example.nhom4_fragment;

public interface FragmentCallbacks {
    void    onMsgFromMainToFragment(String msg);
    void onChangeSelectionFromMainToFragment(int position,int length,Student student);
    void onControlListFromMainToFragment (String controlCode);
}
