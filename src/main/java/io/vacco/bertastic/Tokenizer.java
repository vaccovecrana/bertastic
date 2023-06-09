package io.vacco.bertastic;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A tokenizer that converts text sequences into tokens or sub-tokens for BERT to use
 *
 * @author Rob Rua (https://github.com/robrua)
 */
public abstract class Tokenizer {
  /**
   * Splits a sequence into tokens based on whitespace
   *
   * @param sequence the sequence to split
   * @return a stream of the tokens from the stream that were separated by whitespace
   */
  protected static Stream<String> whitespaceTokenize(final String sequence) {
    return Arrays.stream(sequence.trim().split("\\s+"));
  }

  /**
   * Tokenizes a multiple sequences
   *
   * @param sequences the sequences to tokenize
   * @return the tokens in the sequences, in the order the {@link java.lang.Iterable} provided them
   */
  public String[][] tokenize(Iterable<String> sequences) {
    return tokenize(Iterables.toArray(sequences));
  }

  /**
   * Tokenizes a multiple sequences
   *
   * @param sequences the sequences to tokenize
   * @return the tokens in the sequences, in the order the {@link java.util.Iterator} provided them
   */
  public String[][] tokenize(final Iterator<String> sequences) {
    return tokenize(Iterables.toArray(sequences));
  }

  /**
   * Tokenizes a single sequence
   *
   * @param sequence the sequence to tokenize
   * @return the tokens in the sequence
   */
  public abstract String[] tokenize(String sequence);

  /**
   * Tokenizes a multiple sequences
   *
   * @param sequences the sequences to tokenize
   * @return the tokens in the sequences, in the order they were provided
   */
  public abstract String[][] tokenize(String... sequences);
}
