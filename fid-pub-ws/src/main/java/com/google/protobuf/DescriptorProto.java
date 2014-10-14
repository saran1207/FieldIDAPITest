// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * Describes a message type.
 */
public final class DescriptorProto extends Message {

  public static final String DEFAULT_NAME = "";
  public static final List<FieldDescriptorProto> DEFAULT_FIELD = Collections.emptyList();
  public static final List<FieldDescriptorProto> DEFAULT_EXTENSION = Collections.emptyList();
  public static final List<DescriptorProto> DEFAULT_NESTED_TYPE = Collections.emptyList();
  public static final List<EnumDescriptorProto> DEFAULT_ENUM_TYPE = Collections.emptyList();
  public static final List<ExtensionRange> DEFAULT_EXTENSION_RANGE = Collections.emptyList();
  public static final List<OneofDescriptorProto> DEFAULT_ONEOF_DECL = Collections.emptyList();

  @ProtoField(tag = 1, type = STRING)
  public final String name;

  @ProtoField(tag = 2, label = REPEATED)
  public final List<FieldDescriptorProto> field;

  @ProtoField(tag = 6, label = REPEATED)
  public final List<FieldDescriptorProto> extension;

  @ProtoField(tag = 3, label = REPEATED)
  public final List<DescriptorProto> nested_type;

  @ProtoField(tag = 4, label = REPEATED)
  public final List<EnumDescriptorProto> enum_type;

  @ProtoField(tag = 5, label = REPEATED)
  public final List<ExtensionRange> extension_range;

  @ProtoField(tag = 8, label = REPEATED)
  public final List<OneofDescriptorProto> oneof_decl;

  @ProtoField(tag = 7)
  public final MessageOptions options;

  public DescriptorProto(String name, List<FieldDescriptorProto> field, List<FieldDescriptorProto> extension, List<DescriptorProto> nested_type, List<EnumDescriptorProto> enum_type, List<ExtensionRange> extension_range, List<OneofDescriptorProto> oneof_decl, MessageOptions options) {
    this.name = name;
    this.field = immutableCopyOf(field);
    this.extension = immutableCopyOf(extension);
    this.nested_type = immutableCopyOf(nested_type);
    this.enum_type = immutableCopyOf(enum_type);
    this.extension_range = immutableCopyOf(extension_range);
    this.oneof_decl = immutableCopyOf(oneof_decl);
    this.options = options;
  }

  private DescriptorProto(Builder builder) {
    this(builder.name, builder.field, builder.extension, builder.nested_type, builder.enum_type, builder.extension_range, builder.oneof_decl, builder.options);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DescriptorProto)) return false;
    DescriptorProto o = (DescriptorProto) other;
    return equals(name, o.name)
        && equals(field, o.field)
        && equals(extension, o.extension)
        && equals(nested_type, o.nested_type)
        && equals(enum_type, o.enum_type)
        && equals(extension_range, o.extension_range)
        && equals(oneof_decl, o.oneof_decl)
        && equals(options, o.options);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = name != null ? name.hashCode() : 0;
      result = result * 37 + (field != null ? field.hashCode() : 1);
      result = result * 37 + (extension != null ? extension.hashCode() : 1);
      result = result * 37 + (nested_type != null ? nested_type.hashCode() : 1);
      result = result * 37 + (enum_type != null ? enum_type.hashCode() : 1);
      result = result * 37 + (extension_range != null ? extension_range.hashCode() : 1);
      result = result * 37 + (oneof_decl != null ? oneof_decl.hashCode() : 1);
      result = result * 37 + (options != null ? options.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<DescriptorProto> {

    public String name;
    public List<FieldDescriptorProto> field;
    public List<FieldDescriptorProto> extension;
    public List<DescriptorProto> nested_type;
    public List<EnumDescriptorProto> enum_type;
    public List<ExtensionRange> extension_range;
    public List<OneofDescriptorProto> oneof_decl;
    public MessageOptions options;

    public Builder() {
    }

    public Builder(DescriptorProto message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
      this.field = copyOf(message.field);
      this.extension = copyOf(message.extension);
      this.nested_type = copyOf(message.nested_type);
      this.enum_type = copyOf(message.enum_type);
      this.extension_range = copyOf(message.extension_range);
      this.oneof_decl = copyOf(message.oneof_decl);
      this.options = message.options;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder field(List<FieldDescriptorProto> field) {
      this.field = checkForNulls(field);
      return this;
    }

    public Builder extension(List<FieldDescriptorProto> extension) {
      this.extension = checkForNulls(extension);
      return this;
    }

    public Builder nested_type(List<DescriptorProto> nested_type) {
      this.nested_type = checkForNulls(nested_type);
      return this;
    }

    public Builder enum_type(List<EnumDescriptorProto> enum_type) {
      this.enum_type = checkForNulls(enum_type);
      return this;
    }

    public Builder extension_range(List<ExtensionRange> extension_range) {
      this.extension_range = checkForNulls(extension_range);
      return this;
    }

    public Builder oneof_decl(List<OneofDescriptorProto> oneof_decl) {
      this.oneof_decl = checkForNulls(oneof_decl);
      return this;
    }

    public Builder options(MessageOptions options) {
      this.options = options;
      return this;
    }

    @Override
    public DescriptorProto build() {
      return new DescriptorProto(this);
    }
  }

  public static final class ExtensionRange extends Message {

    public static final Integer DEFAULT_START = 0;
    public static final Integer DEFAULT_END = 0;

    @ProtoField(tag = 1, type = INT32)
    public final Integer start;

    @ProtoField(tag = 2, type = INT32)
    public final Integer end;

    public ExtensionRange(Integer start, Integer end) {
      this.start = start;
      this.end = end;
    }

    private ExtensionRange(Builder builder) {
      this(builder.start, builder.end);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof ExtensionRange)) return false;
      ExtensionRange o = (ExtensionRange) other;
      return equals(start, o.start)
          && equals(end, o.end);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = start != null ? start.hashCode() : 0;
        result = result * 37 + (end != null ? end.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<ExtensionRange> {

      public Integer start;
      public Integer end;

      public Builder() {
      }

      public Builder(ExtensionRange message) {
        super(message);
        if (message == null) return;
        this.start = message.start;
        this.end = message.end;
      }

      public Builder start(Integer start) {
        this.start = start;
        return this;
      }

      public Builder end(Integer end) {
        this.end = end;
        return this;
      }

      @Override
      public ExtensionRange build() {
        return new ExtensionRange(this);
      }
    }
  }
}
