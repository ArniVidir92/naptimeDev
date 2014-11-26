package com.development.napptime.paydebt;

/**
 * Created by Snorri on 26.11.2014.
 */
public class UnitTest {
    public boolean runAllTests()
    {
        runNonBooleanTests();
        return runBooleanTests();
    }

    private boolean runBooleanTests()
    {
        boolean allIsWell = true;

        //if(!test1) {allIsWell = false;}

        return allIsWell;
    }

    private void runNonBooleanTests()
    {

    }
}
