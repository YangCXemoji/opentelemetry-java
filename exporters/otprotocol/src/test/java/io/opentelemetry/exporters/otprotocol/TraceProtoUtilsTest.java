/*
 * Copyright 2019, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.exporters.otprotocol;

import static com.google.common.truth.Truth.assertThat;

import com.google.protobuf.ByteString;
import io.opentelemetry.proto.trace.v1.ConstantSampler;
import io.opentelemetry.proto.trace.v1.ConstantSampler.ConstantDecision;
import io.opentelemetry.sdk.trace.config.TraceConfig;
import io.opentelemetry.trace.SpanId;
import io.opentelemetry.trace.TraceId;
import io.opentelemetry.trace.util.Samplers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link io.opentelemetry.exporters.otprotocol.TraceProtoUtils}. */
@RunWith(JUnit4.class)
public class TraceProtoUtilsTest {
  private static final io.opentelemetry.proto.trace.v1.TraceConfig TRACE_CONFIG_PROTO =
      io.opentelemetry.proto.trace.v1.TraceConfig.newBuilder()
          .setConstantSampler(
              ConstantSampler.newBuilder().setDecision(ConstantDecision.ALWAYS_OFF).build())
          .setMaxNumberOfAttributes(10)
          .setMaxNumberOfTimedEvents(9)
          .setMaxNumberOfLinks(8)
          .setMaxNumberOfAttributesPerTimedEvent(2)
          .setMaxNumberOfAttributesPerLink(1)
          .build();

  private static final byte[] TRACE_ID_BYTES =
      new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'a'};
  private static final TraceId TRACE_ID = TraceId.fromBytes(TRACE_ID_BYTES, 0);
  private static final byte[] SPAN_ID_BYTES = new byte[] {0, 0, 0, 0, 0, 0, 0, 'b'};
  private static final SpanId SPAN_ID = SpanId.fromBytes(SPAN_ID_BYTES, 0);

  @Test
  public void toProtoTraceId() {
    ByteString expected = ByteString.copyFrom(TRACE_ID_BYTES);
    assertThat(TraceProtoUtils.toProtoTraceId(TRACE_ID)).isEqualTo(expected);
  }

  @Test
  public void toProtoSpanId() {
    ByteString expected = ByteString.copyFrom(SPAN_ID_BYTES);
    assertThat(TraceProtoUtils.toProtoSpanId(SPAN_ID)).isEqualTo(expected);
  }

  @Test
  public void traceConfigFromProto() {
    TraceConfig traceConfig = TraceProtoUtils.traceConfigFromProto(TRACE_CONFIG_PROTO);
    assertThat(traceConfig.getSampler()).isEqualTo(Samplers.neverSample());
    assertThat(traceConfig.getMaxNumberOfAttributes()).isEqualTo(10);
    assertThat(traceConfig.getMaxNumberOfEvents()).isEqualTo(9);
    assertThat(traceConfig.getMaxNumberOfLinks()).isEqualTo(8);
    assertThat(traceConfig.getMaxNumberOfAttributesPerEvent()).isEqualTo(2);
    assertThat(traceConfig.getMaxNumberOfAttributesPerLink()).isEqualTo(1);
  }
}
