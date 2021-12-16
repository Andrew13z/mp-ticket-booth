package org.example.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Converter for unmarshalling xml file with model data to list of models
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

	/**
	 * Reads the resource with model dataand transforms it to the target class.
	 *
	 * @return List of models.
	 */
	public <T> List<T> parse(final Resource resource, final TypeReference<List<T>> targetClass) throws IOException {
		try (FileInputStream stream = new FileInputStream(resource.getFile())) {
			return xmlMapper.readValue(stream, targetClass);
		}

	}

	/**
	 * Reads the resource with model dataand transforms it to the target class.
	 *
	 * @return List of models.
	 */
	public <T> List<T> parse(final InputStream stream, final TypeReference<List<T>> targetClass) throws IOException {
		return xmlMapper.readValue(stream, targetClass);
	}
}
