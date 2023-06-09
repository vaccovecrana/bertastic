package io.vacco.bertastic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A port of the BERT FullTokenizer in the <a href="https://github.com/google-research/bert">BERT GitHub Repository</a>.
 * <p>
 * It's used to segment input sequences into the BERT tokens that exist in the model's vocabulary. These tokens are later converted into inputIds for the model.
 * <p>
 * It basically just feeds sequences to the {@link BasicTokenizer} then passes those results to the
 * {@link WordpieceTokenizer}
 *
 * @author Rob Rua (https://github.com/robrua)
 * @version 1.0.3
 * @see <a href="https://github.com/google-research/bert/blob/master/tokenization.py">The Python tokenization code this is ported from</a>
 * @since 1.0.3
 */
public class FullTokenizer extends Tokenizer {

  private static final boolean DEFAULT_DO_LOWER_CASE = false;

  private final Map<String, Integer> vocabulary;
  private final BasicTokenizer basic;
  private final WordpieceTokenizer wordpiece;

  private static Map<String, Integer> loadVocabulary(File in) {
    var vocabulary = new HashMap<String, Integer>();
    try (BufferedReader reader = new BufferedReader(new FileReader(in, StandardCharsets.UTF_8))) {
      int index = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        vocabulary.put(line.trim(), index++);
      }
    } catch (final IOException e) {
      throw new RuntimeException("Unable to read vocabulary from " + in.getAbsolutePath(), e);
    }
    return vocabulary;
  }

  /**
   * Creates a BERT {@link FullTokenizer}
   *
   * @param vocabulary BERT vocabulary file to use for tokenization
   * @since 1.0.3
   */
  public FullTokenizer(File vocabulary) {
    this(vocabulary, DEFAULT_DO_LOWER_CASE);
  }

  /**
   * Creates a BERT {@link FullTokenizer}
   *
   * @param vocabulary BERT vocabulary file to use for tokenization
   * @param doLowerCase whether to convert sequences to lower case during tokenization
   * @since 1.0.3
   */
  public FullTokenizer(File vocabulary, boolean doLowerCase) {
    this.vocabulary = loadVocabulary(vocabulary);
    basic = new BasicTokenizer(doLowerCase);
    wordpiece = new WordpieceTokenizer(this.vocabulary);
  }

  /**
   * Converts BERT sub-tokens into their inputIds
   *
   * @param tokens the tokens to convert
   * @return the inputIds for the tokens
   * @since 1.0.3
   */
  public int[] convert(final String[] tokens) {
    return Arrays.stream(tokens).mapToInt(vocabulary::get).toArray();
  }

  @Override
  public String[] tokenize(final String sequence) {
    return Arrays.stream(wordpiece.tokenize(basic.tokenize(sequence)))
        .flatMap(Stream::of)
        .toArray(String[]::new);
  }

  @Override
  public String[][] tokenize(final String ... sequences) {
    return Arrays.stream(basic.tokenize(sequences))
        .map((var tokens) -> Arrays.stream(wordpiece.tokenize(tokens))
            .flatMap(Stream::of)
            .toArray(String[]::new)
        )
        .toArray(String[][]::new);
  }
}
