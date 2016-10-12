package com.marktony.drinkingwater.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.marktony.drinkingwater.R;


/**
 * 关联内容为更改体重和水杯容量
 */
public class MainFragment_section3 extends Fragment {

    private View view;
    private EditText et1,et2;
    private com.rey.material.widget.Button btn1,btn2;
    private TextInputLayout textInputLayout_weight,textInputLayout_cup;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    final String DB_PATH = "/data/data/com.marktony.drinkingwater/databases";
    final String DB_NAME = "advicedwateramount.db";

    public MainFragment_section3() {
        // 要求有一个空的构造函数
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_fragment_section3,container,false);

        et1 = (EditText) view.findViewById(R.id.register_et1);//关联体重
        et2 = (EditText) view.findViewById(R.id.register_et2);//关联水杯容量
        btn1 = (com.rey.material.widget.Button) view.findViewById(R.id.register_btn1);//关联清除
        btn2 = (com.rey.material.widget.Button) view.findViewById(R.id.register_btn2);//关联确认
        textInputLayout_weight = (TextInputLayout) view.findViewById(R.id.register_txtInput_weight);
        textInputLayout_cup = (TextInputLayout) view.findViewById(R.id.register_txtInput_cup);

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("UserData",Context.MODE_PRIVATE).edit();
        int weight = sharedPreferences.getInt("Weight", 0);
        int bottle = sharedPreferences.getInt("Bottle", 0);

        textInputLayout_weight.setHint(getString(R.string.section3_register_et1_hint));
        textInputLayout_cup.setHint(getString(R.string.section3_register_et2_hint));
        if ((weight != 0) || (bottle != 0)){
            et1.setText(String.valueOf(weight));
            et2.setText(String.valueOf(bottle));
            et1.setSelection(et1.getText().length());//设置初始化界面时将光标自动显示到文字的最后
        }

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 3){
                    textInputLayout_weight.setError(getString(R.string.section3_register_weight_error1));
                    textInputLayout_weight.setErrorEnabled(true);
                }else{
                    textInputLayout_weight.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 4){
                    textInputLayout_cup.setError(getString(R.string.section3_register_cup_error1));
                    textInputLayout_cup.setErrorEnabled(true);
                }else{
                    textInputLayout_cup.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.getText().clear();
                et2.getText().clear();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //已经在xml文件中指定了输入类型为integer,不需要考虑输入的类型是否为字母或者符号之类的其他类型
                //必须先判断输入内容是否为空，不能在将内容转换为int类型后再判断
                if ((et1.getText().length() == 0 ) || (Integer.parseInt(et1.getText().toString()) > 400) || Integer.parseInt(et1.getText().toString()) == 0){
                    textInputLayout_weight.setError(getString(R.string.section3_register_weight_error2));
                    textInputLayout_weight.setErrorEnabled(true);
                }else if (et2.getText().length() == 0 || (Integer.parseInt(et2.getText().toString())) == 0){
                    textInputLayout_cup.setError(getString(R.string.section3_register_cup_error2));
                    textInputLayout_cup.setErrorEnabled(true);
                } else{
                    textInputLayout_weight.setErrorEnabled(false);
                    textInputLayout_cup.setErrorEnabled(false);
                    //使用SharedPreferences存储用户的体重和杯子容量信息
                    editor.putInt("Weight", Integer.parseInt(et1.getText().toString()));
                    editor.putInt("Bottle", Integer.parseInt(et2.getText().toString()));
                    editor.putInt("AdvicedWaterAmount", queryFromDB(Integer.parseInt(et1.getText().toString())));
                    editor.putBoolean("IsFirstLaunch", false);
                    editor.commit();

                    //输入完成点击确认按钮后自动隐藏输入法面板
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                            .getApplicationContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(et2.getWindowToken(), 0);

                    //显示提示信息
                    final Snackbar snackbar = Snackbar.make(getView(),R.string.section3_register_snackbar_text,Snackbar.LENGTH_SHORT);
                    snackbar.setAction(R.string.section3_register_snackbar_action, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }

        });

        return view;
    }

    //根据用户输入的体重值从数据库中查找相应的建议喝水量
    public int queryFromDB(int weight){
        int amount = 0;//临时保存从数据库中读取出来的值
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.query("advicedwateramount", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                //遍历cursor对象，取出数据并打印
                if(weight == cursor.getInt(cursor.getColumnIndex("weight"))){
                    amount = cursor.getInt(cursor.getColumnIndex("wateramount"));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }


}
