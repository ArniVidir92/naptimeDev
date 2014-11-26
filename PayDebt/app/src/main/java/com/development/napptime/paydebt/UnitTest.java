package com.development.napptime.paydebt;

import android.util.Log;

/**
 * Created by Snorri on 26.11.2014.
 */
public class UnitTest {
    public boolean runAllTests()
    {
        boolean result = runBooleanTests();
        Log.d("Unittest","\n =====================================" +
                         "\n        Unit Testing Results" +
                         "\n =====================================" +
                         "\n   Ran 5 tests" +
                         "\n      all unit tests return ="+result);
        return result;
    }

    private boolean runBooleanTests()
    {
        boolean allIsWell = true;

        if( !minusCheck() ) {allIsWell = false;}

        if( !divCheck() ) {allIsWell = false;}

        if( !plusCheck() ) {allIsWell = false;}

        if( !multCheck() ) {allIsWell = false;}

        if( !isUsableCheck() ) {allIsWell = false;}

        return allIsWell;
    }

    private void runNonBooleanTests()
    {

    }

    private boolean plusCheck(){
        boolean t1 = false, t2 = false, t3 = false;
        Calculator calc = new Calculator();

        if( calc.plus((float) 1.2, (float) 2.5) == 3.7 )
            t1 = true;

        if( calc.plus(-12, 23) == 11.0 )
            t2 = true;

        if( calc.plus(0, 0) != 3.7 )
            t3 = true;

        return t1 && t2 && t3;
    }

    private boolean minusCheck(){
        boolean t1 = false, t2 = false, t3 = false;
        Calculator calc = new Calculator();

        if( calc.minus((float) 1.2, (float) 2.5) == -1.3 )
            t1 = true;

        if( calc.minus(-12, 23) == -35 )
            t2 = true;

        if( calc.minus(0, 0) != 1.0 )
            t3 = true;

        return t1 && t2 && t3;
    }

    private boolean multCheck(){
        boolean t1 = false, t2 = false, t3 = false;
        Calculator calc = new Calculator();

        if( calc.mult((float) 1.2, (float) 2.5) == 3.7 )
            t1 = true;

        if( calc.mult(-12, 23) == -276 )
            t2 = true;

        if( calc.mult(0, 0) != 3.7 )
            t3 = true;

        return t1 && t2 && t3;
    }

    private boolean divCheck(){
        boolean t1 = false, t2 = false, t3 = false;
        Calculator calc = new Calculator();

        if( calc.div((float) 1.2, (float) 2.5) == 2.76 )
            t1 = true;

        if( calc.div(-1, 2) == -0.5 )
            t2 = true;

        if( calc.div(0, 1) != 0 )
            t3 = true;

        return t1 && t2 && t3;
    }

    private boolean isUsableCheck(){
        Calculator calc = new Calculator();
        boolean allIsWell = true;
        calc.equationStr = "";

        if( calc.isUsable() ){
            allIsWell = false;
        }

        calc.equationStr = "++--";

        if( calc.isUsable() ){
            allIsWell = false;
        }

        calc.equationStr = "34/";

        if( calc.isUsable() ){
            allIsWell = false;
        }

        return allIsWell;
    }
}