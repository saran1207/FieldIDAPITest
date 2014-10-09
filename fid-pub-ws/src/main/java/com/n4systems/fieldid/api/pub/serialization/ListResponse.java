// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: /Users/mfrederi/workspaces/web/web/fid-pub-ws/src/main/proto/ListResponse.proto
package com.n4systems.fieldid.api.pub.serialization;

import com.squareup.wire.ExtendableMessage;
import com.squareup.wire.Extension;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class ListResponse extends ExtendableMessage<ListResponse> {

  public static final Integer DEFAULT_PAGE = 0;
  public static final Integer DEFAULT_PAGESIZE = 0;
  public static final Long DEFAULT_TOTAL = 0L;

  @ProtoField(tag = 1, type = INT32, label = REQUIRED)
  public final Integer page;

  @ProtoField(tag = 2, type = INT32, label = REQUIRED)
  public final Integer pageSize;

  @ProtoField(tag = 3, type = INT64, label = REQUIRED)
  public final Long total;

  public ListResponse(Integer page, Integer pageSize, Long total) {
    this.page = page;
    this.pageSize = pageSize;
    this.total = total;
  }

  private ListResponse(Builder builder) {
    this(builder.page, builder.pageSize, builder.total);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ListResponse)) return false;
    ListResponse o = (ListResponse) other;
    if (!extensionsEqual(o)) return false;
    return equals(page, o.page)
        && equals(pageSize, o.pageSize)
        && equals(total, o.total);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = extensionsHashCode();
      result = result * 37 + (page != null ? page.hashCode() : 0);
      result = result * 37 + (pageSize != null ? pageSize.hashCode() : 0);
      result = result * 37 + (total != null ? total.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends ExtendableBuilder<ListResponse> {

    public Integer page;
    public Integer pageSize;
    public Long total;

    public Builder() {
    }

    public Builder(ListResponse message) {
      super(message);
      if (message == null) return;
      this.page = message.page;
      this.pageSize = message.pageSize;
      this.total = message.total;
    }

    public Builder page(Integer page) {
      this.page = page;
      return this;
    }

    public Builder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    public Builder total(Long total) {
      this.total = total;
      return this;
    }

    @Override
    public <E> Builder setExtension(Extension<ListResponse, E> extension, E value) {
      super.setExtension(extension, value);
      return this;
    }

    @Override
    public ListResponse build() {
      checkRequiredFields();
      return new ListResponse(this);
    }
  }
}
