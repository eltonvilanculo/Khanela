import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

export const sendNotificationOnSendMsg = functions.database.ref("/CHAT_ROOM/{chatRoomId}/{msgId}/").onCreate((dataSnapshot, event) => {

    const myId: string = dataSnapshot.val().senderId;
    const chatId: string = event.params.chatRoomId;
    const mText: string = dataSnapshot.val().text;
    const mDate: string = dataSnapshot.val().sendDate + " ";

    const payload = {
        data:{
            senderId: myId,
            text: mText,
            date: mDate
        }

    };

    admin.messaging().sendToTopic(chatId, payload).then(() => {
        console.log("nofification sent");
    }).catch((onFailed) => {
        console.log("nofification not sent");
    });

});



