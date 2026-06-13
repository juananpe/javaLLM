# javaLLM

A Java 25 application that calls LLMs via [OpenRouter](https://openrouter.ai/) using the [OpenAI Java SDK](https://github.com/openai/openai-java).

## Prerequisites

- **Java 25**
- **Maven 3.9+**
- An [OpenRouter API key](https://openrouter.ai/keys)

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/juananpe/javaLLM.git
   cd javaLLM
   ```

2. Create a `config.properties` file with your OpenRouter API key:
   ```properties
   OPENROUTER_API_KEY=sk-or-v1-...
   ```

3. Build the project:
   ```bash
   mvn package
   ```

## Usage

Run the basic text completion example:

```bash
mvn exec:java -Dexec.mainClass="eus.ehu.Main"
```

Run the structured JSON completion example (uses Gson for parsing):

```bash
mvn exec:java -Dexec.mainClass="eus.ehu.MainJSON"
```

## Project Structure

```
src/main/java/eus/ehu/
├── Main.java          # Simple text chat completion via OpenRouter
├── MainJSON.java      # JSON-structured completion with Gson parsing
└── module-info.java   # Java module descriptor
```

## Dependencies

- [openai-java](https://github.com/openai/openai-java) — OpenAI SDK client
- [Gson](https://github.com/google/gson) — JSON parsing
- JUnit 5 — Testing
