# Log-Play-App

Log-Play-App is a web application for storing and reading logs of board game sessions. The application is built using Typescript, React, and AWS Lambdas.

## Folder Structure

The repository is organized into three main folders:

### `log-play-page`

This folder contains the React UI for the application. The UI allows users to type in games in form and view past games.

### `log-play-lambda`

This folder contains the AWS Lambdas serving as endpoints for the frontend. The Lambdas handle requests from the frontend and retrieve and store data in the database.

### `log-play-scripts`

This folder contains supplementary scripts related to data preparation. These scripts can be used to import data into the database or export data from the database.


