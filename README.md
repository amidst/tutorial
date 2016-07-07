[![Build Status](https://travis-ci.org/amidst/tutorial.svg?branch=master)](https://travis-ci.org/amidst/tutorial)

# Tutorial of the AMIDST Toolbox

This respository contains a tutorial with code examples of the [AMIDST toolbox](http://amidst.github.io/toolbox). The slides of this tutorial can be found in the following link:

https://github.com/amidst/tutorial/raw/master/doc/slides/AMIDST_slides.pdf


This tutorial focuses on how to learn the predefined latent varaibles (i.e. a probabilistic graphical model with unobserved variables) models included in AMIDST. Two learning schemes are showed:

* Parellel multi-core learning based on Java 8 streams.
* Distributed learning functionalities based on [Apache Flink](https://flink.apache.org/).  

In both cases, AMIDST offers Bayesian parameter learning by means of [variational message passing](www.jmlr.org/papers/volume6/winn05a/winn05a.pdf). 

We also show how new user-defined latent varaible models can be easily defined with this toolbox and, also, learnt from local or distributed data.

This is the list of latent variables models included in [AMIDST](http://amidst.github.io/toolbox).

![Latent Variable Models](http://amidst.github.io/toolbox/docs/web/figs/amidstModels-crop.png)
