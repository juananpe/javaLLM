# javaLLM

A Java 25 application that calls LLMs via [OpenRouter](https://openrouter.ai/) using the [OpenAI Java SDK](https://github.com/openai/openai-java).

## Setup

### Option A — Dev Container (recommended)

This repository ships a [dev container](.devcontainer/devcontainer.json) with **Java 25 + Maven 3.9**, so there is nothing to install on your machine.

1. Install [Docker](https://www.docker.com/products/docker-desktop) and the VS Code [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) extension.
2. Open the `javaLLM` folder in VS Code and run **Dev Containers: Reopen in Container**.
3. The first start builds the project and creates a `config.properties` file with a placeholder. Put your OpenRouter API key in it:
   ```properties
   OPENROUTER_API_KEY=sk-or-v1-...
   ```

   > Tip: if you `export OPENROUTER_API_KEY=sk-or-v1-...` on your host before opening the container, the key is written to `config.properties` automatically.
4. Run the demo from the repository root, see [Usage](#usage).

### Option B — Local installation

Prerequisites:

- **Java 25**
- **Maven 3.9+**
- An [OpenRouter API key](https://openrouter.ai/keys)

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

`mvn package` also builds a self-contained jar, which is handy for a live demo (no Maven involved):

```bash
java -jar target/javaLLM-jar-with-dependencies.jar            # Main
java -cp target/javaLLM-jar-with-dependencies.jar eus.ehu.MainJSON
```

> `config.properties` is read from the current directory, so run these commands from the repository root.

> Note: `mvn exec:java` may print `WARNING: ... will linger despite being asked to die via interruption` after the app finishes. This comes from `exec-maven-plugin` forcefully interrupting OkHttp's internal housekeeping threads inside Maven's own JVM; it's harmless and doesn't happen with the `java -jar ...` alternative above.

## Project Structure

```
.devcontainer/          # Java 25 + Maven dev container
src/main/java/eus/ehu/
├── Main.java          # Simple text chat completion via OpenRouter
├── MainJSON.java      # JSON-structured completion with Gson parsing
└── module-info.java   # Java module descriptor
```

## Dependencies

- [openai-java](https://github.com/openai/openai-java) — OpenAI SDK client
- [Gson](https://github.com/google/gson) — JSON parsing
- JUnit 5 — Testing
