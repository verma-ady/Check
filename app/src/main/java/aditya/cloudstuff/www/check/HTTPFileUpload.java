package aditya.cloudstuff.www.check;

/**
 * Created by Mukesh on 12/19/2015.
 */
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPFileUpload implements Runnable{
    URL connectURL;
    String responseString;
    String Title;
    String Description;
    String vBase64;
    byte[ ] dataToServer;

    HTTPFileUpload(String urlString, String vTitle, String vDesc, String vbase64){
        try{
            connectURL = new URL(urlString);
            Title= vTitle;
            Description = vDesc;
            vBase64 = vbase64;
        }catch(Exception ex){
            Log.v("MyApp","URL Malformatted");
        }
    }

    void Send_Now(){
        Sending();
    }

    void Sending(){
        upload up = new upload();
        up.execute();
    }

    public  class upload extends AsyncTask<Void, Void, Void >{

        @Override
        protected Void doInBackground(Void... params) {

            String iFileName = "resume";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag="MyApp";

            try
            {
                Log.v(Tag,"Starting Http File Sending to URL");

                // Open a HTTP connection to the URL
                HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

                // Allow Inputs
                conn.setDoInput(true);

                // Allow Outputs
                conn.setDoOutput(true);

                // Don't use a cached copy.
                conn.setUseCaches(false);

                // Use a post method.
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");

                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(vBase64);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(Description);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(vBase64);
                Log.v(Tag,"Headers are written");

                // create a buffer of maximum size
//                int bytesAvailable = fileInputStream.available();
//
//                int maxBufferSize = 1024;
//                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                byte[ ] buffer = new byte[bufferSize];
//
//                // read file and write it into form...
//                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0)
//                {
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0,bufferSize);
//                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // close streams
//                fileInputStream.close();

                dos.flush();

                Log.v(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

                InputStream is = conn.getInputStream();

                // retrieve the response from server
                int ch;

                StringBuffer b =new StringBuffer();
                while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
                String s=b.toString();
                Log.v("MyApp", "Response"+ s);
                dos.close();
            }
            catch (MalformedURLException ex)
            {
                Log.v(Tag, "URL error: " + ex.getMessage(), ex);
            }

            catch (IOException ioe)
            {
                Log.v(Tag, "IO error: " + ioe.getMessage(), ioe);
            }

            return null;
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}