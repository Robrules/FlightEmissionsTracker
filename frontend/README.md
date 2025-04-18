# ELEC5619_Project Backend

This is the frontend of our Flight Emissions Tracker, bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Pre-Requisites

You will need the following before you use this service:

- [Node.js (to use npm)](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)

## How to Run

To run the frontend service, first run the below terminal command in the current directory. This will install all required packages.

`npm install`

Then run the following command:

`npm start`

This runs the app in the development mode and can be viewed in the browser at [localhost:3000](http://localhost:3000).

## How to build

To build the application to the `build` folder, run:

`npm run build`

Your app is ready to be deployed!

## How to format

To make the code consistent please regularly run the below commands:

`npx prettier --write .`

This command will reformat all the relevant files in the frontend directory, so it might take a while. To target a specific directory, run `npx prettier --write directory/` instead. Alternatively, if you want to check if files have been formatted but you do not want them to be overwritten, run `npx prettier --check .`.
