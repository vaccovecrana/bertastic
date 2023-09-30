plugins { id("io.vacco.oss.gitflow") version "0.9.8" }

group = "io.vacco.bertastic"
version = "1.5.0"

configure<io.vacco.oss.gitflow.GsPluginProfileExtension> {
  addJ8Spec()
  sharedLibrary(true, false)
}

val api by configurations

dependencies {
  api("org.tensorflow:tensorflow-core-platform:0.5.0")
  testImplementation("com.robrua.nlp.models:easy-bert-uncased-L-12-H-768-A-12:1.0.0")
  testImplementation("com.google.code.gson:gson:2.10.1")
}
