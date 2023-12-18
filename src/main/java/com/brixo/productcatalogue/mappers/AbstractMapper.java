package com.brixo.productcatalogue.mappers;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AbstractMapper{

  private final ModelMapper modelMapper;

  public AbstractMapper() {
    this.modelMapper = new ModelMapper();
  }

  public <S, D> D map(S source, Class<D> destinationType) {
    return modelMapper.map(source, destinationType);
  }

  public <S, D> List<D> mapAll(List<S> source, Class<D> destinationType) {
    return source.stream().map(entity -> map(entity, destinationType)).toList();
  }
}
