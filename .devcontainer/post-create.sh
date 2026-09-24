#!/usr/bin/env bash
#
# Setup of the javaLLM dev container. Executed once after the container is created
# (see "postCreateCommand" in devcontainer.json).
#
# It leaves the container ready to run the demo:
#   - Maven local repository owned by the vscode user
#   - config.properties with the OpenRouter API key
#   - dependencies resolved and the runnable jar already built
#
set -euo pipefail

cd "$(dirname "$0")/.." # repository root

echo "==> javaLLM dev container setup"
echo "    $(java -version 2>&1 | sed -n '1p')"
echo "    $(mvn -v 2>&1 | sed -n '1p')"

# The ~/.m2 volume is created by Docker as root: hand it over to the vscode user.
sudo chown -R vscode:vscode "${HOME}/.m2"

# config.properties holds the API key and is git-ignored (see .gitignore).
if [ -f config.properties ]; then
  echo "==> config.properties already exists, left untouched"
elif [ -n "${OPENROUTER_API_KEY:-}" ]; then
  printf 'OPENROUTER_API_KEY=%s\n' "${OPENROUTER_API_KEY}" > config.properties
  echo "==> config.properties created from the OPENROUTER_API_KEY host variable"
else
  printf 'OPENROUTER_API_KEY=sk-or-v1-REPLACE_WITH_YOUR_OWN_KEY\n' > config.properties
  echo "==> config.properties placeholder created: put your OpenRouter key in it"
fi

# Resolve dependencies and build the runnable jar so the first run is instant.
# Runs detached so container creation doesn't block on the build/download time;
# progress is written to build.log instead of the setup terminal.
nohup mvn -B -DskipTests package > build.log 2>&1 &
disown

cat <<'EOF'

==> Setup finished. The first Maven build is running in the background
    (dependency download + package), see progress with:

    tail -f build.log

==> Once it's done, run the demo from the repository root:

    mvn exec:exec -Dexec.executable=java '-Dexec.args=-classpath %classpath eus.ehu.Main'        # text completion
    mvn exec:exec -Dexec.executable=java '-Dexec.args=-classpath %classpath eus.ehu.MainJSON'    # structured JSON completion

  or use the fat jar built by this script:

    java -jar target/javaLLM-jar-with-dependencies.jar
EOF
