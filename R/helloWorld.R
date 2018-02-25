#' Hello world function
#'
#' This says hello world
#' @return hello world string
#' @export
#'
#' @examples
#' helloJavaWorld()
helloJavaWorld <- function() {
    hjw <- .jnew("Test")
    out <- .jcall(hjw, "[D", "giveDoub")
    return(out)
}

train <- function(df) {
    whichClass <- as.character(df[[1]])

    # Get dimension
    dim <- as.integer(length(df) - 1)

    # Shuffle the values
    nums <- c(do.call(rbind, df[, -1]))

    wsvm <- J("WSVM")
    svm <- new(wsvm, nums, dim, whichClass)
    params <- .jcall(svm, "LHolders/HyperParam;", "getParams")
    .jcall(params, "V", "setMu", as.double(1))
    predictor <- .jcall(svm, "LMasterPredictor;", "train")
    return(predictor)
}
