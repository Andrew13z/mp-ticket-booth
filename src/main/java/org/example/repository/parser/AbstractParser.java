package org.example.repository.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractParser<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractParser.class);

	public Map<Long, T> loadData(final String fileName) {
		try (Stream<String> userInfo = Files.lines(Path.of(fileName))) {
			return userInfo.map(this::parse)
					.collect(Collectors.toMap(this::getId, Function.identity()));
		} catch (IOException ioException) {
			logger.error("Failed to initialize data.");
			ioException.printStackTrace();
		}
		return Map.of();
	}

	public abstract T parse(final String content);

	public abstract long getId(T model);

	public abstract Class<T> getType();

}
