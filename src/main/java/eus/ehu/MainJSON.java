package eus.ehu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.ResponseFormatJsonObject;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

record Language(String name, int year_created) {}
record LanguageResponse(List<Language> languages) {}

public class MainJSON {

    public static void main(String[] args) throws IOException {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(loadApiKey())
                .baseUrl("https://openrouter.ai/api/v1")
                .build();

ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .model(ChatModel.of("deepseek/deepseek-v4-pro"))
            .addSystemMessage("You are a helpful assistant. Answer in JSON format. Use this JSON Schema: { languages: [ { name, year_created } ] }")
            .addUserMessage("List three programming languages with their year of creation.")
            .responseFormat(ResponseFormatJsonObject.builder().build())
            .build();
        ChatCompletion completion = client.chat().completions().create(params);

        String jsonResult = completion.choices().get(0).message().content().orElse("");
        
        if (jsonResult.trim().startsWith("```json")) {
            jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
        }

        System.out.println("Raw JSON result from LLM:");
        System.out.println(jsonResult);

        Gson gson = new Gson();
        LanguageResponse response = gson.fromJson(jsonResult, LanguageResponse.class);

        if (response != null && response.languages() != null) {
            for (Language lang : response.languages()) {
                System.out.printf("%s created in %d%n", lang.name(), lang.year_created());
            }
        } else {
            System.out.println("Error: El LLM no devolvió el formato esperado.");
        }
    }

    private static String loadApiKey() throws IOException {
        Path configFile = Path.of("config.properties");
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(configFile)) {
            props.load(in);
        }
        String key = props.getProperty("OPENROUTER_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("OPENROUTER_API_KEY not set in config.properties");
        }
        return key;
    }
}