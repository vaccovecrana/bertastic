package io.vacco.bertastic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * A port of the BERT FullTokenizer in the <a href="https://github.com/google-research/bert">BERT GitHub Repository</a>.
 * <p>
 * It's used to segment input sequences into the BERT tokens that exist in the model's vocabulary. These tokens are later converted into inputIds for the model.
 * <p>
 * It basically just feeds sequences to the {@link BtBasic} then passes those results to the
 * {@link BtWordPiece}
 *
 * @author Rob Rua (https://github.com/robrua)
 * @see <a href="https://github.com/google-research/bert/blob/master/tokenization.py">The Python tokenization code this is ported from</a>
 */
public class BtFull extends BtTokenizer {

  private final Map<String, Integer> vocabulary;
  private final BtBasic basic;
  private final BtWordPiece wordpiece;

  private static Map<String, Integer> loadVocabulary(File in) {
    var vocabulary = new HashMap<String, Integer>();
    try (BufferedReader reader = new BufferedReader(new FileReader(in, StandardCharsets.UTF_8))) {
      int index = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        vocabulary.put(line.trim(), index++);
      }
    } catch (IOException e) {
      throw new RuntimeException("Unable to read vocabulary from " + in.getAbsolutePath(), e);
    }
    return vocabulary;
  }

  /**
   * Creates a BERT {@link BtFull}
   *
   * @param vocabulary BERT vocabulary file to use for tokenization
   * @param doLowerCase whether to convert sequences to lower case during tokenization
   */
  public BtFull(File vocabulary, boolean doLowerCase) {
    this.vocabulary = loadVocabulary(vocabulary);
    basic = new BtBasic(doLowerCase);
    wordpiece = new BtWordPiece(this.vocabulary);
  }

  /**
   * Converts BERT sub-tokens into their inputIds
   *
   * @param tokens the tokens to convert
   * @return the inputIds for the tokens
   */
  public int[] convert(String[] tokens) {
    return Arrays.stream(tokens).mapToInt(vocabulary::get).toArray();
  }

  @Override
  public String[] tokenize(String sequence) {
    return Arrays.stream(wordpiece.tokenize(basic.tokenize(sequence)))
        .flatMap(Stream::of)
        .toArray(String[]::new);
  }

  @Override
  public String[][] tokenize(String ... sequences) {
    return Arrays.stream(basic.tokenize(sequences))
        .map((var tokens) -> Arrays.stream(wordpiece.tokenize(tokens))
            .flatMap(Stream::of)
            .toArray(String[]::new)
        )
        .toArray(String[][]::new);
  }

}
