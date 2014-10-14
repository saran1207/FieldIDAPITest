// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;
import okio.ByteString;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.BYTES;
import static com.squareup.wire.Message.Datatype.DOUBLE;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Datatype.UINT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * A message representing a option the parser does not recognize. This only
 * appears in options protos created by the compiler::Parser class.
 * DescriptorPool resolves these when building Descriptor objects. Therefore,
 * options protos in descriptor objects (e.g. returned by Descriptor::options(),
 * or produced by Descriptor::CopyTo()) will never have UninterpretedOptions
 * in them.
 */
public final class UninterpretedOption extends Message {

  public static final List<NamePart> DEFAULT_NAME = Collections.emptyList();
  public static final String DEFAULT_IDENTIFIER_VALUE = "";
  public static final Long DEFAULT_POSITIVE_INT_VALUE = 0L;
  public static final Long DEFAULT_NEGATIVE_INT_VALUE = 0L;
  public static final Double DEFAULT_DOUBLE_VALUE = 0D;
  public static final ByteString DEFAULT_STRING_VALUE = ByteString.EMPTY;
  public static final String DEFAULT_AGGREGATE_VALUE = "";

  @ProtoField(tag = 2, label = REPEATED)
  public final List<NamePart> name;

  /**
   * The value of the uninterpreted option, in whatever type the tokenizer
   * identified it as during parsing. Exactly one of these should be set.
   */
  @ProtoField(tag = 3, type = STRING)
  public final String identifier_value;

  @ProtoField(tag = 4, type = UINT64)
  public final Long positive_int_value;

  @ProtoField(tag = 5, type = INT64)
  public final Long negative_int_value;

  @ProtoField(tag = 6, type = DOUBLE)
  public final Double double_value;

  @ProtoField(tag = 7, type = BYTES)
  public final ByteString string_value;

  @ProtoField(tag = 8, type = STRING)
  public final String aggregate_value;

  public UninterpretedOption(List<NamePart> name, String identifier_value, Long positive_int_value, Long negative_int_value, Double double_value, ByteString string_value, String aggregate_value) {
    this.name = immutableCopyOf(name);
    this.identifier_value = identifier_value;
    this.positive_int_value = positive_int_value;
    this.negative_int_value = negative_int_value;
    this.double_value = double_value;
    this.string_value = string_value;
    this.aggregate_value = aggregate_value;
  }

  private UninterpretedOption(Builder builder) {
    this(builder.name, builder.identifier_value, builder.positive_int_value, builder.negative_int_value, builder.double_value, builder.string_value, builder.aggregate_value);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UninterpretedOption)) return false;
    UninterpretedOption o = (UninterpretedOption) other;
    return equals(name, o.name)
        && equals(identifier_value, o.identifier_value)
        && equals(positive_int_value, o.positive_int_value)
        && equals(negative_int_value, o.negative_int_value)
        && equals(double_value, o.double_value)
        && equals(string_value, o.string_value)
        && equals(aggregate_value, o.aggregate_value);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = name != null ? name.hashCode() : 1;
      result = result * 37 + (identifier_value != null ? identifier_value.hashCode() : 0);
      result = result * 37 + (positive_int_value != null ? positive_int_value.hashCode() : 0);
      result = result * 37 + (negative_int_value != null ? negative_int_value.hashCode() : 0);
      result = result * 37 + (double_value != null ? double_value.hashCode() : 0);
      result = result * 37 + (string_value != null ? string_value.hashCode() : 0);
      result = result * 37 + (aggregate_value != null ? aggregate_value.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<UninterpretedOption> {

    public List<NamePart> name;
    public String identifier_value;
    public Long positive_int_value;
    public Long negative_int_value;
    public Double double_value;
    public ByteString string_value;
    public String aggregate_value;

    public Builder() {
    }

    public Builder(UninterpretedOption message) {
      super(message);
      if (message == null) return;
      this.name = copyOf(message.name);
      this.identifier_value = message.identifier_value;
      this.positive_int_value = message.positive_int_value;
      this.negative_int_value = message.negative_int_value;
      this.double_value = message.double_value;
      this.string_value = message.string_value;
      this.aggregate_value = message.aggregate_value;
    }

    public Builder name(List<NamePart> name) {
      this.name = checkForNulls(name);
      return this;
    }

    /**
     * The value of the uninterpreted option, in whatever type the tokenizer
     * identified it as during parsing. Exactly one of these should be set.
     */
    public Builder identifier_value(String identifier_value) {
      this.identifier_value = identifier_value;
      return this;
    }

    public Builder positive_int_value(Long positive_int_value) {
      this.positive_int_value = positive_int_value;
      return this;
    }

    public Builder negative_int_value(Long negative_int_value) {
      this.negative_int_value = negative_int_value;
      return this;
    }

    public Builder double_value(Double double_value) {
      this.double_value = double_value;
      return this;
    }

    public Builder string_value(ByteString string_value) {
      this.string_value = string_value;
      return this;
    }

    public Builder aggregate_value(String aggregate_value) {
      this.aggregate_value = aggregate_value;
      return this;
    }

    @Override
    public UninterpretedOption build() {
      return new UninterpretedOption(this);
    }
  }

  /**
   * The name of the uninterpreted option.  Each string represents a segment in
   * a dot-separated name.  is_extension is true iff a segment represents an
   * extension (denoted with parentheses in options specs in .proto files).
   * E.g.,{ ["foo", false], ["bar.baz", true], ["qux", false] } represents
   * "foo.(bar.baz).qux".
   */
  public static final class NamePart extends Message {

    public static final String DEFAULT_NAME_PART = "";
    public static final Boolean DEFAULT_IS_EXTENSION = false;

    @ProtoField(tag = 1, type = STRING, label = REQUIRED)
    public final String name_part;

    @ProtoField(tag = 2, type = BOOL, label = REQUIRED)
    public final Boolean is_extension;

    public NamePart(String name_part, Boolean is_extension) {
      this.name_part = name_part;
      this.is_extension = is_extension;
    }

    private NamePart(Builder builder) {
      this(builder.name_part, builder.is_extension);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof NamePart)) return false;
      NamePart o = (NamePart) other;
      return equals(name_part, o.name_part)
          && equals(is_extension, o.is_extension);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = name_part != null ? name_part.hashCode() : 0;
        result = result * 37 + (is_extension != null ? is_extension.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<NamePart> {

      public String name_part;
      public Boolean is_extension;

      public Builder() {
      }

      public Builder(NamePart message) {
        super(message);
        if (message == null) return;
        this.name_part = message.name_part;
        this.is_extension = message.is_extension;
      }

      public Builder name_part(String name_part) {
        this.name_part = name_part;
        return this;
      }

      public Builder is_extension(Boolean is_extension) {
        this.is_extension = is_extension;
        return this;
      }

      @Override
      public NamePart build() {
        checkRequiredFields();
        return new NamePart(this);
      }
    }
  }
}
