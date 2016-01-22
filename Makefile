PORT=7122

all:
	./gradlew buildAll

runClient:
	java -jar ./build/libs/toy-chat-room-Client-0.0.1.jar

runServer:
	java -jar ./build/libs/toy-chat-room-Server-0.0.1.jar $(PORT)

clean:
	rm -rf ./build
