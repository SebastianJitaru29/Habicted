/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started
const functions = require("firebase-functions");

exports.helloWorld = onRequest((request, response) => {
  logger.info("Hello logs!", {structuredData: true});
  response.send("Hello from Firebase!");
});

exports.helloWorld2 = functions.https.onRequest((request, response) => {
  logger.info("Hello logs!", {structuredData: true});
  response.json({data: {message: "Hello from Firebase!"}});
});

const admin = require("firebase-admin");

admin.initializeApp();

exports.sendNotification = functions.https.onRequest((request, response) => {
  const message = {
    notification: {
      title: "Sent with functions",
      body: "This is a notification from Firebase Cloud Functions",
    },
    topic: "testTopic",
  };

  admin.messaging().send(message).then((response) => {
    console.log("Successfully sent message:", response);
  });
  //Response sent from firebase, has to be in json format with data camp
  response.json({data: {message: "Hello from Firebase!"}});
});
