import com.google.gson.Gson;
import io.vacco.bertastic.Bert;
import io.vacco.bertastic.FileIO;
import io.vacco.bertastic.ModelBundle;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class BertTest {
  static {
    it("Loads a BERT model", () -> {
      var bertMod = BertTest.class.getResource("com/robrua/nlp/easy-bert/bert-uncased-L-12-H-768-A-12");
      var tmp = FileIO.newTempDir();

      FileIO.unzip(bertMod, tmp);

      var g = new Gson();
      var modBundle = ModelBundle.from(tmp);

      try (var bert = Bert.load(modBundle, g::fromJson)) {
        var vec0 = bert.embedSequences("Papa Gundam is watching...");
        var vec1 = bert.embedSequences("Papa Gundam is watching...");
      }
    });
  }
}
