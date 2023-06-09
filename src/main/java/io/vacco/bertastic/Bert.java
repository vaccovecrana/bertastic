package io.vacco.bertastic;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.StdArrays;
import org.tensorflow.ndarray.buffer.DataBuffers;
import org.tensorflow.types.TFloat32;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;

public class Bert implements AutoCloseable {

  protected final ModelDetails model;

  private final SavedModelBundle bundle;
  private final int separatorTokenId;
  private final int startTokenId;
  private final FullTokenizer tokenizer;

  private static final String SEPARATOR_TOKEN = "[SEP]";
  private static final String START_TOKEN = "[CLS]";

  /**
   * Loads a pre-trained BERT model from a TensorFlow saved model saved by the easy-bert Python utilities
   *
   * @return a ready-to-use BERT model
   */
  public static Bert load(ModelBundle mb, JsonInput ji) {
    try {
      var model = (ModelDetails) ji.fromJson(new FileReader(mb.modelDetails), ModelDetails.class);
      var bundle = SavedModelBundle.load(mb.bundleDir.getAbsolutePath(), "serve");
      return new Bert(bundle, model, mb.vocabFile);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load BERT model", e);
    }
  }

  private Bert(SavedModelBundle bundle, ModelDetails model, File vocabulary) {
    tokenizer = new FullTokenizer(vocabulary, model.doLowerCase);
    this.bundle = bundle;
    this.model = model;
    int[] ids = tokenizer.convert(new String[]{START_TOKEN, SEPARATOR_TOKEN});
    startTokenId = ids[0];
    separatorTokenId = ids[1];
  }

  @Override
  public void close() {
    bundle.close();
  }

  /**
   * Gets pooled BERT embeddings for multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the pooled embeddings for the sequences, in the order the input {@link java.lang.Iterable} provided them
   */
  public float[][] embedSequences(Iterable<String> sequences) {
    return embedSequences(Iterables.toArray(sequences));
  }

  /**
   * Gets pooled BERT embeddings for multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the pooled embeddings for the sequences, in the order the input {@link java.util.Iterator} provided them
   */
  public float[][] embedSequences(Iterator<String> sequences) {
    return embedSequences(Iterables.toArray(sequences));
  }

  /**
   * Gets pooled BERT embeddings for multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the pooled embeddings for the sequences, in the order they were provided
   */
  public float[][] embedSequences(String ... sequences) {
    try (var inputs = getInputs(sequences)) {
      List<Tensor> output = bundle.session().runner()
          .feed(model.inputIds, inputs.inputIds)
          .feed(model.inputMask, inputs.inputMask)
          .feed(model.segmentIds, inputs.segmentIds)
          .fetch(model.pooledOutput)
          .run();
      try (var embedding = output.get(0)) {
        return StdArrays.array2dCopyOf((TFloat32) embedding);
      }
    }
  }

  /**
   * Gets BERT embeddings for each of the tokens in multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the token embeddings for the sequences, in the order the input {@link java.lang.Iterable} provided them
   */
  public float[][][] embedTokens(Iterable<String> sequences) {
    return embedTokens(Iterables.toArray(sequences));
  }

  /**
   * Gets BERT embeddings for each of the tokens in multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the token embeddings for the sequences, in the order the input {@link java.util.Iterator} provided them
   */
  public float[][][] embedTokens(Iterator<String> sequences) {
    return embedTokens(Iterables.toArray(sequences));
  }

  /**
   * Gets BERT embeddings for each of the tokens in multiple sequences. Sequences are usually individual sentences, but don't have to be.
   * The sequences will be processed in parallel as a single batch input to the TensorFlow model.
   *
   * @param sequences the sequences to embed
   * @return the token embeddings for the sequences, in the order they were provided
   */
  public float[][][] embedTokens(String ... sequences) {
    try (var inputs = getInputs(sequences)) {
      var output = bundle.session().runner()
          .feed(model.inputIds, inputs.inputIds)
          .feed(model.inputMask, inputs.inputMask)
          .feed(model.segmentIds, inputs.segmentIds)
          .fetch(model.sequenceOutput)
          .run();
      try (var embedding = output.get(0)) {
        return StdArrays.array3dCopyOf((TFloat32) embedding);
      }
    }
  }

  private Inputs getInputs(String[] sequences) {
    var tokens = tokenizer.tokenize(sequences);
    var inputIds = IntBuffer.allocate(sequences.length * model.maxSequenceLength);
    var inputMask = IntBuffer.allocate(sequences.length * model.maxSequenceLength);
    var segmentIds = IntBuffer.allocate(sequences.length * model.maxSequenceLength);

    /*
     * In BERT:
     * inputIds are the indexes in the vocabulary for each token in the sequence
     * inputMask is a binary mask that shows which inputIds have valid data in them
     * segmentIds are meant to distinguish paired sequences during training tasks. Here they're always 0 since we're only doing inference.
     */
    int instance = 1;
    for (var token : tokens) {
      int[] ids = tokenizer.convert(token);
      inputIds.put(startTokenId);
      inputMask.put(1);
      segmentIds.put(0);
      for (int i = 0; i < ids.length && i < model.maxSequenceLength - 2; i++) {
        inputIds.put(ids[i]);
        inputMask.put(1);
        segmentIds.put(0);
      }
      inputIds.put(separatorTokenId);
      inputMask.put(1);
      segmentIds.put(0);

      while (inputIds.position() < model.maxSequenceLength * instance) {
        inputIds.put(0);
        inputMask.put(0);
        segmentIds.put(0);
      }
      instance++;
    }

    inputIds.rewind();
    inputMask.rewind();
    segmentIds.rewind();

    return new Inputs(
        DataBuffers.of(inputIds), DataBuffers.of(inputMask),
        DataBuffers.of(segmentIds), sequences.length, model.maxSequenceLength
    );
  }
}
