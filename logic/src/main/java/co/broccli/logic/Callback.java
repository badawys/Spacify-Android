package co.broccli.logic;


public interface Callback <T> {

    public void onResult (T t);

    public void onError (String errorMessage);

}
