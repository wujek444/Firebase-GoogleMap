package pl.jwojcik.gascompanion.services;


public interface ObjectResultListener {
    public void onResult(boolean isSuccess, String error, Object object);
}
