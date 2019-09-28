package nlp;

import com.google.cloud.language.v1.*;

import java.io.IOException;
import java.util.logging.Logger;


public class TextProcessor {

    private final static Logger LOG = Logger.getLogger(TextProcessor.class.getName());

    public AnalyzeEntitiesResponse processEntities(String text) {
        try {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            LanguageServiceClient language = LanguageServiceClient.create();

            AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            return language.analyzeEntities(request);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
            return null;
        }
    }

    public AnalyzeSyntaxResponse processSyntax(String text) {
        try {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            LanguageServiceClient language = LanguageServiceClient.create();

            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            return language.analyzeSyntax(request);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
            return null;
        }
    }

    public AnnotateTextResponse processAll(String text) {
        try {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            LanguageServiceClient language = LanguageServiceClient.create();

            AnnotateTextRequest request = AnnotateTextRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            return language.annotateText(request);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
            return null;
        }
    }
}
