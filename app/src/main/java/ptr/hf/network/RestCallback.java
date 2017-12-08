package ptr.hf.network;

import android.util.Log;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestCallback<T> implements Callback<T> {
    protected IApiFinishedListener finishedListener;
    protected IApiResultListener<T> resultListener;

    public RestCallback(IApiFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    public RestCallback(IApiResultListener<T> resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        String responseString = response.toString();
        Log.d("Response__", response.toString()+"__"+response.message());
        Log.d("Response__code__", Integer.toString(response.code()));
        if (response.isSuccessful() && response.body() != null && resultListener != null ||
                response.isSuccessful() && finishedListener != null) {
            success(response.body());
        } else if (response.code() >= 200 && response.code() < 300) {
            if (response.code() == 204) {
                if (finishedListener != null) {
                    finishedListener.success();
                } else if (resultListener != null) {
                    resultListener.fail();
                }
            } else {
                success(response.body());
            }
        } else if (response.code() >= 300 && response.code() < 500 && response.errorBody() != null) {
            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
            if (errorResponse != null)
                error(errorResponse);
            else
                fail();
        } else {
            fail();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("RestCallback", "Rest failure!", t);
        t.printStackTrace();
        fail();
    }

    private void success(T responseBody) {
        if (resultListener != null)
            resultListener.success(responseBody);
        if (finishedListener != null)
            finishedListener.success();
    }

    private void error(ErrorResponse errorResponse) {
        ErrorHandler.printError(errorResponse);

        if (resultListener != null)
            resultListener.error(errorResponse);
        if (finishedListener != null)
            finishedListener.error(errorResponse);
    }

    private void fail() {
        if (resultListener != null)
            resultListener.fail();
        if (finishedListener != null)
            finishedListener.fail();
    }



}