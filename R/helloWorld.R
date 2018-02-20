#' Hello world function
#'
#' This says hello world
#' @return hello world string
#' @export
#'
#' @examples
#' helloJavaWorld()
helloJavaWorld <- function() {
    hjw <- .jnew("HelloJavaWorld")
    out <- .jcall(hjw, "S", "sayHello")
    return(out)
}