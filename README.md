# bertastic

API for using Google's [BERT](https://github.com/google-research/bert) language model in Java.

Available in [Maven Central](https://mvnrepository.com/artifact/io.vacco.bertastic/bertastic).

```
implementation("io.vacco.bertastic:bertastic:<LATEST_VERSION>")
```

### Usage

You can use bertastic with pre-trained BERT models generated with [easy-bert's Python tools](https://github.com/robrua/easy-bert/blob/master/easybert/bert.py).

You can also use pre-generated models on Maven Central.

See test case for [example usage](./src/test/java/BtSessionTest.java).

### Pre-Generated Maven Central Models

Various TensorFlow Hub BERT models are available in bertastic format on [Maven Central](https://mvnrepository.com/artifact/com.robrua.nlp.models).

```
implementation("com.robrua.nlp.models:ARTIFACT-ID:1.0.0")
```

## Bugs

If you find bugs please let us know via a pull request or issue.
