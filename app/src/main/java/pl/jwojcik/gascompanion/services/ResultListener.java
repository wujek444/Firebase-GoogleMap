package pl.jwojcik.gascompanion.services;

import java.util.List;


public interface ResultListener {
    void onResult(boolean isSuccess, String error, List data);
}
