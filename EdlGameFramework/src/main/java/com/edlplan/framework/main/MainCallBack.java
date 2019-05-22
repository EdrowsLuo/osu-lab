package com.edlplan.framework.main;

public interface MainCallBack {
    public void onPause();

    public void onResume();

    public void onLowMemory();

    public boolean onBackPressed();
}
