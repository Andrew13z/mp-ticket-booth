package org.example.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.exception.PdfGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public final class DocumentUtil<T> {

	private static final Logger logger = LoggerFactory.getLogger(DocumentUtil.class);
	private static final int LINE_OFFSET = 10;
	private static final int LINE_HEIGHT = 50;
	private static final PDType1Font DOCUMENT_FONT = PDType1Font.HELVETICA;
	private static final int MAX_LINES_ON_PAGE = 10;
	private static final String FILE_PATH_PREFIX = DocumentUtil.class.getProtectionDomain()
																		.getCodeSource()
																		.getLocation()
																		.toString();

	/**
	 * Writes a list of models to pdf file.
	 * @param models
	 * @return String path to the file
	 */
	public String writeToPdf(List<T> models) {
		try (PDDocument doc = new PDDocument()) {
			PDPage page = new PDPage();
			doc.addPage(page);

			var contentStream = new PDPageContentStream(doc, page);
			contentStream.setFont(DOCUMENT_FONT, 12);

			int lineNumber = 1;
			float pageHeight = page.getMediaBox().getHeight();

			for (T model : models) {
				writeLine(contentStream, lineNumber, pageHeight, model);
				lineNumber++;
				if (lineNumber > MAX_LINES_ON_PAGE) {
					addNextPage(doc, contentStream);
				}
			}
			contentStream.close();

			var realFile = createFile();
			doc.save(realFile);
			return realFile.getPath();
		} catch (IOException | URISyntaxException ex) {
			throw new PdfGenerationException("Failed to generate and load PDF file.", ex);
		}
	}

	/**
	 * Created new page in the provided document
	 * @param doc Document
	 * @param contentStream Content stream
	 * @throws IOException
	 */
	private void addNextPage(PDDocument doc, PDPageContentStream contentStream) throws IOException{
		PDPage page2 = new PDPage();
		doc.addPage(page2);
		contentStream.close();
		contentStream = new PDPageContentStream(doc, page2);
		contentStream.setFont(DOCUMENT_FONT, 12);
	}

	/**
	 * Writes a line of text to the document.
	 * @param contentStream
	 * @param lineNumber
	 * @param pageHeight
	 * @param model model to print
	 * @throws IOException
	 */
	private void writeLine(PDPageContentStream contentStream,
								  int lineNumber,
								  float pageHeight,
								  T model) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(LINE_OFFSET, pageHeight - LINE_HEIGHT * lineNumber);
		contentStream.showText(model.toString());
		contentStream.endText();
	}

	/**
	 * Create a new pdf file.
	 * @return Created file.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private File createFile() throws IOException, URISyntaxException {
		var filePath = FILE_PATH_PREFIX + (System.currentTimeMillis() + ".pdf");
		File file = new File(new URI(filePath));
		file.createNewFile();
		logger.info("File created at {}.", file.getPath());
		return file;
	}
}
