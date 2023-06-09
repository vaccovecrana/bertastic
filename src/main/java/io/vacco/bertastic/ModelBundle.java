package io.vacco.bertastic;

import java.io.File;
import java.util.Objects;

public class ModelBundle {

  private static final String ASSETS = "assets";
  private static final String MODEL_DETAILS = "model.json";
  private static final String VOCAB_FILE = "vocab.txt";

  public File modelDetails, bundleDir, vocabFile;

  public static ModelBundle from(File bertRoot) {
    var mb = new ModelBundle();
    var assets = new File(Objects.requireNonNull(bertRoot), ASSETS);
    mb.modelDetails = new File(assets, MODEL_DETAILS);
    mb.vocabFile = new File(assets, VOCAB_FILE);
    mb.bundleDir = bertRoot;
    return mb;
  }

}
