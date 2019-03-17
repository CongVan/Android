package com.example.tuan04_nhom4;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    Integer length=100;
    String[] items=new String[length];
    ListView lsvTest;
    ArrayList<Customer> lstCustomer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstCustomer= getListCustomer();
        ListView listView= findViewById(R.id.lsvTest);
        ListCustomerAdapter listCustomerAdapter=new ListCustomerAdapter(this,lstCustomer);
        listView.setAdapter(listCustomerAdapter);
        listView.setOnItemClickListener(this);
    }

    private ArrayList<Customer> getListCustomer(){
        ArrayList<Customer> lstCustomer= new ArrayList<Customer>();
        for (Integer i=0;i<200;i++){
            Random generator = new Random();

            String phoneRandom;
            int num1 = 0;
            int num2 = 0;
            int num3 = 0;

            num1 = generator.nextInt(600) + 100;//numbers can't include an 8 or 9, can't go below 100.
            num2 = generator.nextInt(641) + 100;//number has to be less than 742//can't go below 100.
            num3 = generator.nextInt(8999) + 1000; // make numbers 0 through 9 for each digit.//can't go below 1000.

            phoneRandom = Integer.toString(num1) + Integer.toString(num2) +Integer.toString(num3);
            Customer temp=new Customer("Khách hàng " + i.toString(),phoneRandom);
            lstCustomer.add(temp);
        }
        return  lstCustomer;
    }

    @Override
    public void onItemClick(AdapterView<?>   parent, View view, int position, long id) {
        TextView currentChose= findViewById(R.id.txtCurrentChose);
        Customer customer=lstCustomer.get(position);
        currentChose.setText("You chose: "+ customer.getFullName());
//        Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
    }
}
