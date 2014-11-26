package com.development.napptime.paydebt;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napptime on 12/11/14.
 *
 * The Calculator class handles all the math and button listening needed for an operational
 * calculator.
 */

public class Calculator extends Fragment{

    //Instance variables

    //Our layouts view
    private View view = null;

    //Buttons for calculator
    private Button btn0 = null;
    private Button btn1 = null;
    private Button btn2 = null;
    private Button btn3 = null;
    private Button btn4 = null;
    private Button btn5 = null;
    private Button btn6 = null;
    private Button btn7 = null;
    private Button btn8 = null;
    private Button btn9 = null;
    private Button btnAC = null;
    private Button btnD = null;
    private Button btnM = null;
    private Button btnP = null;
    private Button btnMinus = null;
    private Button btnEq = null;
    private Button btnDot = null;
    private Button btnAns = null;

    //Textboxes for the results and equation
    private EditText eqText = null;
    private EditText resText = null;

    // operations and numbers
    private List<String> operations=new ArrayList<String>();
    private List<Float> numbers=new ArrayList<Float>();

    // Current input
    private String current = "";

    // Lets you know if the number is already a float
    private boolean dotDone = false;

    // The string to display
    private String equationStr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the fragment to create the view
        this.view = inflater.inflate(R.layout.lay_calculator, container, false);

        initializeLayoutVars();

        setClickListeners();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void clickedNumber(View v){
        Button b = (Button) v;
        String s = b.getText().toString();
        if( s.equals(".") && equationStr.length() >= 1 &&  equationStr.substring(equationStr.length()-1).equals(".") ){
            resText.setText("Don't do that");
            destroyEquation(v);
            return;
        }
        if( s.equals(".") ){
            if( dotDone ){
                resText.setText("Don't do that");
                destroyEquation(v);
                return;
            }
            dotDone = true;
        }
        if(current.equals("Not")){
            current = b.getText().toString();
        }else{
            current += b.getText().toString();
        }
        equationStr += b.getText().toString();
        eqText.setText(equationStr);
    }

    public void clickedOp(View v){
        if( current.equals("") ){
            resText.setText("Don't do that");
            destroyEquation(v);
            return ;
        }
        Button b = (Button) v;
        operations.add(b.getText().toString());
        equationStr += b.getText().toString();
        if( !current.equals("Not") ){
            numbers.add(Float.parseFloat(current));
            current = "";
            dotDone = false;
        }
        eqText.setText(equationStr);
    }

    public boolean isUsable(){
        if(equationStr.equals("") || !((Integer) numbers.size()).equals((Integer) operations.size()) ||
                equationStr.substring(equationStr.length()-1).equals("+") || equationStr.substring(equationStr.length()-1).equals("-") ||
                equationStr.substring(equationStr.length()-1).equals("/") || equationStr.substring(equationStr.length()-1).equals("x") ||
                equationStr.substring(equationStr.length()-1).equals("+")){
            return false;
        }
        return true;
    }

    public void compute(View v){
        if( ! isUsable() ){
            resText.setText("NO INPUT OR IDIOT");
            destroyEquation(v);
            return;
        }

        numbers.add(Float.parseFloat(current));
        current = "Not";
        dotDone = false;

        String op;
        List<Float> numb = new ArrayList<Float>();
        equals(numb,numbers);
        List<String> cOperations = new ArrayList<String>();
        int ni = 0;
        for(int i = 0; i < operations.size(); i++){
            op = operations.get(i);
            if( op.equals("x") ){
                numbers.set(ni ,numbers.get(ni) * numbers.get(ni+1));
                numbers.remove(ni+1);
                ni--;
            }
            else if( op.equals("/") ){
                numbers.set(ni ,numbers.get(ni) / numbers.get(ni+1));
                numbers.remove(ni+1);
                ni--;
            }
            else if( op.equals("-") || op.equals("+") ){
                cOperations.add(op);
            }
            ni++;
        }

        ni=0;
        for (String ope : cOperations) {
            if( ope.equals("+") ){
                numbers.set(ni ,numbers.get(ni) + numbers.get(ni+1));
                numbers.remove(ni+1);
                ni--;
            }
            else if( ope.equals("-") ){
                numbers.set(ni ,numbers.get(ni) - numbers.get(ni+1));
                numbers.remove(ni+1);
                ni--;
            }
            ni++;
        }
        resText.setText(""+numbers.get(0));
        equals(numbers, numb);
    }

    public void destroyEquation(View v){
        numbers = new ArrayList<Float>();
        operations = new ArrayList<String>();
        equationStr = "";
        eqText.setText("");
        current = "";
    }

    public void ans(View v){
        if( resText.getText().toString().equals("") ){
            return;
        }
        destroyEquation(v);
        equationStr += resText.getText().toString();
        eqText.setText(equationStr);
        current = resText.getText().toString();
        dotDone = true;
    }

    public void equals(List<Float> a, List<Float> b){
        a.clear();
        for (Float item : b) {
            a.add(item);
        }
    }

    public void initializeLayoutVars(){
        btn0 = (Button) this.view.findViewById(R.id.calc0);
        btn1 = (Button) this.view.findViewById(R.id.calc1);
        btn2 = (Button) this.view.findViewById(R.id.calc2);
        btn3 = (Button) this.view.findViewById(R.id.calc3);
        btn4 = (Button) this.view.findViewById(R.id.calc4);
        btn5 = (Button) this.view.findViewById(R.id.calc5);
        btn6 = (Button) this.view.findViewById(R.id.calc6);
        btn7 = (Button) this.view.findViewById(R.id.calc7);
        btn8 = (Button) this.view.findViewById(R.id.calc8);
        btn9 = (Button) this.view.findViewById(R.id.calc9);
        btnP = (Button) this.view.findViewById(R.id.calcPlus);
        btnM = (Button) this.view.findViewById(R.id.calcM);
        btnMinus = (Button) this.view.findViewById(R.id.calcMinus);
        btnD = (Button) this.view.findViewById(R.id.calcD);
        btnAC = (Button) this.view.findViewById(R.id.calcAC);
        btnEq = (Button) this.view.findViewById(R.id.calcEq);
        btnDot = (Button) this.view.findViewById(R.id.calcDot);
        btnAns = (Button) this.view.findViewById(R.id.calcAns);


        eqText = (EditText) this.view.findViewById(R.id.inputEquation);
        resText = (EditText) this.view.findViewById(R.id.displayResults);

    }

    public void setClickListeners(){
        //Listener; catches when the user clicks the button
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOp(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOp(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOp(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedOp(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compute(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyEquation(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans(v);
            }
        });
        //Listener; catches when the user clicks the button
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNumber(v);
            }
        });
    }
}
