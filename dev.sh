#!/bin/bash
set -a
source .env
set +a

echo "Starting server with hot reload..."
echo "Edit any file in src/ and the server will auto-restart."
echo ""

mvn spring-boot:run &
SERVER_PID=$!

while true; do
    inotifywait -r -e modify,create,delete,move src/ 2>/dev/null
    mvn compile -q 2>/dev/null
done

kill $SERVER_PID 2>/dev/null
