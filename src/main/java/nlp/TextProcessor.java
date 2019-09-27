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
}
