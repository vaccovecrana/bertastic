package io.vacco.bertastic;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A port of the BERT WordpieceTokenizer in the <a href="https://github.com/google-research/bert">BERT GitHub Repository</a>.
 * <p>
 * The WordpieceTokenizer processes tokens from the {@link BtBasic} into sub-tokens - parts of words that compose BERT's vocabulary.
 * These tokens can then be converted into the inputIds that the BERT model accepts.
 *
 * @author Rob Rua (https://github.com/robrua)
 * @see <a href="https://github.com/google-research/bert/blob/master/tokenization.py">The Python tokenization code this is ported from</a>
 */
public class BtWordPiece extends BtTokenizer {
  private static final int DEFAULT_MAX_CHARACTERS_PER_WORD = 200;
  private static final String DEFAULT_UNKNOWN_TOKEN = "[UNK]";

  private final int maxCharactersPerWord;
  private final String unknownToken;
  private final Map<String, Integer> vocabulary;

  /**
   * Creates a BERT {@link BtWordPiece}
   *
   * @param vocabulary a mapping from sub-tokens in the BERT vocabulary to their inputIds
   */
  public BtWordPiece(Map<String, Integer> vocabulary) {
    this.vocabulary = vocabulary;
    unknownToken = DEFAULT_UNKNOWN_TOKEN;
    maxCharactersPerWord = DEFAULT_MAX_CHARACTERS_PER_WORD;
  }

  /**
   * Creates a BERT {@link BtWordPiece}
   *
   * @param vocabulary            a mapping from sub-tokens in the BERT vocabulary to their inputIds
   * @param unknownToken          the sub-token to use when an unrecognized or too-long token is encountered
   * @param maxCharactersPerToken the maximum number of characters allowed in a token to be sub-tokenized
   */
  public BtWordPiece(Map<String, Integer> vocabulary, String unknownToken, int maxCharactersPerToken) {
    this.vocabulary = vocabulary;
    this.unknownToken = unknownToken;
    maxCharactersPerWord = maxCharactersPerToken;
  }

  private Stream<String> splitToken(String token) {
    char[] characters = token.toCharArray();
    if (characters.length > maxCharactersPerWord) {
      return Stream.of(unknownToken);
    }

    Stream.Builder<String> subtokens = Stream.builder();
    int start = 0;
    int end;
    while (start < characters.length) {
      end = characters.length;
      boolean found = false;
      while (start < end) {
        String substring = (start > 0 ? "##" : "") + String.valueOf(characters, start, end - start);
        if (vocabulary.containsKey(substring)) {
          subtokens.accept(substring);
          start = end;
          found = true;
          break;
        }
        end--;
      }
      if (!found) {
        subtokens.accept(unknownToken);
        break;
      }
      start = end;
    }
    return subtokens.build();
  }

  @Override
  public String[] tokenize(String sequence) {
    return whitespaceTokenize(sequence)
        .flatMap(this::splitToken)
        .toArray(String[]::new);
  }

  @Override
  public String[][] tokenize(String... sequences) {
    return Arrays.stream(sequences)
        .map((String sequence) -> whitespaceTokenize(sequence).toArray(String[]::new))
        .map((String[] tokens) -> Arrays.stream(tokens)
            .flatMap(this::splitToken)
            .toArray(String[]::new))
        .toArray(String[][]::new);
  }
}
