package io.vacco.bertastic;

import org.tensorflow.Tensor;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.buffer.IntDataBuffer;
import org.tensorflow.types.TInt32;

public class BtInputs implements AutoCloseable {

  public final Tensor inputIds, inputMask, segmentIds;

  public BtInputs(IntDataBuffer inputIds, IntDataBuffer inputMask, IntDataBuffer segmentIds,
                  int count, int maxSequenceLength) {
    this.inputIds = TInt32.tensorOf(Shape.of(count, maxSequenceLength), inputIds);
    this.inputMask = TInt32.tensorOf(Shape.of(count, maxSequenceLength), inputMask);
    this.segmentIds = TInt32.tensorOf(Shape.of(count, maxSequenceLength), segmentIds);
  }

  @Override
  public void close() {
    inputIds.close();
    inputMask.close();
    segmentIds.close();
  }

}
