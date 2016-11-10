package co.broccli.logic;


public interface Callback <T> {

    void onResult (T t);

    void onError (String errorMessage);

}
