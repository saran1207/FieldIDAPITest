// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/kirillternovsky/Documents/web/fid-pub-ws/src/main/proto/descriptor.proto
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.PACKED;
import static com.squareup.wire.Message.Label.REPEATED;

/**
 * ===================================================================
 * Optional source code info
 * Encapsulates information about the original source file from which a
 * FileDescriptorProto was generated.
 */
public final class SourceCodeInfo extends Message {

  public static final List<Location> DEFAULT_LOCATION = Collections.emptyList();

  /**
   * A Location identifies a piece of source code in a .proto file which
   * corresponds to a particular definition.  This information is intended
   * to be useful to IDEs, code indexers, documentation generators, and similar
   * tools.
   *
   * For example, say we have a file like:
   *   message Foo {
   *     optional string foo = 1;
   *   }
   * Let's look at just the field definition:
   *   optional string foo = 1;
   *   ^       ^^     ^^  ^  ^^^
   *   a       bc     de  f  ghi
   * We have the following locations:
   *   span   path               represents
   *   [a,i)  [ 4, 0, 2, 0 ]     The whole field definition.
   *   [a,b)  [ 4, 0, 2, 0, 4 ]  The label (optional).
   *   [c,d)  [ 4, 0, 2, 0, 5 ]  The type (string).
   *   [e,f)  [ 4, 0, 2, 0, 1 ]  The name (foo).
   *   [g,h)  [ 4, 0, 2, 0, 3 ]  The number (1).
   *
   * Notes:
   * - A location may refer to a repeated field itself (i.e. not to any
   *   particular index within it).  This is used whenever a set of elements are
   *   logically enclosed in a single code segment.  For example, an entire
   *   extend block (possibly containing multiple extension definitions) will
   *   have an outer location whose path refers to the "extensions" repeated
   *   field without an index.
   * - Multiple locations may have the same path.  This happens when a single
   *   logical declaration is spread out across multiple places.  The most
   *   obvious example is the "extend" block again -- there may be multiple
   *   extend blocks in the same scope, each of which will have the same path.
   * - A location's span is not always a subset of its parent's span.  For
   *   example, the "extendee" of an extension declaration appears at the
   *   beginning of the "extend" block and is shared by all extensions within
   *   the block.
   * - Just because a location's span is a subset of some other location's span
   *   does not mean that it is a descendent.  For example, a "group" defines
   *   both a type and a field in a single declaration.  Thus, the locations
   *   corresponding to the type and field and their components will overlap.
   * - Code which tries to interpret locations should probably be designed to
   *   ignore those that it doesn't understand, as more types of locations could
   *   be recorded in the future.
   */
  @ProtoField(tag = 1, label = REPEATED)
  public final List<Location> location;

  public SourceCodeInfo(List<Location> location) {
    this.location = immutableCopyOf(location);
  }

