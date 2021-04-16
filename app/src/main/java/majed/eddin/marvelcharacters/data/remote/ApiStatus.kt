package majed.eddin.marvelcharacters.data.remote

enum class ApiStatus {
    OnAuth,
    OnBackEndError,
    OnConnectException,
    OnError,
    OnFailure,
    OnForbidden,
    OnHttpException,
    OnLoading,
    OnNotFound,
    OnSuccess,
    OnTimeoutException,
    OnUnknownHost
}