package pl.jwojcik.gascompanion.services;


public interface ObjectResultListener {
    void onResult(boolean isSuccess, String error, Object object);
}
