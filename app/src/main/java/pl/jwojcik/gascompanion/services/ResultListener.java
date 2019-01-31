package pl.jwojcik.gascompanion.services;

import java.util.List;


public interface ResultListener {
    public void onResult(boolean isSuccess, String error, List data);
}
