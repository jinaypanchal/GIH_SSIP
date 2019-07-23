Requirements:
- Node.js
- npm package manager

Steps: 
1. Copy server.js, functions.js and package.json in a folder.
2. Open Command Prompt/Terminal in that folder.
3. run the following commands
   - npm init
   - npm install
   - node server.js
4. Make a POST api call to the following url
   url : "localhost:3000/crop"
5. Attach the api request body in JSON format having the structure shown below:
	{
	"city" : "_city name here_",
	"temp" : "temprature here"
	"moisture" : "_moisture here_"
	}