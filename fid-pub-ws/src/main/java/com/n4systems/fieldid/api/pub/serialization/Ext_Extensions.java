// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/Extensions.proto
package com.n4systems.fieldid.api.pub.serialization;

import com.google.protobuf.FieldOptions;
import com.squareup.wire.Extension;

public final class Ext_Extensions {

  private Ext_Extensions() {
  }

  public static final Extension<FieldOptions, String> serialized_name = Extension
      .stringExtending(FieldOptions.class)
      .setName("com.n4systems.fieldid.api.pub.serialization.serialized_name")
      .setTag(50001)
      .buildOptional();
}
