package org.safegees.safegees.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.safegees.safegees.model.Friend;
import org.safegees.safegees.model.PublicUser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by victor on 16/8/16.
 */
public class DownloadPublicUsersImagesController {

    private Context context;
    private ArrayList<PublicUser> publicUser;

    public DownloadPublicUsersImagesController(Context context, ArrayList<PublicUser> publicUser) {
        this.context = context;
        this.publicUser = publicUser;
    }

    public void run(){
        if (publicUser.size()>0){
            SendAddContactTask sact = new SendAddContactTask(context,this.publicUser);
            sact.execute();
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class SendAddContactTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ArrayList<PublicUser> publicUsers;


        SendAddContactTask(Context context, ArrayList<PublicUser> publicUsers) {
            this.context = context;
            this.publicUsers = publicUsers;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: it will change the process. This one doesnt update the image. It will use the md5 key.
            File f = new File(ImageController.getUserImageFileNameByEmail(context, publicUsers.get(0).getPublicEmail()));
            if (!f.exists() && publicUsers.get(0).getAvatar() != null && !publicUsers.get(0).getAvatar().equals("")){
                return  downloadBitmap(publicUsers.get(0));
            }else{
                publicUsers.remove(publicUsers.get(0));
            }
            return false;
        }

        private Boolean downloadBitmap(PublicUser publicUser) {

            String url = publicUser.getAvatar();
                // initilize the default HTTP client object
                final DefaultHttpClient client = new DefaultHttpClient();

                //forming a HttoGet request
                final HttpGet getRequest = new HttpGet(url);
                try {

                    HttpResponse response = client.execute(getRequest);

                    //check 200 OK for success
                    final int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode != HttpStatus.SC_OK) {
                        Log.w("ImageDownloader", "Error " + statusCode +
                                " while retrieving bitmap from " + url);
                        return false;

                    }

                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = null;
                        try {
                            // getting contents from the stream
                            inputStream = entity.getContent();

                            // decoding stream data back into image Bitmap that android understands
                            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ImageController.storeUserBitmapWithEmail(context, bitmap, publicUser.getPublicEmail());
                            //Remove the downloaded friend from the list
                            publicUsers.remove(publicUser);
                            return true;
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        }
                    }
                } catch (Exception e) {
                    // You Could provide a more explicit error message for IOException
                    getRequest.abort();
                    Log.e("ImageDownloader", "Something went wrong while" +
                            " retrieving bitmap from " + url + e.toString());
                }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (publicUsers.size()>0){
                SendAddContactTask sendAddContactTask = new SendAddContactTask(context, publicUsers);
                sendAddContactTask.execute();
            }

        }



    }
}
