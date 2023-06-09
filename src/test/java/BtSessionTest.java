import com.google.gson.Gson;
import io.vacco.bertastic.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class BtSessionTest {
  static {
    it("Creates BERT embeddings", () -> {
      if (!GraphicsEnvironment.isHeadless()) {
        var bertMod = BtSessionTest.class.getResource("/com/robrua/nlp/easy-bert/bert-uncased-L-12-H-768-A-12");
        var tmp = BtFileIO.newTempDir();

        BtFileIO.unzip(bertMod, tmp);

        var g = new Gson();
        var source = BtModelSource.from(tmp);
        var st0 = "Papa Gundam is watching...";
        var strings = List.of("1", "2", "3");

        try (var bert = BtSession.load(source, g::fromJson)) {
          var vec0 = bert.embedSequence(st0);
          var vec1 = bert.embedSequence(st0);
          var vec2 = bert.embedSequences(strings);
          System.out.printf("%s -> %s%n", st0, Arrays.toString(vec0));
          System.out.printf("%s -> %s%n", st0, Arrays.toString(vec1));

          for (int i = 0; i < vec2.length; i++) {
            var st = strings.get(i);
            var a = vec2[i];
            System.out.printf("%s -> %s%n", st, Arrays.toString(a));
          }
        }
      } else {
        System.out.println("CI environment, nothing to do.");
      }
    });
  }
}
