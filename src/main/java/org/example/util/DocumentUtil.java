package org.example.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.exception.PdfGenerationException;
import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
public final class DocumentUtil {

	private static final Logger logger = LoggerFactory.getLogger(DocumentUtil.class);
	private static final int LINE_OFFSET = 10;
	private static final int LINE_HEIGHT = 50;
	public static final PDType1Font DOCUMENT_FONT = PDType1Font.HELVETICA;
	public static final int MAX_LINES_ON_PAGE = 10;

	private static final String FILE_PATH_PREFIX;

	static {
		try {
			FILE_PATH_PREFIX = DocumentUtil.class.getProtectionDomain()
												.getCodeSource()
												.getLocation()
												.toURI()
												+ "tickets";
		} catch (URISyntaxException e) {
			logger.warn("Failed to parse file location: {}", e.getMessage());
			throw new PdfGenerationException("Failed to parse file location.");
		}
	}

	private DocumentUtil(){
		//private constructor
	}

	/**
	 * Writes a list of tickets to pdf file.
	 * @param tickets
	 * @return String path to the file
	 */
	public static String writeToPdf(List<Ticket> tickets) {
		try (PDDocument doc = new PDDocument()) {
			PDPage page = new PDPage();
			doc.addPage(page);

			var contentStream = new PDPageContentStream(doc, page);
			contentStream.setFont(DOCUMENT_FONT, 12);

			int lineNumber = 1;
			float pageHeight = page.getMediaBox().getHeight();

			for (Ticket ticket : tickets) {
				writeLine(contentStream, lineNumber, pageHeight, ticket);
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
			logger.warn("Failed to generate and load PDF file: {}", ex);
			ex.printStackTrace();
			throw new PdfGenerationException("Failed to generate and load PDF file.");
		}
	}

	/**
	 * Created new page in the provided document
	 * @param doc Document
	 * @param contentStream Content stream
	 * @throws IOException
	 */
	private static void addNextPage(PDDocument doc, PDPageContentStream contentStream) throws IOException{
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
	 * @param ticket
	 * @throws IOException
	 */
	private static void writeLine(PDPageContentStream contentStream, int lineNumber, float pageHeight, Ticket ticket) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(LINE_OFFSET, pageHeight - LINE_HEIGHT * lineNumber);
		contentStream.showText(ticket.toString());
		contentStream.endText();
	}

	/**
	 * Create a new pdf file.
	 * @return Created file.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static File createFile() throws IOException, URISyntaxException {
		var mainPath = FILE_PATH_PREFIX + (System.currentTimeMillis() + ".pdf");
		File file = new File(new URI(mainPath));
		var fileCreated = file.createNewFile();
		logger.info("File with tickets created: {}.", fileCreated);
		return file;
	}
}
