package ptr.hf.network;

import android.util.Log;

public class ErrorHandler {
    public static void printError(ErrorResponse errorResponse) {
        Log.d("API_ERROR_RESPONSE", errorResponse.toString());

        // Turn on for debug purposes only!
//        if (errorResponse.getName() != null && errorResponse.getMessage() != null)
//            UIHelper.createAlert(MainActivity.getInstance(), errorResponse.getName(), errorResponse.getMessage());
    }
}
