package io.vacco.bertastic;

import java.io.File;
import java.util.Objects;

public class BtModelSource {

  private static final String ASSETS = "assets";
  private static final String MODEL_DETAILS = "model.json";
  private static final String VOCAB_FILE = "vocab.txt";

  public File modelDetails, bundleDir, vocabFile;

  public static BtModelSource from(File bertRoot) {
    var mb = new BtModelSource();
    var assets = new File(Objects.requireNonNull(bertRoot), ASSETS);
    mb.modelDetails = new File(assets, MODEL_DETAILS);
    mb.vocabFile = new File(assets, VOCAB_FILE);
    mb.bundleDir = bertRoot;
    return mb;
  }

}
