/**
 * Copyright 2018 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.transport.test.generic.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.test.generic.GenericWrapper;
import com.linkedin.transport.test.spi.Row;
import com.linkedin.transport.test.spi.types.StructTestType;
import com.linkedin.transport.test.spi.types.TestType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GenericStruct implements StdStruct, PlatformData {

  private Row _struct;
  private final List<String> _fieldNames;
  private final List<TestType> _fieldTypes;

  public GenericStruct(Row struct, TestType type) {
    _struct = struct;
    _fieldNames = ((StructTestType) type).getFieldNames();
    _fieldTypes = ((StructTestType) type).getFieldTypes();
  }

  public GenericStruct(TestType type) {
    this(new Row(new ArrayList<>(Collections.nCopies(((StructTestType) type).getFieldTypes().size(), null))), type);
  }

  @Override
  public Object getUnderlyingData() {
    return _struct;
  }

  @Override
  public void setUnderlyingData(Object value) {
    _struct = (Row) value;
  }

  @Override
  public StdData getField(int index) {
    return GenericWrapper.createStdData(_struct.getFields().get(index), _fieldTypes.get(index));
  }

  @Override
  public StdData getField(String name) {
    return getField(_fieldNames.indexOf(name));
  }

  @Override
  public void setField(int index, StdData value) {
    _struct.getFields().set(index, ((PlatformData) value).getUnderlyingData());
  }

  @Override
  public void setField(String name, StdData value) {
    setField(_fieldNames.indexOf(name), value);
  }

  @Override
  public List<StdData> fields() {
    return IntStream.range(0, _struct.getFields().size()).mapToObj(this::getField).collect(Collectors.toList());
  }
}
