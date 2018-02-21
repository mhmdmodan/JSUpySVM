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
    whichClass <- as.integer(df[[1]])
    dim <- as.integer(length(df) - 1)
    nums <- c(do.call(rbind, df[, -1]))

    twoHull <- J("TwoHull")
    svm <- new(twoHull, nums, dim, whichClass)
    predictor <- .jcall(svm, "LPredictor;", "runAlgo")
    return(predictor)
}
