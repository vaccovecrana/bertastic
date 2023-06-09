# bertastic

bertastic is a dead simple API for using Google's high quality [BERT](https://github.com/google-research/bert) language model in Java.

Currently, bertastic is focused on getting embeddings from pre-trained BERT models in Java.

### How To Get It

bertastic is available on [Maven Central](https://search.maven.org/search?q=g:com.robrua.nlp%20a:bertastic). It is also distributed through the [releases page](https://github.com/robrua/bertastic/releases).

To add the latest bertastic release version to your maven project, add the dependency to your `pom.xml` dependencies section:
```xml
<dependencies>
  <dependency>
    <groupId>com.robrua.nlp</groupId>
    <artifactId>bertastic</artifactId>
    <version>1.0.3</version>
  </dependency>
</dependencies>
```

### Usage

You can use bertastic with pre-trained BERT models generated with bertastic's Python tools. You can also used pre-generated models on Maven Central.

To load a model from your local filesystem, you can use:

```java
try (Bert bert = Bert.load(new File("/path/to/your/model/"))) {
  // Embed some sequences
}
```

If the model is in your classpath (e.g. if you're pulling it in via Maven), you can use:

```java
try (Bert bert = Bert.load("/resource/path/to/your/model")) {
  // Embed some sequences
}
```

Once you have a BERT model loaded, you can get sequence embeddings using `bert.embedSequence` or `bert.embedSequences`:

```java
float[] embedding = bert.embedSequence("A sequence");
float[][] embeddings = bert.embedSequences("Multiple", "Sequences");
```

If you want per-token embeddings, you can use `bert.embedTokens`:

```java
float[][] embedding = bert.embedTokens("A sequence");
float[][][] embeddings = bert.embedTokens("Multiple", "Sequences");
```

### Pre-Generated Maven Central Models
Various TensorFlow Hub BERT models are available in bertastic format on [Maven Central](https://search.maven.org/search?q=g:com.robrua.nlp.models). To use one in your project, add the following to your `pom.xml`, substituting one of the Artifact IDs listed below in place of `ARTIFACT-ID` in the `artifactId`:

```xml
<dependencies>
  <dependency>
    <groupId>com.robrua.nlp.models</groupId>
    <artifactId>ARTIFACT-ID</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

Once you've pulled in the dependency, you can load the model using this code. Substitute the appropriate Resource Path from the list below in place of `RESOURCE-PATH` based on the model you added as a dependency:

```java
try (Bert bert = Bert.load("RESOURCE-PATH")) {
    // Embed some sequences
}
```

#### Available Models

| Model | Languages | Layers | Embedding Size | Heads | Parameters | Artifact ID | Resource Path |
| --- | --- | --- | --- | --- | --- | --- | --- |
| [BERT-Base, Uncased](https://tfhub.dev/google/bert_uncased_L-12_H-768_A-12/1) | English | 12 | 768 | 12 | 110M | bertastic-uncased-L-12-H-768-A-12 [![Maven Central](https://img.shields.io/maven-central/v/com.robrua.nlp.models/bertastic-uncased-L-12-H-768-A-12.svg)](https://search.maven.org/search?q=g:com.robrua.nlp.models%20a:bertastic-uncased-L-12-H-768-A-12) | com/robrua/nlp/bertastic/bert-uncased-L-12-H-768-A-12 |
| [BERT-Base, Cased](https://tfhub.dev/google/bert_cased_L-12_H-768_A-12/1) | English | 12 | 768 | 12 | 110M | bertastic-cased-L-12-H-768-A-12 [![Maven Central](https://img.shields.io/maven-central/v/com.robrua.nlp.models/bertastic-cased-L-12-H-768-A-12.svg)](https://search.maven.org/search?q=g:com.robrua.nlp.models%20a:bertastic-cased-L-12-H-768-A-12) | com/robrua/nlp/bertastic/bert-cased-L-12-H-768-A-12 |
| [BERT-Base, Multilingual Cased](https://tfhub.dev/google/bert_multi_cased_L-12_H-768_A-12/1) | 104 Languages | 12 | 768 | 12 | 110M | bertastic-multi-cased-L-12-H-768-A-12 [![Maven Central](https://img.shields.io/maven-central/v/com.robrua.nlp.models/bertastic-multi-cased-L-12-H-768-A-12.svg)](https://search.maven.org/search?q=g:com.robrua.nlp.models%20a:bertastic-multi-cased-L-12-H-768-A-12) | com/robrua/nlp/bertastic/bert-multi-cased-L-12-H-768-A-12 |
| [BERT-Base, Chinese](https://tfhub.dev/google/bert_chinese_L-12_H-768_A-12/1) | Chinese Simplified and Traditional | 12 | 768 | 12 | 110M | bertastic-chinese-L-12-H-768-A-12 [![Maven Central](https://img.shields.io/maven-central/v/com.robrua.nlp.models/bertastic-chinese-L-12-H-768-A-12.svg)](https://search.maven.org/search?q=g:com.robrua.nlp.models%20a:bertastic-chinese-L-12-H-768-A-12) | com/robrua/nlp/bertastic/bert-chinese-L-12-H-768-A-12 |

## Bugs

If you find bugs please let us know via a pull request or issue.

## Citing bertastic

If you used bertastic for your research, please [cite the project](https://doi.org/10.5281/zenodo.2651822).