  private SourceCodeInfo(Builder builder) {
    this(builder.location);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SourceCodeInfo)) return false;
    return equals(location, ((SourceCodeInfo) other).location);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = location != null ? location.hashCode() : 1);
  }

  public static final class Builder extends Message.Builder<SourceCodeInfo> {

    public List<Location> location;

    public Builder() {
    }

    public Builder(SourceCodeInfo message) {
      super(message);
      if (message == null) return;
      this.location = copyOf(message.location);
    }

    /**
     * A Location identifies a piece of source code in a .proto file which
     * corresponds to a particular definition.  This information is intended
     * to be useful to IDEs, code indexers, documentation generators, and similar
     * tools.
     *
     * For example, say we have a file like:
     *   message Foo {
     *     optional string foo = 1;
     *   }
     * Let's look at just the field definition:
     *   optional string foo = 1;
     *   ^       ^^     ^^  ^  ^^^
     *   a       bc     de  f  ghi
     * We have the following locations:
     *   span   path               represents
     *   [a,i)  [ 4, 0, 2, 0 ]     The whole field definition.
     *   [a,b)  [ 4, 0, 2, 0, 4 ]  The label (optional).
     *   [c,d)  [ 4, 0, 2, 0, 5 ]  The type (string).
     *   [e,f)  [ 4, 0, 2, 0, 1 ]  The name (foo).
     *   [g,h)  [ 4, 0, 2, 0, 3 ]  The number (1).
     *
     * Notes:
     * - A location may refer to a repeated field itself (i.e. not to any
     *   particular index within it).  This is used whenever a set of elements are
     *   logically enclosed in a single code segment.  For example, an entire
     *   extend block (possibly containing multiple extension definitions) will
     *   have an outer location whose path refers to the "extensions" repeated
     *   field without an index.
     * - Multiple locations may have the same path.  This happens when a single
     *   logical declaration is spread out across multiple places.  The most
     *   obvious example is the "extend" block again -- there may be multiple
     *   extend blocks in the same scope, each of which will have the same path.
     * - A location's span is not always a subset of its parent's span.  For
     *   example, the "extendee" of an extension declaration appears at the
     *   beginning of the "extend" block and is shared by all extensions within
     *   the block.
     * - Just because a location's span is a subset of some other location's span
     *   does not mean that it is a descendent.  For example, a "group" defines
     *   both a type and a field in a single declaration.  Thus, the locations
     *   corresponding to the type and field and their components will overlap.
     * - Code which tries to interpret locations should probably be designed to
     *   ignore those that it doesn't understand, as more types of locations could
     *   be recorded in the future.
     */
    public Builder location(List<Location> location) {
      this.location = checkForNulls(location);
      return this;
    }

    @Override
    public SourceCodeInfo build() {
      return new SourceCodeInfo(this);
    }
  }

  public static final class Location extends Message {

    public static final List<Integer> DEFAULT_PATH = Collections.emptyList();
    public static final List<Integer> DEFAULT_SPAN = Collections.emptyList();
    public static final String DEFAULT_LEADING_COMMENTS = "";
    public static final String DEFAULT_TRAILING_COMMENTS = "";

    /**
     * Identifies which part of the FileDescriptorProto was defined at this
     * location.
     *
     * Each element is a field number or an index.  They form a path from
     * the root FileDescriptorProto to the place where the definition.  For
     * example, this path:
     *   [ 4, 3, 2, 7, 1 ]
     * refers to:
     *   file.message_type(3)  // 4, 3
     *       .field(7)         // 2, 7
     *       .name()           // 1
     * This is because FileDescriptorProto.message_type has field number 4:
     *   repeated DescriptorProto message_type = 4;
     * and DescriptorProto.field has field number 2:
     *   repeated FieldDescriptorProto field = 2;
     * and FieldDescriptorProto.name has field number 1:
     *   optional string name = 1;
     *
     * Thus, the above path gives the location of a field name.  If we removed
     * the last element:
     *   [ 4, 3, 2, 7 ]
     * this path refers to the whole field declaration (from the beginning
     * of the label to the terminating semicolon).
     */
    @ProtoField(tag = 1, type = INT32, label = PACKED)
    public final List<Integer> path;

    /**
     * Always has exactly three or four elements: start line, start column,
     * end line (optional, otherwise assumed same as start line), end column.
     * These are packed into a single field for efficiency.  Note that line
     * and column numbers are zero-based -- typically you will want to add
     * 1 to each before displaying to a user.
     */
    @ProtoField(tag = 2, type = INT32, label = PACKED)
    public final List<Integer> span;

    /**
     * If this SourceCodeInfo represents a complete declaration, these are any
     * comments appearing before and after the declaration which appear to be
     * attached to the declaration.
     *
     * A series of line comments appearing on consecutive lines, with no other
     * tokens appearing on those lines, will be treated as a single comment.
     *
     * Only the comment content is provided; comment markers (e.g. //) are
     * stripped out.  For block comments, leading whitespace and an asterisk
     * will be stripped from the beginning of each line other than the first.
     * Newlines are included in the output.
     *
     * Examples:
     *
     *   optional int32 foo = 1;  // Comment attached to foo.
     *   // Comment attached to bar.
     *   optional int32 bar = 2;
     *
     *   optional string baz = 3;
     *   // Comment attached to baz.
     *   // Another line attached to baz.
     *
     *   // Comment attached to qux.
     *   //
     *   // Another line attached to qux.
     *   optional double qux = 4;
     *
     *   optional string corge = 5;
     *   /* Block comment attached
     *    * to corge.  Leading asterisks
     *    * will be removed. *
     *   /* Block comment attached to
     *    * grault. *
     *   optional int32 grault = 6;
     */
    @ProtoField(tag = 3, type = STRING)
    public final String leading_comments;

    @ProtoField(tag = 4, type = STRING)
    public final String trailing_comments;

    public Location(List<Integer> path, List<Integer> span, String leading_comments, String trailing_comments) {
      this.path = immutableCopyOf(path);
      this.span = immutableCopyOf(span);
      this.leading_comments = leading_comments;
      this.trailing_comments = trailing_comments;
    }

    private Location(Builder builder) {
      this(builder.path, builder.span, builder.leading_comments, builder.trailing_comments);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Location)) return false;
      Location o = (Location) other;
      return equals(path, o.path)
          && equals(span, o.span)
          && equals(leading_comments, o.leading_comments)
          && equals(trailing_comments, o.trailing_comments);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = path != null ? path.hashCode() : 1;
        result = result * 37 + (span != null ? span.hashCode() : 1);
        result = result * 37 + (leading_comments != null ? leading_comments.hashCode() : 0);
        result = result * 37 + (trailing_comments != null ? trailing_comments.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<Location> {

      public List<Integer> path;
      public List<Integer> span;
      public String leading_comments;
      public String trailing_comments;

      public Builder() {
      }

      public Builder(Location message) {
        super(message);
        if (message == null) return;
        this.path = copyOf(message.path);
        this.span = copyOf(message.span);
        this.leading_comments = message.leading_comments;
        this.trailing_comments = message.trailing_comments;
      }

      /**
       * Identifies which part of the FileDescriptorProto was defined at this
       * location.
       *
       * Each element is a field number or an index.  They form a path from
       * the root FileDescriptorProto to the place where the definition.  For
       * example, this path:
       *   [ 4, 3, 2, 7, 1 ]
       * refers to:
       *   file.message_type(3)  // 4, 3
       *       .field(7)         // 2, 7
       *       .name()           // 1
       * This is because FileDescriptorProto.message_type has field number 4:
       *   repeated DescriptorProto message_type = 4;
       * and DescriptorProto.field has field number 2:
       *   repeated FieldDescriptorProto field = 2;
       * and FieldDescriptorProto.name has field number 1:
       *   optional string name = 1;
       *
       * Thus, the above path gives the location of a field name.  If we removed
       * the last element:
       *   [ 4, 3, 2, 7 ]
       * this path refers to the whole field declaration (from the beginning
       * of the label to the terminating semicolon).
       */
      public Builder path(List<Integer> path) {
        this.path = checkForNulls(path);
        return this;
      }

      /**
       * Always has exactly three or four elements: start line, start column,
       * end line (optional, otherwise assumed same as start line), end column.
       * These are packed into a single field for efficiency.  Note that line
       * and column numbers are zero-based -- typically you will want to add
       * 1 to each before displaying to a user.
       */
      public Builder span(List<Integer> span) {
        this.span = checkForNulls(span);
        return this;
      }

      /**
       * If this SourceCodeInfo represents a complete declaration, these are any
       * comments appearing before and after the declaration which appear to be
       * attached to the declaration.
       *
       * A series of line comments appearing on consecutive lines, with no other
       * tokens appearing on those lines, will be treated as a single comment.
       *
       * Only the comment content is provided; comment markers (e.g. //) are
       * stripped out.  For block comments, leading whitespace and an asterisk
       * will be stripped from the beginning of each line other than the first.
       * Newlines are included in the output.
       *
       * Examples:
       *
       *   optional int32 foo = 1;  // Comment attached to foo.
       *   // Comment attached to bar.
       *   optional int32 bar = 2;
       *
       *   optional string baz = 3;
       *   // Comment attached to baz.
       *   // Another line attached to baz.
       *
       *   // Comment attached to qux.
       *   //
       *   // Another line attached to qux.
       *   optional double qux = 4;
       *
       *   optional string corge = 5;
       *   /* Block comment attached
       *    * to corge.  Leading asterisks
       *    * will be removed. *
       *   /* Block comment attached to
       *    * grault. *
       *   optional int32 grault = 6;
       */
      public Builder leading_comments(String leading_comments) {
        this.leading_comments = leading_comments;
        return this;
      }

      public Builder trailing_comments(String trailing_comments) {
        this.trailing_comments = trailing_comments;
        return this;
      }

      @Override
      public Location build() {
        return new Location(this);
      }
    }
  }
}
