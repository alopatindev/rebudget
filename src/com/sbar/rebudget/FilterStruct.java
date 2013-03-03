package com.sbar.rebudget;

public class FilterStruct {
    public String wallet;
    public boolean outcome;
    public String smsAddress;
    public String smsTextContains;
    public String costIntegerRegexp;
    public String costFracRegexp;
    public String remainingIntegerRegexp;
    public String remainingFracRegexp;
    public String storeRegexp;

    public static FilterStruct s_instance = null;
}
