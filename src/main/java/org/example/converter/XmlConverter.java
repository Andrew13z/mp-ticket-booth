package org.example.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Converter for unmarshalling xml file with model data to list of models
 * @author Andrii Krokhta
 */
@Component
public class XmlConverter<T> {

	@Autowired
	private Unmarshaller unmarshaller;

	/**
	 * Converter the xml file with model data to list of models.
	 * @return List of models.
	 */
	@SuppressWarnings("unchecked")
	public List<T> parseXmlToObjectList(File file) throws IOException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			return (List<T>) unmarshaller.unmarshal(new StreamSource(inputStream));
		}
	}
}
