# how to deploy kiosk.html

## Set up the Environment
1. Download Node.js (my version is 2.14.4)
2. Install grunt-cli `npm install -g grunt-cli`
3. Run `npm install` to install dependent libraries

## Build the source codes
1. Open console and cd to webapp directory
2. Run `grunt build`
3. Direcory named "dist" is created and this is the app root of kiosk.html

## Run web server on localhost(Optional)
1. Open console and cd to webapp directory
2. Run `grunt`
3. Access to http://localhost:9000/dist
