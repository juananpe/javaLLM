package eus.ehu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class Main {

    public static void main(String[] args) throws IOException {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(loadApiKey())
                .baseUrl("https://openrouter.ai/api/v1")
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.of("deepseek/deepseek-v4-flash"))
                .addSystemMessage("Eres un asistente útil.")
                .addUserMessage("Lista tres lenguajes de programación con su año de creación.")
                .build();
        ChatCompletion completion = client.chat().completions().create(params);

        String result = completion.choices().get(0).message().content().orElse("");

        System.out.println("Raw result from LLM:");
        System.out.println(result);
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