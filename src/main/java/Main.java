/**
 * Created by GaPhil on 2019-09-27.
 */

import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String... args) throws Exception {
        // Instantiates a client
        try {
            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));

            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Type.PLAIN_TEXT)
                    .build();

            LanguageServiceClient language = LanguageServiceClient.create();

            AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            AnalyzeEntitiesResponse response = language.analyzeEntities(request);

            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                System.out.printf("Entity: %s\n", entity.getName());
                System.out.printf("Salience: %.3f\n", entity.getSalience());
                System.out.println("Metadata: ");
                for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
                    System.out.printf("%s : %s", entry.getKey(), entry.getValue());
                }
                for (EntityMention mention : entity.getMentionsList()) {
                    System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
                    System.out.printf("Content: %s\n", mention.getText().getContent());
                    System.out.printf("Type: %s\n\n", mention.getType());
                }
            }
        } catch (Exception exception) {
            System.out.print(exception);
        }
    }
}