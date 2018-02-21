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