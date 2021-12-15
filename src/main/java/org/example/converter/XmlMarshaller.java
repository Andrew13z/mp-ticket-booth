package org.example.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Converter for unmarshalling xml file to the specified model type
 *
 * @author Andrii Krokhta
 */
@Component
public class XmlMarshaller {

  private final XmlMapper xmlMapper;

  @Autowired
  public XmlMarshaller(XmlMapper xmlMapper) {
    this.xmlMapper = xmlMapper;
  }

  public <T> List<T> parse(final Resource resource, final TypeReference<List<T>> targetClass) throws IOException {
    try (FileInputStream stream = new FileInputStream(resource.getFile())) {
      return xmlMapper.readValue(stream, targetClass);
    }
  }
}
