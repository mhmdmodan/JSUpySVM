WSVM <- function(df, kernel = "linear",
                  mu = NA,
                  q = 2.0,
                  gamma = 0.01,
                  ep = 1e-2,
                  max_its = 2000,
                  non_sep = 1e-5) {
    whichClass <- as.character(df[[1]])

    # Get dimension
    dim <- as.integer(length(df) - 1)

    # Shuffle the values
    nums <- c(do.call(rbind, df[, -1]))
    wsvm <- J("WSVM")
    svm <- new(wsvm, nums, dim, whichClass)
    params <- .jcall(svm, "LHolders/HyperParam;", "getParams")
    if (!is.na(mu)) {.jcall(params, "V", "setMu", as.double(mu))}
    .jcall(params, "V", "setQ", as.double(q))
    .jcall(params, "V", "setGamma", as.double(gamma))
    .jcall(params, "V", "setKernel", kernel)
    .jcall(params, "V", "setEp", as.double(ep))
    .jcall(params, "V", "setNonSep", as.double(non_sep))
    .jcall(params, "V", "setMaxIts", as.integer(max_its))
    predictor <- .jcall(svm, "LMasterPredictor;", "train")
    return(predictor)
}
