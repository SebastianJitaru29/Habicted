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
  // Response sent from firebase, has to be in json format with data camp
  response.json({data: {message: "Hello from Firebase!"}});
});

exports.sendNotificationOnNewTask = functions.firestore
    .document("Groups/{groupID}/taskList/{taskID}")
    .onCreate(async (snap, context) => {
        const taskID = context.params.taskID;
        const groupID = context.params.groupID;

        try {
            // Fetch the task document to get task details if necessary
            const taskDoc = await admin.firestore().doc(`Groups/${groupID}/taskList/${taskID}`).get();
            const taskData = taskDoc.data();
            const taskTitle = taskData ? taskData.title : "New Task";

            // Fetch the group document from Firestore
            const grpDoc = await admin.firestore().doc(`Groups/${groupID}`).get();
            const groupData = grpDoc.data();
            const groupTitle = groupData ? groupData.title : "Unknown Group";

            // Construct the notification message
            const message = {
                notification: {
                    title: "New Task",
                    body: `${groupTitle}: ${taskTitle}`,
                },
                topic: groupID, // Use groupID as the topic
            };

            // Send the notification
            await admin.messaging().send(message);
            console.log("Successfully sent message");
        } catch (error) {
            console.error("Error sending notification", error);
        }
    });
