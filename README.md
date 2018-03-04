# JSUpySVM

This is a multithreaded, multiclass implementation of a weighted, reduced convex hull-based support vector machine algorithm based on [this paper](http://ieeexplore.ieee.org/document/6119691/). I have written the algorithm in Java, and that is mostly complete. Currently, I am working on developing the package with rJava. Uncommented demos can be found at [this repo](https://github.com/mhmdmodan/svmTests). My presentation can be found at [my website](https://mhmdmodan.com).

As of now, the algorithm can be accessed by calling:

```r
WSVM(df, kernel = "linear",
                  mu = NA,
                  q = 2.0,
                  gamma = 0.01,
                  ep = 1e-2,
                  max_its = 2000,
                  non_sep = 1e-5)
```

Where `df` is a dataframe with classes as the first column, and features as the rest. `kernel` has optiosn of `"linear"`, `"rbf"`, `"monomial"`, and `"polynomial"`. `mu` is calculated by default as $\frac{1}{0.9\kappa}$ where $\kappa$ equals the sum of all weights in the smallest by weight class. Unfortunately it is not possible to assign weights yet (although the implementation in Java supports it).

`q` is the exponent of the monnomial/polynomial kernel, `gamma` is for the `rbf` kernel. `ep` is $\epsilon$. `max_its` is the maximum iterations. `non_sep` is the threshold of deciding if hulls are nonseparable.

Predictions can be done with:

```r
out$predict(vector)
```

Where `out` is the Java object returned from the `WSVM` function and `vector` is a feature vector with features in the same order as `df`.

## Installation

```r
require(devtools)
install_github(“mhmdmodan/JSUpySVM”)
```

If issues arise with Java/R architecture mismatch, clone the repo and install it using the build tab in RStudio.