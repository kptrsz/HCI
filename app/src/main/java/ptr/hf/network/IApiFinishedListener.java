package ptr.hf.network;

public interface IApiFinishedListener {
    /**
     * Api call has finished successfully
     */
    void success();

    /**
     * Api call has finished with a known error
     *
     * @param errorResponse The known error
     */
    void error(ErrorResponse errorResponse);

    /**
     * Api call has finished with an unknown error
     */
    void fail();
}
