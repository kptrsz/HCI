package ptr.hf.network;

/**
 * Interface for api callback
 */
public interface IApiResultListener<T> {
    /**
     * Api call has finished successfully
     *
     * @param result    The result
     */
    void success(T result);

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